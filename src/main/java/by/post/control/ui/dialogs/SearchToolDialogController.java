package by.post.control.ui.dialogs;

import by.post.control.db.TableType;
import by.post.control.events.RootEventTarget;
import by.post.control.events.TableSelectionEvent;
import by.post.control.search.Search;
import by.post.data.Table;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.util.Duration;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.ServiceLoader;

/**
 * @author Dmitriy V.Yefremov
 */
public class SearchToolDialogController {

    @FXML
    private TextField searchField;
    @FXML
    private ProgressBar progress;
    @FXML
    private Label progressLabel;
    @FXML
    private DialogPane dialogPane;
    @FXML
    private ListView listView;
    @FXML
    private Label timeValueLabel;
    @FXML
    private Label timeLabel;
    @FXML
    private ButtonType cancelButton;
    @FXML
    private ButtonType searchButton;
    @FXML
    private ComboBox<String> comboBox;

    private boolean searchRunning;
    private Search search;
    //It is used to display the time spent on search
    private Timeline timeline;
    private long startSearchTime;
    private Map<String, Table> tables;

    public SearchToolDialogController() {

    }

    /**
     * Action for close dialog request or cancel button
     */
    public void onCloseRequest() {
        setProgressVisible(false);
        search.cancel();
    }

    /**
     * Action for Enter key
     */
    @FXML
    private void onKeyReleased(KeyEvent event) {

        if (event.getCode() == KeyCode.ENTER) {
            search();
        }
    }

    /**
     * ListView actions for found tables
     *
     * @param event
     */
    @FXML
    private void onListMouseClick(MouseEvent event) throws IOException {

        if (event.getClickCount() == 2) {
            selectItem();
        }
    }

    @FXML
    private void onListKeyReleased(KeyEvent event) throws IOException {

        if (event.getCode() == KeyCode.ENTER) {
            selectItem();
        }
    }

    @FXML
    private void onComboBoxAction() {
        Platform.runLater(() -> {
            if (comboBox.getValue() != null) {
                searchField.setText(comboBox.getValue());
            }
            comboBox.getSelectionModel().clearSelection();
        });
    }

    @FXML
    private void initialize() {

        tables = new HashMap<>();
        search = ServiceLoader.load(Search.class).iterator().next();
        dialogPane.setExpandableContent(null);

        timeline = new Timeline(new KeyFrame(Duration.seconds(0), event -> {
            if (searchRunning) {
                timeValueLabel.setText((System.currentTimeMillis() - startSearchTime) / 1000 + "s");
            }
        }), new KeyFrame(Duration.seconds(1)));

        timeline.setCycleCount(Animation.INDEFINITE);
        //Adding filter on cancel button click
        dialogPane.lookupButton(cancelButton).addEventFilter(ActionEvent.ACTION, event -> {
            onCloseRequest();
            event.consume();
        });

        dialogPane.lookupButton(searchButton).addEventFilter(ActionEvent.ACTION, event -> {
            search();
            event.consume();
        });
    }

    private void search() {

        String searchText = searchField.getText();

        if (searchRunning || searchText.isEmpty()) {
            String error = searchRunning ? "Search is already running!" : "Blank query";
            new Alert(Alert.AlertType.ERROR, error).showAndWait();
            return;
        }

        updateComboBox(searchText);
        search.cancel();
        setProgressVisible(true);
        dialogPane.setExpanded(false);
        dialogPane.setExpandableContent(null);

        Task<Boolean> task = new Task<Boolean>() {
            @Override
            protected Boolean call() throws Exception {
                Collection<Table> founded = search.search(searchText);
                tables.clear();
                Platform.runLater(() -> {
                    listView.getItems().clear();
                    founded.forEach(t -> {
                        listView.getItems().add(t.getName());
                        tables.put(t.getName(), t);
                    });
                });

                return !tables.isEmpty();
            }
        };

        task.setOnSucceeded(event -> {
            dialogPane.setExpandableContent(listView);
            dialogPane.setExpanded(true);
            setProgressVisible(false);
            timeline.stop();
        });

        Thread thread = new Thread(task);
        thread.setDaemon(true);
        thread.start();

        startSearchTime = System.currentTimeMillis();
        timeline.playFromStart();
    }

    /**
     * Updating and storing search requests in combobox
     *
     * @param searchText
     */
    private void updateComboBox(String searchText) {

        if (!comboBox.getItems().contains(searchText)) {
            Platform.runLater(() -> comboBox.getItems().add(searchText));
        }
    }

    /**
     * @param visible
     */
    private void setProgressVisible(boolean visible) {

        progress.setVisible(visible);
        progress.setDisable(!visible);
        progressLabel.setVisible(visible);
        searchRunning = visible;
        //For first run
        timeLabel.setVisible(visible);
        timeValueLabel.setVisible(visible);
    }

    /**
     * Select item in table tree by table name
     */
    private void selectItem() throws IOException {

        Table table = tables.get(String.valueOf(listView.getSelectionModel().getSelectedItem()));

        if (table != null) {
            table.setType(TableType.TABLE);
            Event.fireEvent(RootEventTarget.TARGET, new TableSelectionEvent(table));
        }
    }
}