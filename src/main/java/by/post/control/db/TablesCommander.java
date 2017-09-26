package by.post.control.db;

import by.post.control.ui.TypedTreeItem;
import by.post.data.Table;
import by.post.data.View;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.TreeView;
import javafx.scene.image.ImageView;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.SQLException;

/**
 * Helper class for add/delete tables/views
 *
 * @author Dmitriy V.Yefremov
 */
public class TablesCommander {

    private DbControl dbControl;
    
    private static TablesCommander INSTANCE;

    private static final Logger logger = LogManager.getLogger(TablesCommander.class);

    public static TablesCommander getInstance() {
        return INSTANCE == null ? INSTANCE = new TablesCommander(): INSTANCE;
    }

    private TablesCommander() {
        dbControl = DbController.getInstance();
    }

    /**
     * Add table in the tree
     *
     * @param tableTree
     */
    public void addTable(TreeView tableTree, Table table, ImageView icon) {

        try {
            dbControl.update(Queries.createTable(table));
            String name = table.getName();
            addTableTreeItem(tableTree, icon, TableType.TABLE, name);
            logger.info("Added new  table: " + name);
        } catch (SQLException e) {
            logger.error("Table editor error[addTable]: " + e);
            new Alert(Alert.AlertType.ERROR, "Failure to add  the table.\nSee more info in console!").showAndWait();
        }
    }

    /**
     * @param tableTree
     * @param view
     * @param icon
     */
    public void addView(TreeView tableTree, View view, ImageView icon) {

        if (view.getTables() == null) {
            new Alert(Alert.AlertType.INFORMATION, "No table is present!").showAndWait();
            return;
        }

        try {
            dbControl.update(Queries.createView(view));
            String name = view.getName();
            addTableTreeItem(tableTree, icon, TableType.VIEW, name);
            logger.info("Added new  view: " + name);
        } catch (SQLException e) {
            logger.error("Table editor error[addTable]: " + e);
            new Alert(Alert.AlertType.ERROR, "Failure to add  the table.\nSee more info in console!").showAndWait();
        }
    }

    /**
     * Add new item into tables tree
     *
     * @param tableTree
     * @param icon
     * @param type
     * @param name
     */
    private void addTableTreeItem(TreeView tableTree, ImageView icon, TableType type, String name) {

        TypedTreeItem treeItem = new TypedTreeItem(name.toUpperCase(), icon, type, false);
        ObservableList<TypedTreeItem> items = tableTree.getRoot().getChildren();

        for (TypedTreeItem item : items) {
            if (item.getType().equals(type)) {
                item.getChildren().add(treeItem);
            }
        }

        tableTree.getSelectionModel().select(treeItem);
        tableTree.scrollTo(tableTree.getSelectionModel().getSelectedIndex());
        tableTree.refresh();
    }

    /**
     * Delete table from the tree
     *
     * @param tableTree
     */
    public void deleteTable(TreeView tableTree) {

        try {
            TypedTreeItem itemToDelete = (TypedTreeItem) tableTree.getSelectionModel().getSelectedItem();
            String name = itemToDelete.getValue().toString();

            TableType type = itemToDelete.getType();

            if (type.equals(TableType.TABLE)) {
                dbControl.update(Queries.deleteTable(name));
            } else if (type.equals(TableType.VIEW)) {
                dbControl.update(Queries.deleteView(name));
            }

            ObservableList<TypedTreeItem> items = tableTree.getRoot().getChildren();

            for (TypedTreeItem item : items) {
                if (item.getType().equals(type)) {
                    item.getChildren().remove(itemToDelete);
                }
            }

            tableTree.refresh();
            logger.info("Deleted table: " + name);
        } catch (Exception e) {
            logger.error("Table editor error[deleteTable]: " + e);
            new Alert(Alert.AlertType.ERROR, "Failure to remove the table.\nSee more info in console!").showAndWait();
        }
    }

}
