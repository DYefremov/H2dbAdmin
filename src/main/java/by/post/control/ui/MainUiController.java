package by.post.control.ui;

import by.post.control.Context;
import by.post.control.PropertiesController;
import by.post.control.Settings;
import by.post.control.db.*;
import by.post.data.Row;
import by.post.data.Table;
import by.post.data.View;
import by.post.data.type.Dbms;
import by.post.ui.*;
import javafx.application.Platform;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.ButtonType;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Controller class for main ui form
 *
 * @author Dmitriy V.Yefremov
 */
public class MainUiController {

    @FXML
    private TextArea console;
    @FXML
    private SplitPane mainSplitPane;
    @FXML
    private SplitPane explorerSplitPane;
    @FXML
    MainTableTreeController mainTableTreeController;
    @FXML
    MainTableController mainTableController;

    private DbControl dbControl;
    private MainUiForm mainUiForm;
    private TableEditor tableEditor;
    private DatabaseManager databaseManager;
    private DataService dataService;

    private static final Logger logger = LogManager.getLogger(MainUiController.class);

    public MainUiController() {
    }

    public void setMainUiForm(MainUiForm mainUiForm) {
        this.mainUiForm = mainUiForm;
    }

    /**
     * Actions for menu and tool bar items
     */
    @FXML
    public void onNewDbAdd() throws IOException {

        Optional<Map<String, String>> result = new DatabaseDialog().showAndWait();

        if (result.isPresent()) {
            new Thread(() -> {
                databaseManager.addDatabase(result.get());
                Platform.runLater(() -> {
                    clearMainTable();
                    mainTableTreeController.init();
                });
            }).start();
        }
    }

    @FXML
    public void onAddNewTable() {
        addNewTable();
    }

    @FXML
    public void onAddNewView() {
        addNewView();
    }

    @FXML
    public void onDbDelete() {
        databaseDelete(false);
    }

    @FXML
    public void onDbDrop() {
        databaseDelete(true);
    }

    public void onTableDelete() {

        Optional<ButtonType> result = new ConfirmationDialog().showAndWait();

        if (result.get() == ButtonType.OK) {
            clearMainTable();
            tableEditor.deleteTable(Context.getMainTableTree());
        }
    }

    @FXML
    public void onOpenDb() {
        openNewDatabase();
    }

    @FXML
    public void onExit() {
        closeProgram();
    }

    @FXML
    public void onAbout() {
        try {
            new AboutDialog(mainUiForm.getHostServices()).showAndWait();
        } catch (Exception e) {
            logger.error("MainUiController error onItemAbout: " + e);
        }
    }

    @FXML
    public void onExplorer() {
        setCenter(explorerSplitPane);
    }

    @FXML
    public void onTrigger() throws IOException {
        setCenter(FXMLLoader.load(MainUiForm.class.getResource("tools/TriggersToolPane.fxml")));
    }

    @FXML
    public void onSqlConsole() throws IOException {
        setCenter(FXMLLoader.load(MainUiForm.class.getResource("tools/SqlConsole.fxml")));
    }

    @FXML
    public void onSearchTool() {
        onExplorer();
        showSearchTool();
    }

    @FXML
    public void onRecoveryTool() {
        new RecoveryToolDialog().showAndWait();
    }

    @FXML
    public void onSettings() throws IOException {
        new SettingsDialog().showAndWait();
    }

    /**
     * @param item
     */
    public void onTableSelect(TypedTreeItem item) {
        mainTableController.setTableType(item.getType() != null && item.getType().equals(TableType.TABLE));
        tableEditor.clearSavedData();
        selectTable(item);
    }

    @FXML
    private void initialize() {

        mainTableTreeController.setMainController(this);
        // Set log messages output to the text area
        LogArea.setArea(console);
        logger.info("Starting application...");
        initDbmsType();
        init();
    }

    /**
     * Init on startup
     */
    private void init() {

        dbControl = DbController.getInstance();
        tableEditor = TableEditor.getInstance();
        databaseManager = DatabaseManager.getInstance();
        dataService = new DataService();
    }

    /**
     * Init DBMS type
     */
    private void initDbmsType() {

        String driver = PropertiesController.getProperties().getProperty(Settings.DRIVER);

        if (driver.equals(Settings.DEFAULT_DRIVER)) {
            Context.setCurrentDbms(Dbms.H2);
        }
    }

