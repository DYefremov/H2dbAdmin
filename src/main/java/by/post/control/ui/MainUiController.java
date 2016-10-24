package by.post.control.ui;

import by.post.control.PropertiesController;
import by.post.control.db.DbControl;
import by.post.control.db.DbController;
import by.post.control.db.TableDataResolver;
import by.post.control.db.TableEditor;
import by.post.data.Table;
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
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Properties;

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

    private String dbName;

    private MainUiForm mainUiForm;

    private TableEditor tableEditor;

    private static final Logger logger = LogManager.getLogger(MainUiController.class);

    public MainUiController() {
    }

    public void setMainUiForm(MainUiForm mainUiForm) {
        this.mainUiForm = mainUiForm;
    }

    /**
     * Add action at the start
     */
    @FXML
    private void initialize() {
        // Set log messages output to the text area
        LogArea.setArea(console);
        logger.info("Starting application...");
        init();
    }

    /**
     * Action for "File/Open" menu item
     */
    @FXML
    public void onItemOpen() {
        try {
            File file = new OpenFileDialogProvider().getFileDialog("Open db file.", false);

            if (file != null) {
                dbName = file.getName();
                dbName = dbName.substring(0, dbName.indexOf("."));

                String path = file.getParent() + File.separator;
                Optional<Pair<String, String>> data = new LoginDialog().showAndWait();
                String user = data.get().getKey();
                String password = data.get().getValue();

                logger.info("Entered user data : user = " + user + ", password = " + password);

                PropertiesController.setProperties(path, dbName, user, password);

                Task<Void> task = new Task<Void>() {
                    @Override
                    protected Void call() throws Exception {
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                init();
                            }
                        });
                        return null;
                    }
                };

                new Thread(task).start();
            }
        } catch (Exception e) {
            logger.error("MainUiController error onItemOpen: " + e);
        }

    }

    /**
     * Action for "File/Settings" menu item
     */
    public void onItemSettings() throws IOException {
        setCenter("SettingsPane.fxml");
    }

    /**
     * Action for "File/Close" menu item
     */
    @FXML
    public void onItemExit() {
        closeProgram();
    }

    /**
     * Action for "About" menu item
     */
    @FXML
    public void onItemAbout() {
        try {
            new AboutDialog(mainUiForm.getHostServices()).showAndWait();
        } catch (Exception e) {
            logger.error("MainUiController error onItemAbout: " + e);
        }
    }

    /**
     * Action for "Tools\Recovery" menu item
     */
    @FXML
    public void onItemRecovery() {
        try {
            mainPane.setCenter(FXMLLoader.load(MainUiForm.class.getResource("RecoveryPane.fxml")));
        } catch (Exception e) {
            logger.error("MainUiController error onItemRecovery: " + e);
        }
    }

    /**
     * Action for "Tools\SQL console" menu item
     */
    @FXML
    public void onItemSqlConsole() throws IOException {
        if (mainPane.getChildren().contains(mainSplitPane)) {
            setCenter("SqlConsole.fxml");
        } else {
            mainPane.setCenter(mainSplitPane);
        }
    }

    /**
     * Actions for menu bar
     */
    @FXML
    public void onBarExplorer() {
        mainPane.getChildren().remove(mainPane.getCenter());
        mainPane.setCenter(mainSplitPane);
    }

    @FXML
    public void onBarConsole() throws IOException {
        setCenter("SqlConsole.fxml");
    }

    @FXML
    public void onBarSettings() throws IOException {
        setCenter("SettingsPane.fxml");
    }

    @FXML
    public void onBarExit() {
        closeProgram();
    }

    /**
     * Actions for table context menu
     */
    @FXML
    public void onTableItemAdd() {

        Optional<ButtonType> result = new ConfirmationDialog().showAndWait();

        if (result.get() == ButtonType.OK) {
            try {
                tableEditor.addRow();
            } catch (Exception e) {
                logger.error("MainUiController error onTableItemAdd: " + e);
            }
        }
    }

    @FXML
    public void onTableItemDelete() {

        Optional<ButtonType> result = new ConfirmationDialog().showAndWait();

        if (result.get() == ButtonType.OK) {
            tableEditor.deleteRow();
        }
    }

    /**
     * Actions for tool bar buttons
     */
    @FXML
    public void onAddButton() {

        if (!currentTableName.getText().equals("")) {
            Optional<ButtonType> result = new ConfirmationDialog().showAndWait();

            if (result.get() == ButtonType.OK) {
                try {
                    tableEditor.addRow();
                } catch (Exception e) {
                    logger.error("MainUiController error onAddButton: " + e);
                }
            }
        }
    }

    @FXML
    public void onRemoveButton() {

        Optional<ButtonType> result = new ConfirmationDialog().showAndWait();

        if (result.get() == ButtonType.OK) {
            tableEditor.deleteRow();
        }
    }

    @FXML
    public void onSaveButton() {

        int selectedIndex = mainTable.getSelectionModel().getSelectedIndex();

        if (selectedIndex != -1) {
            Optional<ButtonType> result = new ConfirmationDialog("Save current row to database?").showAndWait();

            if (result.get() == ButtonType.OK) {
                tableEditor.saveRow(selectedIndex);
            }
        }
    }

    /**
     * Actions for tree context menu
     */
    @FXML
    public void onTreeContextAdd() {
        Optional<String> result = new InputDialog("Please, write table name!", "New table", false).showAndWait();

        if (result.isPresent()) {
            tableEditor.addTable(tableTree, result.get());
        }
    }

    @FXML
    public void onTreeContextDelete() {

        Optional<ButtonType> result = new ConfirmationDialog().showAndWait();

        if (result.get() == ButtonType.OK) {
            tableEditor.deleteTable(tableTree);
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
     * Init data on startup
     */
    private void init() {

        List<TreeItem> tables = new ArrayList<>();
        DbControl dbControl = DbController.getInstance();

        if (tableEditor == null) {
            tableEditor = TableEditor.getInstance();
        }

        tableEditor.setTable(mainTable);

        getDbTablesList(dbControl).stream().forEach(t -> {
            tables.add(new TreeItem(t));
        });

        if (tables.isEmpty()) {
            tableTree.setRoot(new TreeItem("Database is not present..."));
            tableTree.setContextMenu(null);
            return;
        }

        tableTree.setContextMenu(treeContextMenu);
        // Sorting
//        tables.sort(Comparator.comparing(t -> t.getValue().toString()));
        ObservableList<TreeItem> list = FXCollections.observableList(tables);
        TreeItem root = new TreeItem(dbName);
        root.getChildren().addAll(list);
        tableTree.setRoot(root);

        tableTree.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {

                if (newValue == null) {
                    return;
                }

                TreeItem<String> item = (TreeItem<String>) newValue;
                // A TreeItem is a leaf if it has no children
                if (item.isLeaf()) {

                    Task<Boolean> task = new Task<Boolean>() {
                        @Override
                        protected Boolean call() throws Exception {

                            Table table = dbControl.getTable(item.getValue());

                            Platform.runLater(new Runnable() {
                                @Override
                                public void run() {
                                    selectTable(table);
                                }
                            });
                            return true;
                        }
                    };

                    task.setOnFailed(event -> {
                        logger.error("MainUiController error when selecting table: " + task.getException());
                    });

                    new Thread(task).start();
                }
            }
        });
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
        TreeItem item = (TreeItem) tableTree.getSelectionModel().getSelectedItem();
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
    private List<String> getDbTablesList(DbControl dbControl) {

        Properties properties = PropertiesController.getProperties();
        String user = properties.getProperty("user");
        String password = properties.getProperty("password");
        String path = properties.getProperty("path");
        String db = properties.getProperty("db");
        dbName = db;

        dbControl.connect(path, db, user, password);

        return dbControl.getTablesList() != null ? dbControl.getTablesList() : new ArrayList<>();
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
        Stage mainStage =  mainUiForm.getMainStage();
        mainStage.fireEvent(new WindowEvent(mainStage, WindowEvent.WINDOW_CLOSE_REQUEST));
    }
}
