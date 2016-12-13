package by.post.control.ui;

import by.post.control.PropertiesController;
import by.post.control.db.*;
import by.post.data.Table;
import by.post.data.View;
import by.post.ui.*;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.*;

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
    private TitledPane infoPane;
    @FXML
    private ContextMenu treeContextMenu;
    @FXML
    private MenuItem contextMenuItemTable;
    @FXML
    private MenuItem contextMenuItemView;

    private DbControl dbControl;

    private MainUiForm mainUiForm;

    private TableEditor tableEditor;

    private TypedTreeItem tablesTreeItem;


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

        Dialog dialog = FXMLLoader.load(MainUiForm.class.getResource("DatabaseDialog.fxml"));

        Optional<Map<String, String>> result = dialog.showAndWait();

        if (result.isPresent()) {
            new Alert(Alert.AlertType.INFORMATION, "Not implemented!").showAndWait();
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
    public void onTableDelete() {

        Optional<ButtonType> result = new ConfirmationDialog().showAndWait();

        if (result.get() == ButtonType.OK) {
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
        mainPane.setCenter(mainSplitPane);
    }

    @FXML
    public void onSqlConsole() throws IOException {
        setCenter("SqlConsole.fxml");
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
        tableEditor.saveCurrentRow();
    }

    public void onKeyReleased() {
        tableEditor.saveCurrentRow();
    }

    /**
     * Actions for work with rows
     */
    @FXML
    public void onAddRow() {

        if (!currentTableName.getText().equals("")) {
            Optional<ButtonType> result = new ConfirmationDialog().showAndWait();

            if (result.get() == ButtonType.OK) {
                try {
                    tableEditor.addRow();
                } catch (Exception e) {
                    logger.error("MainUiController error onAddRowButton: " + e);
                }
            }
        }
    }

    @FXML
    public void onRemoveRow() {

        Optional<ButtonType> result = new ConfirmationDialog().showAndWait();

        if (result.get() == ButtonType.OK) {
            tableEditor.deleteRow();
        }
    }

    @FXML
    public void onSaveRow() {

        int selectedIndex = mainTable.getSelectionModel().getSelectedIndex();

        if (selectedIndex != -1) {
            Optional<ButtonType> result = new ConfirmationDialog("Save current row to database?").showAndWait();

            if (result.get() == ButtonType.OK) {
                tableEditor.saveRow(selectedIndex);
            }
        }
    }

    @FXML
    private void initialize() {
        // Set log messages output to the text area
        LogArea.setArea(console);
        logger.info("Starting application...");
        init();
    }

    /**
     * Init data on startup
     */
    private void init() {

        dbControl = DbController.getInstance();

        if (tableEditor == null) {
            tableEditor = TableEditor.getInstance();
        }

        tableEditor.setTable(mainTable);

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
        tableTree.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {

                if (newValue == null) {
                    return;
                }

                TypedTreeItem item = (TypedTreeItem) newValue;
                // A TreeItem is a leaf if it has no children
                if (!item.isLeaf()) {
                    return;
                }

                selectTable(item);
            }
        });
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
            //Set tables tree item for using in search tool
            if (item.getType().equals(TableType.TABLE)) {
                tablesTreeItem = item;
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
                mainTable.getItems().clear();
                mainTable.getColumns().clear();
                init();
            });
        }
    }

    /**
     * Select table by selected item
     *
     * @param item
     */
    private void selectTable(TypedTreeItem item) {

        showIndicator(true);

        Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                Table table = dbControl.getTable((String) item.getValue(), item.getType());
                Platform.runLater(() -> selectTable(table));
                return null;
            }
        };

        task.setOnFailed(event -> {
            showIndicator(false);
            logger.error("MainUiController error when selecting table: " + task.getException());
        });

        task.setOnSucceeded(event -> showIndicator(false));

        new Thread(task).start();
    }

    /**
     * Select and display the selected table
     *
     * @param table
     */
    private void selectTable(Table table) {

        logger.info("Select table: " + table.getName());

        if (!mainTable.getColumns().isEmpty()) {
            mainTable.getColumns().clear();
            mainTable.getItems().clear();
        }
        // Set text for current table name label by selected tree item.
        TypedTreeItem item = (TypedTreeItem) tableTree.getSelectionModel().getSelectedItem();
        String name = item.getValue() != null ? item.getValue().toString() : "";
        currentTableName.setText(name);
        mainTable.setId(name);

        TableDataResolver resolver = new TableDataResolver(table);

        if (!resolver.getTableColumns().isEmpty()) {
            mainTable.refresh();
            mainTable.getColumns().addAll(resolver.getTableColumns());
            mainTable.setItems(resolver.getItems());
        }
    }

    /**
     * @return list of db tables
     */
    private List<String> getDbTablesList(String type) {

        Properties properties = PropertiesController.getProperties();
        String user = properties.getProperty("user");
        String password = properties.getProperty("password");
        String url = properties.getProperty("url");

        dbControl.connect(url, user, password);
        List tables = dbControl.getTablesList(type);

        return tables != null ? tables : new ArrayList<>();
    }

    /**
     * Add new table
     */
    private void addNewTable() {

        Optional<Table> result =  new TableCreationDialog().showAndWait();

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
     * Set center node in main border pane
     *
     * @param fxml
     */
    private void setCenter(String fxml) throws IOException {
        mainPane.getChildren().remove(mainPane.getCenter());
        Node node = (Node) FXMLLoader.load(MainUiForm.class.getResource(fxml));
        mainPane.setCenter(node);
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
        try {
            FXMLLoader loader = new FXMLLoader(MainUiForm.class.getResource("SearchToolDialog.fxml"));
            Dialog dialog = loader.load();
            Stage stage = (Stage) dialog.getDialogPane().getScene().getWindow();
            stage.getIcons().add(new Image(Resources.LOGO_PATH));
            SearchToolDialogController controller = loader.getController();
            controller.setTablesTreeItem(tablesTreeItem);
            controller.setTableTree(tableTree);
            controller.setMainTableView(mainTable);
            dialog.showAndWait();
        } catch (IOException e) {
            logger.error("MainUiController error showSearchTool: " + e);
        }
    }

    /**
     * Show/hide simple progress indicator
     *
     * @param show
     */
    private void showIndicator(boolean show) {

        Platform.runLater(() -> {
            SimpleProgressIndicator sp = SimpleProgressIndicator.getInstance();

            if (!sp.isShowing() && show) {
                sp.showAndWait();
            } else {
                sp.hide();
            }
        });
    }

}
