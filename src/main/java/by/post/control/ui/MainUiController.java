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
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Duration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Controller class for main ui form
 *
 * @author Dmitriy V.Yefremov
 */
public class MainUiController {

    @FXML
    private TreeView tableTree;
    @FXML
    private BorderPane mainPane;
    @FXML
    private TextArea console;
    @FXML
    private TableView mainTable;
    @FXML
    private Label currentTableName;
    @FXML
    private SplitPane mainSplitPane;
    @FXML
    private SplitPane explorerSplitPane;
    @FXML
    private TitledPane infoPane;
    @FXML
    private ContextMenu treeContextMenu;
    @FXML
    private MenuItem contextMenuItemTable;
    @FXML
    private MenuItem contextMenuItemView;
    @FXML
    private TextField filterTextField;
    @FXML
    private HBox tableViewToolBar;
    @FXML
    private HBox toolBarButtonsHBox;

    private DbControl dbControl;
    private MainUiForm mainUiForm;
    private TableEditor tableEditor;
    private DatabaseManager databaseManager;
    private SimpleProgressIndicator progressIndicator;
    //Indicate if running filter data process
    private boolean inFiltering;
    //Indicate if table type is selected (for disabling editing in system tables and views)
    private SimpleBooleanProperty isTableType;
    //Used to delay before filtering begins
    private PauseTransition filterPause;
    private static final double FILTER_TIMEOUT = 1;

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
            databaseManager.addDatabase(result.get());
            Platform.runLater(() -> {
                clearMainTable();
                initData();
            });
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

