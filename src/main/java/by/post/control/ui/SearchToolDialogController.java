package by.post.control.ui;

import by.post.control.Context;
import by.post.search.SearchProvider;
import by.post.ui.SimpleProgressIndicator;
import javafx.application.Platform;
import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

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

    private SearchProvider searchProvider;

    private TypedTreeItem tablesTreeItem;
    private TreeView mainTableTree;
    private TableView mainTableView;

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
            selectItem();
        }
    }

    @FXML
    public void onListViewKeyReleased(KeyEvent event) {

        if (event.getCode() == KeyCode.ENTER) {
            selectItem();
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
        setListViewVisible(false);

        Task<Boolean> task = new Task<Boolean>() {
            @Override
            protected Boolean call() throws Exception {
                List<String> tablesNames = searchProvider.getSearchResult(searchField.getText());
                Platform.runLater(() -> {
                    listView.getItems().clear();
                    listView.getItems().addAll(FXCollections.observableList(tablesNames));
                });

                return !tablesNames.isEmpty();
            }
        };

        task.setOnSucceeded(event -> {
            setListViewVisible(task.getValue());
            setProgressVisible(false);
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
     * @param visible
     */
    private void setListViewVisible(boolean visible) {

        listView.setVisible(visible);
        listView.setPrefHeight(visible ? Control.USE_COMPUTED_SIZE : Control.USE_PREF_SIZE);

        if (dialogPane.getScene() != null) {
            // Resize dialog pane
            dialogPane.getScene().getWindow().sizeToScene();
        }
    }

    /**
     *
     */
    @FXML
    private void onCloseRequest() {
        searchProvider.setTerminate(true);
    }

    /**
     *
     */
    @FXML
    private void initialize() {
        //Remove icon from window
        Stage stage = (Stage) dialog.getDialogPane().getScene().getWindow();
        stage.setIconified(false);

        tablesTreeItem = Context.getTablesTreeItem();
        mainTableView = Context.getMainTableView();
        mainTableTree = Context.getMainTableTree();

        searchProvider = new SearchProvider();
    }

    /**
     * Select item in table tree by table name
     */
    private void selectItem() {

        if (tablesTreeItem == null || mainTableTree == null) {
            return;
        }

        Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                String tableName = String.valueOf(listView.getSelectionModel().getSelectedItem());
                ObservableList<TypedTreeItem> typedTreeItems = tablesTreeItem.getChildren();
                //Search first element with equal table name value
                TypedTreeItem item = typedTreeItems.stream().filter(val -> tableName.equals(val.getValue())).findFirst().get();
                mainTableTree.getSelectionModel().select(item);

                return null;
            }
        };

        task.setOnSucceeded(event -> scrollToRow(searchField.getText()));

        Thread thread = new Thread(task);
        thread.setDaemon(true);
        thread.start();
    }

    /**
     * Go to row with text value in main table
     *
     * @param text
     */
    private void scrollToRow(String text) {

        if (mainTableView == null) {
            return;
        }

        ObservableList<ObservableList> rows = mainTableView.getItems();

        for (Observable row : rows) {
            if (row.toString().toUpperCase().contains(text.toUpperCase())) {
                select(rows.indexOf(row));
                return;
            }
        }
    }

    /**
     * @param index
     */
    private void select(int index) {

        Platform.runLater(() -> {
            mainTableView.scrollTo(index);
            mainTableView.layout();
            mainTableView.getSelectionModel().clearSelection();
            mainTableView.getSelectionModel().select(index);
            mainTableView.getFocusModel().focus(index);
        });
    }

}
