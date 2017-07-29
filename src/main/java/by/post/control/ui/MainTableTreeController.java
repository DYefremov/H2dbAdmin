package by.post.control.ui;

import by.post.control.Context;
import by.post.control.PropertiesController;
import by.post.control.Settings;
import by.post.control.db.DbControl;
import by.post.control.db.DbController;
import by.post.control.db.TableType;
import by.post.control.db.TablesCommander;
import by.post.data.Table;
import by.post.data.View;
import by.post.ui.ConfirmationDialog;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Properties;
import java.util.stream.Collectors;

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
    private TablesCommander tablesCommander;
    private MainUiController mainController;
    private final InitService initService = new InitService();

    public MainTableTreeController() {

    }

    public void setMainController(MainUiController mainController) {
        this.mainController = mainController;
    }

    @FXML
    public void onOpenDb() throws IOException {
        mainController.onOpenDb();
    }

    @FXML
    public void onAddNewTable() throws IOException {
        mainController.onAddNewTable();
    }

    @FXML
    public void onAddNewView() throws IOException {
        mainController.onAddNewView();
    }

    public void addNewTable(Table table) {
        tablesCommander.addTable(tableTree, table, getItemImage("table.png"));
    }

    public void addNewView(View view) {
        tablesCommander.addView(tableTree, view, getItemImage("view.png"));
    }

    @FXML
    public void onTableDelete() {

        Optional<ButtonType> result = new ConfirmationDialog().showAndWait();

        if (result.get() == ButtonType.OK) {
            mainController.clearMainTable();
            tablesCommander.deleteTable(tableTree);
        }
    }

    /**
     * Actions for tree context menu
     */
    @FXML
    public void onContextMenuRequested() {

        boolean visible = tableTree.getRoot() != null && !tableTree.getRoot().isLeaf();
        menuNew.setVisible(visible);
        separatorDelete.setVisible(visible);
        contextMenuItemDelete.setVisible(visible);

        if (!visible) {
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
     * Initialization for first db opening
     */
    public void init() {
        initService.restart();
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

        if (dbControl.isClosed()) {
            tableTree.setRoot(new TypedTreeItem("Database is not present...", null, null, true));
            return;
        }

        TypedTreeItem root = new TypedTreeItem(dbControl.getCurrentDbName(), getItemImage("database.png"), null, true);
        root.getChildren().addAll(FXCollections.observableList(getRootItems()));
        tableTree.setRoot(root);
    }

    @FXML
    private void initialize() {

        dbControl = DbController.getInstance();
        tablesCommander = TablesCommander.getInstance();
        //Initialization of selection property
        tableTree.getSelectionModel().selectedItemProperty().addListener(getChangeListener());
        init();
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
            TypedTreeItem item = new TypedTreeItem(name, image, tType, true);
            //Set tables tree item for using in search tool and view creation dialog
            if (item.getType().equals(TableType.TABLE)) {
                Context.setTablesTreeItem(item);
            }

            item.getChildren().addAll(getDbTablesList(tType.preparedName()).stream()
                    .map(t -> new TypedTreeItem(t, getItemImage(iconName), tType, false))
                    .collect(Collectors.toList()));

            tables.add(item);
        }

        return tables;
    }

    /**
     * @return view with item image
     */
    private ImageView getItemImage(String name) {
        return new ImageView(new Image("/img/" + name, 16, 16, false, false));
    }

    /**
     * @return listener for table tree
     */
    private ChangeListener getChangeListener() {

        return (observable, oldValue, newValue) -> {

            TypedTreeItem item = newValue == null ? null : (TypedTreeItem) newValue;

            if (item == null || item.isRoot()) {
                return;
            }

            mainController.onTableSelect(item);
        };
    }

    /**
     *Service for first db opening
     */
    private class InitService extends Service<Void> {

        @Override
        protected Task<Void> createTask() {
            Task<Void> task = new Task<Void>() {
                @Override
                protected Void call() throws Exception {
                    initDb();
                    return null;
                }
            };

            task.setOnSucceeded(event -> initData());

            return task;
        }
    }

}
