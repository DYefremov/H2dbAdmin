package by.post.control.ui;

import by.post.control.Context;
import by.post.control.db.TableDataResolver;
import by.post.control.db.TableEditor;
import by.post.control.db.TableType;
import by.post.data.Row;
import by.post.data.Table;
import by.post.ui.ConfirmationDialog;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.ButtonType;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.util.Duration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author Dmitriy V.Yefremov
 */
public class MainTableController {

    @FXML
    private TableView<Row> mainTable;
    @FXML
    private TextField filterTextField;
    @FXML
    private HBox tableViewToolBar;
    @FXML
    private HBox toolBarButtonsHBox;
    @FXML
    private HBox filterBox;

    private TableEditor tableEditor;
    private ObservableList<Row> data;
    //Used to disable the filter field for empty tables
    private SimpleBooleanProperty tableNotEmpty;
    //Indicate if running filter data process
    private boolean inFiltering;
    //Indicate if table type is selected (for disabling editing in system tables and views)
    private SimpleBooleanProperty isTableType;
    private FilterService filterService;
    //Used to delay before filtering begins
    private PauseTransition filterPause;
    private static final double FILTER_TIMEOUT = 1;

    private static final Logger logger = LogManager.getLogger(MainTableController.class);

    public MainTableController() {

    }

    public TableEditor getTableEditor() {
        return tableEditor;
    }

    /**
     * Actions for selections in the table
     */
    @FXML
    public void onMouseClicked() {

    }

    public void onKeyReleased() {

    }

    /**
     * Actions for work with rows
     */
    @FXML
    public void onAddRow() {
        try {
            tableEditor.addRow();
        } catch (Exception e) {
            logger.error("MainUiController error [onAddRow]: " + e);
        }
    }

    @FXML
    public void onRemoveRow() {

        if (mainTable.getColumns().size() < 1) {
            return;
        }

        Optional<ButtonType> result = new ConfirmationDialog().showAndWait();

        if (result.get() == ButtonType.OK) {
            tableEditor.deleteRow();
        }
    }

    @FXML
    public void onSaveRow() {

        int selectedIndex = mainTable.getSelectionModel().getSelectedIndex();

        if (selectedIndex != -1) {
            Optional<ButtonType> result = new ConfirmationDialog("Save entered data to database?").showAndWait();

            if (result.get() == ButtonType.OK) {
                tableEditor.saveRow();
            }
        }
    }

    @FXML
    public void onFilter(KeyEvent event) {

        if (event.getCode().equals(KeyCode.ENTER)) {
            filterData();
            return;
        }

        filterPause.playFromStart();
    }

    /**
     * @param table
     */
    public void setTable(Table table) {

        logger.info("Select table: " + table.getName());
        inFiltering = false;
        filterTextField.clear();
        mainTable.setId(table.getName());
        clearMainTable();
        setTableType(table.getType());

        TableDataResolver resolver = new TableDataResolver(table, tableEditor);

        if (!resolver.getTableColumns().isEmpty()) {
            mainTable.getColumns().addAll(resolver.getTableColumns());
            ObservableList<Row> items = resolver.getItems();
            data.addAll(items);
            mainTable.setItems(items);
            tableNotEmpty.set(!items.isEmpty());
        }
    }

    /**
     * @param data
     */
    public void setData(Collection<Row> data) {

        mainTable.getItems().clear();
        mainTable.getItems().addAll(data);
        this.data.clear();
        this.data.addAll(data);
        Platform.runLater(() -> mainTable.refresh());
    }

    /**
     * @return size of items in main table
     */
    public int getDataSize() {
        return mainTable.getItems().size();
    }

    /**
     * Clear main table
     */
    public void clearMainTable() {
        mainTable.getColumns().clear();
        mainTable.getItems().clear();
    }

    @FXML
    private void initialize() {

        tableEditor = new TableEditor(mainTable);
        tableNotEmpty = new SimpleBooleanProperty();
        isTableType = new SimpleBooleanProperty();
        data = FXCollections.observableArrayList();
        //disabling editing in system tables and views
        mainTable.editableProperty().bind(isTableType);
        filterBox.visibleProperty().bind(tableNotEmpty);
        //Disable field if load data in progress
        filterTextField.disableProperty().bind(Context.getIsLoadDataProperty());
        filterService = new FilterService();
        filterPause = new PauseTransition(Duration.seconds(FILTER_TIMEOUT));
        filterPause.setOnFinished(event -> filterData());
        //Set multiple selection in table view
        mainTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        //Show buttons only if no system table or view selected
        toolBarButtonsHBox.visibleProperty().bind(isTableType);
    }

    /**
     * @param type
     */
    private void setTableType(TableType type) {
        isTableType.set(type != null && type.equals(TableType.TABLE));
    }

    /**
     * Filter data without default sorting replacement
     */
    private void filterData() {
        filterService.restart();
    }

    /**
     * Service for filtering data
     */
    private class FilterService extends Service<Void> {

        @Override
        protected Task<Void> createTask() {
            return new Task<Void>() {
                @Override
                protected Void call() throws Exception {
                    String searchText = filterTextField.getText().toUpperCase();

                    List<Row> filtered = data.stream()
                            .filter(row -> row.getCells().toString().toUpperCase().contains(searchText))
                            .collect(Collectors.toList());

                    if (!filtered.isEmpty()) {
                        mainTable.setItems(FXCollections.observableArrayList(filtered));
                    }
                    return null;
                }
            };
        }
    }

}
