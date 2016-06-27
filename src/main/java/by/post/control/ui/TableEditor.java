package by.post.control.ui;

import by.post.control.db.DbControl;
import by.post.control.db.DbController;
import by.post.control.db.Queries;
import by.post.ui.ConfirmationDialog;
import by.post.ui.InputDialog;
import javafx.collections.FXCollections;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableView;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;

import java.util.Collections;
import java.util.Optional;

/**
 * @author Dmitriy V.Yefremov
 */
public class TableEditor {
    private static  DbControl dbControl = null;

    /**
     * Add new row to the table
     *
     * @param table
     */
    public static void addRow(TableView table) {

        int size = table.getColumns().size();
        int selectedIndex = table.getSelectionModel().getSelectedIndex();

        table.getItems().add(++selectedIndex, FXCollections.observableArrayList(Collections.nCopies(size, "New value.")));
        table.getSelectionModel().select(selectedIndex, null);
        table.refresh();
    }

    /**
     * Remove  selected row from the table
     *
     * @param table
     */
    public static void removeRow(TableView table) {

        table.getItems().remove(table.getSelectionModel().getSelectedItem());
    }

    /**
     * Save changes after table editing
     *
     * @param table
     */
    public static void save(TableView table) {

        Optional<ButtonType> result = new ConfirmationDialog().showAndWait();

        if (result.get() == ButtonType.OK){
            System.out.println("ok");
        }
    }

    /**
     * Add table in the tree
     *
     * @param tableTree
     */
    public static void addTable(TreeView tableTree) {

        Optional<String> result = new InputDialog().showAndWait();

        if (result.isPresent()){
            dbControl = DbController.getInstance();
            dbControl.update(Queries.createTable(result.get()));
        }
    }

    /**
     * Delete table from the tree
     *
     * @param tableTree
     */
    public static void deleteTable(TreeView tableTree) {

        Optional<ButtonType> result = new ConfirmationDialog().showAndWait();

        if (result.get() == ButtonType.OK){
            TreeItem item = (TreeItem) tableTree.getSelectionModel().getSelectedItem();
            String table = item.getValue().toString();
            dbControl = DbController.getInstance();
            dbControl.update(Queries.deleteTable(table));
        }
    }


}