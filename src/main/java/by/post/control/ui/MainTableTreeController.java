package by.post.control.ui;

import by.post.control.Context;
import by.post.control.PropertiesController;
import by.post.control.Settings;
import by.post.control.db.DbControl;
import by.post.control.db.DbController;
import by.post.control.db.TableEditor;
import by.post.control.db.TableType;
import by.post.data.Table;
import by.post.data.View;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * @author Dmitriy V.Yefremov
 */
public class MainTableTreeController {

    @FXML
    private TreeView tableTree;
    @FXML
    private ContextMenu treeContextMenu;
    @FXML
    private MenuItem contextMenuItemTable;
    @FXML
    private MenuItem contextMenuItemView;
    @FXML
    private MenuItem contextMenuItemDelete;
    @FXML
    private SeparatorMenuItem separatorDelete;
    @FXML
    private Menu menuNew;

    private DbControl dbControl;
    private TableEditor tableEditor;
    private MainUiController mainController;

    public MainTableTreeController() {

    }

    public void setMainController(MainUiController mainController) {
        this.mainController = mainController;
    }

    @FXML
    public void onOpenDb() {
        mainController.onOpenDb();
    }

    @FXML
    public void onAddNewTable() {
        mainController.onAddNewTable();
    }

    @FXML
    public void onAddNewView() {
        mainController.onAddNewView();
    }

    public void addNewTable(Table table) {
        tableEditor.addTable(tableTree, table, getItemImage("table.png"));
    }

    public void addNewView(View view) {
        tableEditor.addView(tableTree, view, getItemImage("view.png"));
    }

    @FXML
    public void onTableDelete() {
        mainController.onTableDelete();
    }

    /**
     * Actions for tree context menu
     */
    @FXML
    public void onContextMenuRequested() {

        if (tableTree.getRoot().isLeaf()) {
            menuNew.setVisible(false);
            separatorDelete.setVisible(false);
            contextMenuItemDelete.setVisible(false);
            return;
        }

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
     * @param show
     */
    public void setBusy(boolean show) {

        Platform.runLater(() -> {
            tableTree.setDisable(show);
            tableTree.setCursor(show ? Cursor.WAIT : Cursor.DEFAULT);
        });
    }

    /**
     * Init settings and database connection
     */
    public void initDb() {

        Properties properties = PropertiesController.getProperties();
        String user = properties.getProperty(Settings.USER);
        String password = properties.getProperty(Settings.PASSWORD);
        String url = properties.getProperty(Settings.URL);

        dbControl.connect(url, user, password);
    }

    /**
     * Init data
     */
    public void initData() {

        if (dbControl.getCurrentConnection() == null) {
            tableTree.setRoot(new TreeItem("Database is not present..."));
            return;
        }

        initSelectionModel();
        List<TypedTreeItem> tables = getRootItems();
        // Sorting
//        tables.sort(Comparator.comparing(t -> t.getValue().toString()));
        ObservableList<TypedTreeItem> list = FXCollections.observableList(tables);

        TypedTreeItem root = new TypedTreeItem(dbControl.getCurrentDbName(), getItemImage("database.png"), null);
        root.getChildren().addAll(list);

        tableTree.setRoot(root);
        tableTree.setContextMenu(treeContextMenu);
    }

    @FXML
    private void initialize() {

        dbControl = DbController.getInstance();
        tableEditor = TableEditor.getInstance();
        initDb();
        initData();
        //Set context
        Context.setMainTableTree(tableTree);
    }

    /**
     * Initialization of selection properties
     */
    private void initSelectionModel() {

        tableTree.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {

            if (newValue == null) {
                return;
            }

            TypedTreeItem item = (TypedTreeItem) newValue;
            // A TreeItem is a leaf if it has no children
            if (!item.isLeaf()) {
                return;
            }

            mainController.onTableSelect(item);
        });

        tableTree.addEventFilter(MouseEvent.MOUSE_PRESSED, event -> mainController.checkIsDataStored(event));
        tableTree.addEventFilter(KeyEvent.KEY_PRESSED, event -> mainController.checkIsDataStored(event));
    }

    /**
     * @return list of db tables
     */
    private List<String> getDbTablesList(String type) {

        List<String> tables = dbControl.getTablesList(type);

        return tables != null ? tables : new ArrayList<>();
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
     * @return view with item image
     */
    private ImageView getItemImage(String name) {
        return new ImageView(new Image("/img/" + name, 16, 16, false, false));
    }

}
