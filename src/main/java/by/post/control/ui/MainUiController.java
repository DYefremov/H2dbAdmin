package by.post.control.ui;

import by.post.control.PropertiesController;
import by.post.control.db.DbControl;
import by.post.control.db.DbController;
import by.post.data.Cell;
import by.post.data.Row;
import by.post.data.Table;
import by.post.ui.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.util.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
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
    //Main table view
    private TableView mainTable;

    private MainUiForm mainUiForm;

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
    public void onItemOpen(ActionEvent event) {

        try {
            File file = new OpenFileDialogProvider().getFileDialog("Open db file.", false);

            if (file != null) {
                String dbName = file.getName();
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
            logger.error("MainUiController error: " + e);
        }

    }

    /**
     * Action for "close" menu item
     */
    @FXML
    public void onItemClose(ActionEvent event) {
        try {
            mainUiForm.getMainStage().close();
        } catch (Exception e) {
            logger.error("MainUiController error: " + e);
        }
    }

    /**
     * Action for "About" menu item
     */
    @FXML
    public void onItemAbout(ActionEvent event) {
        try {
            new AboutDialog().start();
        } catch (Exception e) {
            logger.error("MainUiController error: " + e);
        }
    }

    /**
     * Action for "Tools\Recovery" menu item
     */
    @FXML
    public void onItemRecovery(ActionEvent event) {
        try {
            new RecoveryDialog().start();
        } catch (Exception e) {
            logger.error("MainUiController error: " + e);
        }
    }

    /**
     * init data on startup
     */
    private void init() {
        //TODO переработать метод и DbControl
        List<TreeItem> tables = new ArrayList<>();
        DbControl dbControl = new DbController();

        getDbTablesList(dbControl).stream().forEach(t -> {
            tables.add(new TreeItem(t));
        });
        //Корневой элемент
        TreeItem root = new TreeItem("test");

        ObservableList<TreeItem> list = FXCollections.observableList(tables);

        root.getChildren().addAll(list);
        tableTree.setRoot(root);

        tableTree.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                TreeItem<String> item = (TreeItem<String>) newValue;
                selectTable(dbControl.getTable(item.getValue()));
            }
        });
        //Добавляем корневую таблицу в центр
        mainTable = new TableView<>();
        pane.setCenter(mainTable);
    }

    /**
     * Select and display the selected table
     *
     * @param table
     */
    private void selectTable(Table table) {

        logger.info(table.getName());

        if (!mainTable.getColumns().isEmpty()) {
            mainTable.getColumns().clear();
            mainTable.getItems().clear();
        }

        List<Row> rows = table.getRows();
        List<String> values = new ArrayList<>();

        if (rows != null && !rows.isEmpty()) {
            List<Cell> cells = rows.get(0).getCells();
            List<TableColumn> tableColumns = new ArrayList<>();
            cells.forEach(cell -> {
                tableColumns.add(new TableColumn(cell.getName()));
                values.add((String) cell.getValue());
                System.out.println(cell.toString());
            });

            mainTable.getColumns().addAll(tableColumns);
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

        dbControl.connect(path, db, user, password);
        return dbControl.getTablesList() != null ? dbControl.getTablesList() : new ArrayList<>();
    }

}