    /**
     * @param event
     */
    public void checkIsDataStored(Event event) {

        if (tableEditor.hasNotSavedData()) {
            Optional<ButtonType> result = new ConfirmationDialog("You have unsaved data. Continue?").showAndWait();
            if (result.get() != ButtonType.OK) {
                event.consume();
            }
        }
    }

    /**
     * Open new database
     */
    private void openNewDatabase() {

        Optional<Map<String, String>> result = new OpenDbDialog().showAndWait();

        if (result.isPresent()) {
            PropertiesController.setProperties(result.get());
            clearMainTable();
            mainTableTreeController.init();
        }
    }

    /**
     * Select table by selected item
     *
     * @param item
     */
    private void selectTable(TypedTreeItem item) {

        setBusy(true);
        Context.setLoadData(false);

        if (dataService.isRunning()) {
            dataService.cancel();
        }

        Table table = dbControl.getTable((String) item.getValue(), item.getType());

        Platform.runLater(() -> {
            selectTable(table);
            Context.setLoadData(false);
            setBusy(false);

            if (mainTableController.getDataSize() == TableBuilder.MAX_ROWS) {
                dataService.setItem(item);
                dataService.restart();
            }
        });
    }

    /**
     * Select and display the selected table
     *
     * @param table
     */
    private void selectTable(Table table) {
        mainTableController.setTable(table);
    }

    /**
     * Add new table
     */
    private void addNewTable() {

        Optional<Table> result = new TableCreationDialog().showAndWait();

        if (result.isPresent()) {
            mainTableTreeController.addNewTable(result.get());
        }
    }

    /**
     * Add new view
     */
    private void addNewView() {

        Optional<View> result = new ViewCreationDialog().showAndWait();

        if (result.isPresent()) {
            mainTableTreeController.addNewView(result.get());
        }
    }

    /**
     * Set center node in main split pane
     *
     * @param node
     */
    private void setCenter(Node node) {
        mainSplitPane.getItems().set(0, node);
    }

    /**
     * Close and exit the program.
     */
    private void closeProgram() {
        Stage mainStage = mainUiForm.getMainStage();
        mainStage.fireEvent(new WindowEvent(mainStage, WindowEvent.WINDOW_CLOSE_REQUEST));
    }

    /**
     * Show and work with search tool
     */
    private void showSearchTool() {
        new SearchToolDialog().show();
    }

    /**
     * Show/hide wait cursor and
     * Enable/Disable table tree
     *
     * @param show
     */
    private void setBusy(boolean show) {
       mainTableTreeController.setBusy(show);
    }

    /**
     * Clear main table
     */
    private void clearMainTable() {
        mainTableController.clearMainTable();
    }

    /**
     * Deleting database
     * If not dropOnly the database files will be removed!
     */
    private void databaseDelete(boolean dropOnly) {

        Optional<ButtonType> result = new ConfirmationDialog().showAndWait();

        if (result.get() == ButtonType.OK) {
            new Thread(() -> {
                try {
                    databaseManager.deleteDatabase(dropOnly);
                    Platform.runLater(() -> {
                        clearMainTable();
                        mainTableTreeController.initData();
                    });
                } catch (SQLException e) {
                    logger.error("MainUiController error [databaseDelete]: " + e);
                }
            }).start();
        }
    }

    /**
     * Service for adding data to main table.
     * Used for big tables.
     */
    private class DataService extends Service<Void> {

        private TypedTreeItem item;

        public void setItem(TypedTreeItem item) {
            this.item = item;
        }

        public DataService() {
            setOnSucceeded(event -> setBusy(false));
            setOnFailed(event -> {
                setBusy(false);
                logger.error("DataService error [onFailed]: " + event.getSource().getException());
            });
        }

        @Override
        protected Task<Void> createTask() {
            return new Task<Void>() {
                @Override
                protected Void call() throws Exception {
                    if (item != null) {
                        List<Row> data = (List<Row>) dbControl.getTableData(String.valueOf(item.getValue()), item.getType());
                        mainTableController.addData(data);
                    }
                    return null;
                }
            };
        }
    }

}
