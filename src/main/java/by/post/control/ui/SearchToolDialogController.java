package by.post.control.ui;

import by.post.search.SearchProvider;
import javafx.collections.FXCollections;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

import java.util.List;

/**
 * @author Dmitriy V.Yefremov
 */
public class SearchToolDialogController {

    @FXML
    private Dialog dialog;
    @FXML
    private ListView listView;
    @FXML
    private TextField searchField;
    @FXML
    private ProgressBar progress;
    @FXML
    private Label progressLabel;
    @FXML
    private DialogPane dialogPane;

    private boolean searchRunning;

    private  SearchProvider searchProvider;

    public SearchToolDialogController() {

    }

    /**
     * Actions for list view
     *
     * @param event
     */
    @FXML
    public void onListViewClicked(MouseEvent event) {
        if (event.getClickCount() == 2) {

        }
    }

    @FXML
    public void onListViewKeyReleased(KeyEvent event) {

        if (event.getCode() == KeyCode.ENTER) {

        }
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
     * Action for search button
     */
    public void onSearchButton() {
        search();
    }

    /**
     *
     */
    private void search() {

        if (searchRunning) {
            new Alert(Alert.AlertType.ERROR, "Search is already running!").showAndWait();
            return;
        }

        setProgressVisible(true);

        Task<Boolean> task = new Task<Boolean>() {
            @Override
            protected Boolean call() throws Exception {
                List<String> list = searchProvider.getSearchResult(searchField.getText());
                listView.getItems().addAll(FXCollections.observableList(list));
                return !list.isEmpty();
            }
        };

        task.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent event) {

                if (task.getValue()) {
                    listView.setVisible(true);
                    listView.setPrefHeight(Control.USE_COMPUTED_SIZE);

                    if (dialogPane.getScene() != null) {
                        // Resize dialog pane
                        dialogPane.getScene().getWindow().sizeToScene();
                    }
                }
                setProgressVisible(false);
            }
        });

        new Thread(task).start();
    }

    /**
     * @param visible
     */
    private void setProgressVisible(boolean visible) {
        progress.setVisible(visible);
        progressLabel.setVisible(visible);
        searchRunning = visible;
    }

    /**
     *
     */
    @FXML
    private void initialize() {

        searchProvider = new SearchProvider();

        dialog.setOnCloseRequest(new EventHandler<DialogEvent>() {
            @Override
            public void handle(DialogEvent event) {
                searchProvider.setTerminate(true);
            }
        });
    }
}