    @FXML
    public void onTableDelete() {

        Optional<ButtonType> result = new ConfirmationDialog().showAndWait();

        if (result.get() == ButtonType.OK) {
            clearMainTable();
            tableEditor.deleteTable(tableTree);
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
        setCenter(FXMLLoader.load(MainUiForm.class.getResource("TriggersToolPane.fxml")));
    }

    @FXML
    public void onSqlConsole() throws IOException {
        setCenter(FXMLLoader.load(MainUiForm.class.getResource("SqlConsole.fxml")));
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
     * Actions for tree context menu
     */
    @FXML
    public void onContextMenuRequested() {

        TypedTreeItem treeItem = (TypedTreeItem) tableTree.getSelectionModel().getSelectedItem();

        if (treeItem == null) {
            treeContextMenu.hide();
            return;
        }

        TableType type = treeItem.getType();

        contextMenuItemView.setVisible(type == null || type.equals(TableType.VIEW));
        contextMenuItemTable.setVisible(type == null || type.equals(TableType.TABLE));

        if (type != null && type.equals(TableType.SYSTEM_TABLE)) {
            treeContextMenu.hide();
        }
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
    public void onFilter() {
        filterPause.playFromStart();
    }

    @FXML
    public void initialize() {
        // Set log messages output to the text area
        LogArea.setArea(console);
        logger.info("Starting application...");
        initDbmsType();
        init();
        initData();
        //Set context
        Context.setMainTableTree(tableTree);
        Context.setMainTableView(mainTable);
    }

    /**
     * Init on startup
     */
    private void init() {

        dbControl = DbController.getInstance();
        tableEditor = TableEditor.getInstance();
        databaseManager = DatabaseManager.getInstance();
        progressIndicator = SimpleProgressIndicator.getInstance();
        tableEditor.setTable(mainTable);
        isTableType = new SimpleBooleanProperty();
        //disabling editing in system tables and views
        mainTable.editableProperty().bind(isTableType);
        //Disable field if load data in progress
        filterTextField.disableProperty().bind(Context.getIsLoadDataProperty());
        filterPause = new PauseTransition(Duration.seconds(FILTER_TIMEOUT));
        filterPause.setOnFinished(event -> filterData());

        tableTree.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {

            if (newValue == null) {
                return;
            }

            TypedTreeItem item = (TypedTreeItem) newValue;
            // A TreeItem is a leaf if it has no children
            if (!item.isLeaf()) {
                return;
            }

            isTableType.set(item.getType() != null && item.getType().equals(TableType.TABLE));
            tableEditor.clearSavedData();
            selectTable(item);
        });

        tableTree.addEventFilter(MouseEvent.MOUSE_PRESSED, event -> checkIsDataStored(event));
        tableTree.addEventFilter(KeyEvent.KEY_PRESSED, event -> checkIsDataStored(event));
        //Set multiple selection in table view
        mainTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        //Show toolbar only if any table selected
        tableViewToolBar.visibleProperty().bind(currentTableName.textProperty().isNotEmpty());
        //Show buttons only if no system table or view selected
        toolBarButtonsHBox.visibleProperty().bind(isTableType);
    }

    /**
     * Init data
     */
    private void initData() {

        List<TypedTreeItem> tables = getRootItems();

        if (tables.isEmpty()) {
            tableTree.setRoot(new TreeItem("Database is not present..."));
            tableTree.setContextMenu(null);
            return;
        }
        // Sorting
//        tables.sort(Comparator.comparing(t -> t.getValue().toString()));
        ObservableList<TypedTreeItem> list = FXCollections.observableList(tables);

        TypedTreeItem root = new TypedTreeItem(dbControl.getCurrentDbName(), getItemImage("database.png"), null);
        root.getChildren().addAll(list);

        tableTree.setRoot(root);
        tableTree.setContextMenu(treeContextMenu);
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
    private void checkIsDataStored(Event event) {
        if (tableEditor.hasNotSavedData()) {
            Optional<ButtonType> result = new ConfirmationDialog("You have unsaved data. Continue?").showAndWait();
            if (result.get() != ButtonType.OK) {
                event.consume();
            }
        }
    }

    /**
     * Get tables items for root element
     *
     * @return items list
     */
    private List<TypedTreeItem> getRootItems() {

        List<TypedTreeItem> tables = new ArrayList<>();

        for (TableType tType : TableType.values()) {
            String iconName = tType.name().toLowerCase() + ".png";
            boolean isSchema = tType.equals(TableType.SYSTEM_TABLE);
            String name = isSchema ? "INFORMATION_SCHEMA" : tType.preparedName() + "S";
            ImageView image = getItemImage(isSchema ? "info.png" : iconName);

            TypedTreeItem item = new TypedTreeItem(name, image, tType);
            tables.add(item);
            //Set tables tree item for using in search tool and view creation dialog
            if (item.getType().equals(TableType.TABLE)) {
                Context.setTablesTreeItem(item);
            }
            List<TypedTreeItem> items = new ArrayList<>();

            getDbTablesList(tType.preparedName()).stream().forEach(t -> {
                items.add(new TypedTreeItem(t, getItemImage(iconName), tType));
            });

            item.getChildren().addAll(items);
        }

        return tables;
    }

    /**
     * Open new database
     */
    private void openNewDatabase() {

        Optional<Map<String, String>> result = new OpenDbDialog().showAndWait();

        if (result.isPresent()) {
            PropertiesController.setProperties(result.get());

            Platform.runLater(() -> {
                clearMainTable();
                initData();
            });
        }
    }

    /**
     * Select table by selected item
     *
     * @param item
     */
    private void selectTable(TypedTreeItem item) {

        Context.setLoadData(false);
        showIndicator(true);
        final String tableName = (String) item.getValue();
        TableType type = item.getType();


        Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                Table table = dbControl.getTable(tableName, type);
                Platform.runLater(() -> selectTable(table));
                return null;
            }
        };

        task.setOnFailed(event -> {
            showIndicator(false);
            logger.error("MainUiController error when selecting table: " + task.getException());
        });

        task.setOnSucceeded(event -> {
            showIndicator(false);
            if (mainTable.getItems().size() == TableBuilder.MAX_ROWS) {
                new Thread(() -> {
                    Context.setLoadData(true);
                    List<Row> data = (List<Row>) dbControl.getTableData(tableName, type);
                    if (Context.isLoadData()) {
                        Platform.runLater(() -> mainTable.getItems().addAll(data));
                    }
                    Context.setLoadData(false);
                }).start();
            }
        });

        Thread thread = new Thread(task);
        thread.setDaemon(true);
        thread.start();
    }

    /**
     * Select and display the selected table
     *
     * @param table
     */
    private void selectTable(Table table) {

        logger.info("Select table: " + table.getName());
        inFiltering = false;
        filterTextField.clear();
        clearMainTable();
        // Set text for current table name label by selected tree item.
        TypedTreeItem item = (TypedTreeItem) tableTree.getSelectionModel().getSelectedItem();
        String name = item.getValue() != null ? item.getValue().toString() : "";
        currentTableName.setText(name);
        mainTable.setId(name);

        TableDataResolver resolver = new TableDataResolver(table);

        if (!resolver.getTableColumns().isEmpty()) {
            mainTable.refresh();
            mainTable.getColumns().addAll(resolver.getTableColumns());
            ObservableList<Row> items = resolver.getItems();
            Context.setCurrentData(items);
            mainTable.setItems(items);
        }
    }

    /**
     * @return list of db tables
     */
    private List<String> getDbTablesList(String type) {

        Properties properties = PropertiesController.getProperties();
        String user = properties.getProperty(Settings.USER);
        String password = properties.getProperty(Settings.PASSWORD);
        String url = properties.getProperty(Settings.URL);

        dbControl.connect(url, user, password);
        List tables = dbControl.getTablesList(type);

        return tables != null ? tables : new ArrayList<>();
    }

    /**
     * Add new table
     */
    private void addNewTable() {

        Optional<Table> result = new TableCreationDialog().showAndWait();

        if (result.isPresent()) {
            tableEditor.addTable(tableTree, result.get(), getItemImage("table.png"), TableType.TABLE);
        }
    }

    /**
     * Add new view
     */
    private void addNewView() {

        Optional<View> result = new ViewCreationDialog().showAndWait();

        if (result.isPresent()) {
            tableEditor.addView(tableTree, result.get(), getItemImage("view.png"), TableType.VIEW);
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
     * @return view with item image
     */
    private ImageView getItemImage(String name) {
        return new ImageView(new Image("/img/" + name, 16, 16, false, false));
    }

    /**
     * Show and work with search tool
     */
    private void showSearchTool() {
        new SearchToolDialog().showAndWait();
    }

    /**
     * Filter data without default sorting replacement
     */
    private void filterData() {

        ObservableList<Row> data = Context.getCurrentData();

        if (inFiltering || data == null || data.isEmpty()) {
            return;
        }

        inFiltering = true;

        String searchText = filterTextField.getText();

        Platform.runLater(() -> {
            List<Row> filtered = data.stream()
                    .filter(row -> row.toString().toUpperCase().contains(searchText.toUpperCase()))
                    .collect(Collectors.toList());

            if (!filtered.isEmpty()) {
                mainTable.setItems(FXCollections.observableArrayList(filtered));
            }

            inFiltering = false;
        });
    }

    /**
     * Show/hide simple progress indicator
     *
     * @param show
     */
    private void showIndicator(boolean show) {

        Platform.runLater(() -> {
            if (!progressIndicator.isShowing() && show) {
                progressIndicator.showAndWait();
            } else {
                progressIndicator.hide();
            }
        });
    }

    /**
     * Clear main table
     */
    private void clearMainTable() {
        currentTableName.setText("");
        mainTable.getColumns().clear();
        mainTable.getItems().clear();
    }

    /**
     * Deleting database
     * If not dropOnly the database files will be removed!
     */
    private void databaseDelete(boolean dropOnly) {

        Optional<ButtonType> result = new ConfirmationDialog().showAndWait();

        if (result.get() == ButtonType.OK) {
            try {
                databaseManager.deleteDatabase(dropOnly);

                Platform.runLater(() -> {
                    clearMainTable();
                    tableTree.setRoot(null);

                    if (dropOnly) {
                        initData();
                    }
                });
            } catch (SQLException e) {
                logger.error("MainUiController error [databaseDelete]: " + e);
            }
        }
    }

}
