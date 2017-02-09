package by.post.control.ui;

import by.post.control.Context;
import by.post.data.Row;
import by.post.search.SearchProvider;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
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
    private TextField searchField;
    @FXML
    private ProgressBar progress;
    @FXML
    private Label progressLabel;
    @FXML
    private DialogPane dialogPane;
    @FXML
    private ListView listView;

    private boolean searchRunning;
    private SearchProvider searchProvider;
    private TypedTreeItem tablesTreeItem;
    private TreeView mainTableTree;
    private TableView mainTableView;


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
     * Action for Search button
     */
    @FXML
    public void onSearchButton() {
        search();
    }

    /**
     *ListView actions for found tables
     *
     * @param event
     */
    @FXML
    public void onListMouseClick(MouseEvent event) {

        if (event.getClickCount() == 2) {
            selectItem();
        }
    }

    @FXML
    public void onListKeyReleased(KeyEvent event) {

        if (event.getCode() == KeyCode.ENTER) {
            selectItem();
        }
    }

    @FXML
    public void initialize() {

        tablesTreeItem = Context.getTablesTreeItem();
        mainTableView = Context.getMainTableView();
        mainTableTree = Context.getMainTableTree();

        searchProvider = new SearchProvider();
        dialogPane.setExpandableContent(null);
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
                List<String> tablesNames = searchProvider.getSearchResult(searchText);

                Platform.runLater(() -> {
                    listView.getItems().clear();
                    listView.getItems().addAll(FXCollections.observableList(tablesNames));
                });

                return !tablesNames.isEmpty();
            }
        };

        task.setOnSucceeded(event -> {
            dialogPane.setExpandableContent(listView);
            dialogPane.setExpanded(true);
            setProgressVisible(false);
        });

        Thread thread = new Thread(task);
        thread.setDaemon(true);
        thread.start();
    }

    /**
     * @param visible
     */
    private void setProgressVisible(boolean visible) {

        progress.setVisible(visible);
        progress.setDisable(!visible);
        progressLabel.setVisible(visible);
        searchRunning = visible;
    }

    /**
     * Select item in table tree by table name
     */
    private void selectItem() {

        if (tablesTreeItem == null || mainTableTree == null) {
            return;
        }

        String tableName = String.valueOf(listView.getSelectionModel().getSelectedItem());
        ObservableList<TypedTreeItem> typedTreeItems = tablesTreeItem.getChildren();
        //Search first element with equal table name value
        TypedTreeItem item = typedTreeItems.stream().filter(val -> tableName.equals(val.getValue())).findFirst().get();
        mainTableTree.getSelectionModel().select(item);

        scrollToRow(searchField.getText());
    }

    /**
     * Go to row with text value in main table
     *
     * @param text
     */
    private void scrollToRow(String text) {

        if (mainTableView == null || mainTableView.getItems().isEmpty()) {
            return;
        }

        ObservableList<Row> rows = mainTableView.getItems();

        for (Row row : rows) {
            if (row.toString().toUpperCase().contains(text.toUpperCase())) {
                select(rows.indexOf(row));
                break;
            }
        }
    }

    /**
     * @param index
     */
    private void select(int index) {

        Platform.runLater(() -> {
            mainTableView.layout();
            mainTableView.scrollTo(index);
            mainTableView.getSelectionModel().clearSelection();
            mainTableView.getSelectionModel().select(index);
            mainTableView.getFocusModel().focus(index);
        });
    }

}
