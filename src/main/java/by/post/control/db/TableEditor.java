package by.post.control.db;

import by.post.data.Column;
import by.post.ui.ColumnDialog;
import by.post.ui.ConfirmationDialog;
import by.post.ui.InputDialog;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
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

    private DbControl dbControl = null;
    private TableView mainTable;

    private static TableEditor instance = new TableEditor();

    private static final String DEFAULT_CELL_VALUE = "value";

    private static final Logger logger = LogManager.getLogger(TableEditor.class);

    private TableEditor() {
       dbControl = DbController.getInstance();
    }

    public static TableEditor getInstance() {
        return instance;
    }

    public void setTable(TableView mainTable) {
        this.mainTable = mainTable;
    }

    /**
     * Add new row to the table
     */
    public void addRow() throws IOException {

        int size = mainTable.getColumns().size();

        if (size > 0) {
            int selectedIndex = mainTable.getSelectionModel().getSelectedIndex();
            mainTable.getItems().add(++selectedIndex, FXCollections.observableArrayList(Collections.nCopies(size, DEFAULT_CELL_VALUE)));
            mainTable.getSelectionModel().select(selectedIndex, null);
        } else {
            Optional<Column> result = new ColumnDialog().showAndWait();

            if (result.isPresent()) {
                addColumn(result.get());
            }
        }
        mainTable.refresh();
    }

    /**
     * Remove  selected row from the table
     */
    public void removeRow() {
        mainTable.getItems().remove(mainTable.getSelectionModel().getSelectedItem());
    }

    /**
     * Save changes after table editing
     *
     * @param name
     */
    public void save(String name) {

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
    public void addTable(TreeView tableTree) {

        Optional<String> result = new InputDialog("Please, write table name!", "New table", false).showAndWait();

        if (result.isPresent()) {
            try {
                String name = result.get();
                dbControl.update(Queries.createTable(name));

                TreeItem treeItem = new TreeItem<>(name);
                tableTree.getRoot().getChildren().add(treeItem);
                tableTree.getSelectionModel().select(treeItem);
                tableTree.scrollTo(tableTree.getSelectionModel().getSelectedIndex());
                tableTree.refresh();

                logger.log(Level.INFO, "Added new  table: " + name);
            } catch (Exception e) {
                logger.log(Level.ERROR, "Table editor error: " + e);
                new Alert(Alert.AlertType.ERROR, "Failure to add  the table.\nSee more in console!").showAndWait();
            }
        }
    }

    /**
     * Delete table from the tree
     *
     * @param tableTree
     */
    public void deleteTable(TreeView tableTree) {

        Optional<ButtonType> result = new ConfirmationDialog().showAndWait();

        if (result.get() == ButtonType.OK) {
            try {
                TreeItem item = (TreeItem) tableTree.getSelectionModel().getSelectedItem();
                String name = item.getValue().toString();

                dbControl.update(Queries.deleteTable(name));

                tableTree.getRoot().getChildren().remove(item);
                tableTree.refresh();

                logger.log(Level.INFO, "Deleted table: " + name);
            } catch (Exception e) {
                logger.log(Level.ERROR, "Table editor error: " + e);
                new Alert(Alert.AlertType.ERROR, "Failure to remove the table.\nSee more in console!").showAndWait();
            }
        }
    }

    /**
     * Change column properties in the table
     *
     * @param column
     */
    public void changeColumnProperties(Column column) {

        logger.log(Level.INFO, "Modified column in the table.");
    }

    /**
     * Delete column from the table
     *
     * @param column
     */
    public void deleteColumn(TableColumn column) {

        Optional<ButtonType> result = new ConfirmationDialog().showAndWait();

        if (result.get() == ButtonType.OK) {
            try {
                dbControl.update(Queries.deleteColumn((Column) column.getUserData()));

                int index = mainTable.getColumns().indexOf(column);

                if (index == -1) {
                    return;
                }

                mainTable.getColumns().remove(index);
                ObservableList<ObservableList> items = mainTable.getItems();

                if (items != null && !items.isEmpty()) {
                    // Remove cell from rows by index.
                    items.parallelStream().forEach(item -> item.remove(index));
                }

                logger.log(Level.INFO, "Column deleted.");
            } catch (Exception e) {
                logger.log(Level.ERROR, "Table editor error: " + e);
                new Alert(Alert.AlertType.ERROR, "Failure to remove the column.\nSee more in console!").showAndWait();
            }
        }
    }

    /**
     * Add new column in the table
     */
    public void addColumn(Column column) {
        createNewColumn(column);
        logger.info("Add column in table: " + column.getColumnName());
    }

    /**
     * Create new column
     *
     * @param column
     */
    private void createNewColumn(Column column){

        try {
            column.setTableName(mainTable.getId());

            dbControl.update(Queries.addColumn(column));

            TableColumn tableColumn = new TableDataResolver().getColumn(column, false);
            mainTable.getColumns().add(tableColumn);

            ObservableList<ObservableList> items = mainTable.getItems();
            if (items != null && !items.isEmpty()) {
                // Fill in the data.
                items.parallelStream().forEach(item -> item.add(""));
                mainTable.setItems(items);
            }
        } catch (Exception e) {
            logger.log(Level.ERROR, "Table editor error: " + e);
            new Alert(Alert.AlertType.ERROR, "Failure to create the column.\nSee more in console!").showAndWait();
        }
    }

}
