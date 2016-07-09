package by.post.control.ui;

import by.post.control.PropertiesController;
import by.post.control.db.DbControl;
import by.post.control.db.DbController;
import by.post.data.Table;
import by.post.ui.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
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
    private BorderPane pane;
    @FXML
    private TextArea console;
    @FXML
    private TableView mainTable;
    @FXML
    private Label currentTableName;
    @FXML
    private SplitPane mainSplitPane;

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
                init();
            }

        } catch (Exception e) {
            logger.error("MainUiController error onItemOpen: " + e);
        }

    }

    /**
     * Action for "close" menu item
     */
    @FXML
    public void onItemExit() {
        try {
            mainUiForm.getMainStage().close();
        } catch (Exception e) {
            logger.error("MainUiController error onItemClose: " + e);
        }
    }

    /**
     * Action for "About" menu item
     */
    @FXML
    public void onItemAbout() {
        try {
            new AboutDialog().showAndWait();
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
            new RecoveryDialog().showAndWait();
        } catch (Exception e) {
            logger.error("MainUiController error onItemRecovery: " + e);
        }
    }

    /**
     * Action for "Tools\SQL console" menu item
     */
    @FXML
    public void onItemSqlConsole() throws IOException {
        if (pane.getChildren().contains(mainSplitPane)) {
           setCenter("SqlConsole.fxml");
        } else {
            pane.getChildren().remove(pane.getCenter());
            pane.setCenter(mainSplitPane);
        }
    }

    /**
     * Actions for menu bar
     */
    @FXML
    public void onBarExplorer() {
        pane.getChildren().remove(pane.getCenter());
        pane.setCenter(mainSplitPane);

    }

    @FXML
    public void onBarConsole() throws IOException {
        setCenter("SqlConsole.fxml");
    }

    @FXML
    public void onBarSettings() throws IOException {
        setCenter("Settings.fxml");
    }

    @FXML
    public void onBarExit() {
        mainUiForm.getMainStage().close();
    }

    /**
     * Actions for table context menu
     */
    @FXML
    public void onTableItemAdd() {
        try {
            tableEditor.addRow();
        } catch (IOException e) {
            logger.error("MainUiController error onTableItemAdd: " + e);
        }
    }

    @FXML
    public void onTableItemDelete() {
        tableEditor.removeRow();
    }

    /**
     * Actions for tool bar buttons
     */
    @FXML
    public void onAddButton() {
        if (!currentTableName.getText().equals("")) {
            try {
                tableEditor.addRow();
            } catch (IOException e) {
                logger.error("MainUiController error onAddButton: " + e);
            }
        }
    }

    @FXML
    public void onRemoveButton() {
        tableEditor.removeRow();
    }

    @FXML
    public void onSaveButton() {
        if (!currentTableName.getText().equals("")) {
            tableEditor.save(currentTableName.getText());
        }
    }

    /**
     * Actions for tree context menu
     */
    @FXML
    public void onTreeContextAdd() {
        tableEditor.addTable(tableTree);
    }

    @FXML
    public void onTreeContextDelete() {
        tableEditor.deleteTable(tableTree);
    }

    /**
     * init data on startup
     */
    private void init() {
        //TODO переработать метод и DbControl
        List<TreeItem> tables = new ArrayList<>();
        DbControl dbControl = DbController.getInstance();
        tableEditor = TableEditor.getInstance();
        tableEditor.setTable(mainTable);

        getDbTablesList(dbControl).stream().forEach(t -> {
            tables.add(new TreeItem(t));
        });

        TreeItem root = new TreeItem(dbName);
        ObservableList<TreeItem> list = FXCollections.observableList(tables);
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
                    Table table = dbControl.getTable(item.getValue());
                    selectTable(table);
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
        pane.getChildren().remove(pane.getCenter());
        Node node = (Node) FXMLLoader.load(MainUiForm.class.getResource(fxml));
        pane.setCenter(node);
    }
}
