package by.post.control.ui.dialogs;

import by.post.control.db.TableType;
import by.post.control.events.RootEventTarget;
import by.post.control.events.TableSelectionEvent;
import by.post.control.search.SearchProvider;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    private boolean searchRunning;
    private SearchProvider searchProvider;
    //It is used to display the time spent on search
    private Timeline timeline;
    private long startSearchTime;
    private Map<String, Table> tables;

    public SearchToolDialogController() {

    }

    /**
     * Action for Enter key
     */
    @FXML
    public void onKeyReleased(KeyEvent event) {

        if (event.getCode() == KeyCode.ENTER) {
            search();
        }
    }

    /**
     * Action for close dialog request or cancel button
     */
    public void onCloseRequest() {
        setProgressVisible(false);
        searchProvider.setTerminate(true);
    }

    /**
     * ListView actions for found tables
     *
     * @param event
     */
    @FXML
    public void onListMouseClick(MouseEvent event) throws IOException {

        if (event.getClickCount() == 2) {
            selectItem();
        }
    }

    @FXML
    public void onListKeyReleased(KeyEvent event) throws IOException {

        if (event.getCode() == KeyCode.ENTER) {
            selectItem();
        }
    }

    @FXML
    private void initialize() {

        tables = new HashMap<>();
        searchProvider = new SearchProvider();
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

        searchProvider.setTerminate(false);
        setProgressVisible(true);
        dialogPane.setExpanded(false);
        dialogPane.setExpandableContent(null);

        Task<Boolean> task = new Task<Boolean>() {
            @Override
            protected Boolean call() throws Exception {
                List<Table> founded = searchProvider.getSearchResult(searchText);
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
     * @param visible
     */

    private void setProgressVisible(boolean visible) {

        progress.setVisible(visible);
        progress.setDisable(!visible);
        progressLabel.setVisible(visible);
        searchRunning = visible;
        //For first run
        timeLabel.setVisible(visible ? visible : !visible);
        timeValueLabel.setVisible(visible ? visible : !visible);
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