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
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.Collections;
import java.util.Optional;

/**
 * @author Dmitriy V.Yefremov
 */
public class TableEditor {

    private static DbControl dbControl = null;

    private static final Logger logger = LogManager.getLogger(TableEditor.class);

    /**
     * Add new row to the table
     *
     * @param table
     */
    public static void addRow(TableView table) throws IOException {

        int size = table.getColumns().size();

        if (size > 0) {
            int selectedIndex = table.getSelectionModel().getSelectedIndex();

            table.getItems().add(++selectedIndex, FXCollections.observableArrayList(Collections.nCopies(size, "New value.")));
            table.getSelectionModel().select(selectedIndex, null);
        } else {

            Optional<String> result = new InputDialog("\tPlease, specify\n the number of columns!", "1", true).showAndWait();

            if (result.isPresent()) {
//                Integer num = Integer.valueOf(result.get());
//                List<Cell> cells = new ArrayList<>(Collections.nCopies(num, new Cell("new", "", "")));
//                table.getColumns().addAll(new TableDataResolver().getColumns(cells));
//                addRow(table);
            }
        }
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
    public static void save(TableView table, String name) {

        Optional<ButtonType> result = new ConfirmationDialog("Save changes for table: " + name).showAndWait();

        if (result.get() == ButtonType.OK) {
            logger.log(Level.INFO, "Save changes for  table: " + name);
        }
    }

    /**
     * Add table in the tree
     *
     * @param tableTree
     */
    public static void addTable(TreeView tableTree) {

        Optional<String> result = new InputDialog("Please, write table name!", "New table", false).showAndWait();

        if (result.isPresent()) {
            String name = result.get();

            dbControl = DbController.getInstance();
            dbControl.update(Queries.createTable(name));

            tableTree.getRoot().getChildren().add(new TreeItem<>(name));
            tableTree.refresh();

            logger.log(Level.INFO, "Added new  table: " + name);
        }
    }

    /**
     * Delete table from the tree
     *
     * @param tableTree
     */
    public static void deleteTable(TreeView tableTree) {

        Optional<ButtonType> result = new ConfirmationDialog().showAndWait();

        if (result.get() == ButtonType.OK) {
            TreeItem item = (TreeItem) tableTree.getSelectionModel().getSelectedItem();
            String name = item.getValue().toString();

            dbControl = DbController.getInstance();
            dbControl.update(Queries.deleteTable(name));

            tableTree.getRoot().getChildren().remove(item);
            tableTree.refresh();

            logger.log(Level.INFO, "Deleted table: " + name);
        }
    }
}
