package by.post.control.db;

import by.post.data.Column;
import by.post.data.ColumnDataType;
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

    private static final String DEFAULT_CELL_VALUE = "New value";
    private static final String DEFAULT_COLUMN_NAME = "New column";
    private static final String DEFAULT_COLUMN_TYPE = ColumnDataType.VARCHAR.name();

    private static final Logger logger = LogManager.getLogger(TableEditor.class);

    private TableEditor() {
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
            Optional<String> result = new InputDialog("\tPlease, specify\n the number of columns!", "1", true).showAndWait();

            if (result.isPresent()) {
                Integer num = Integer.valueOf(result.get());

                for (int i = 0; i < num; i++) {
                   createNewColumn(new Column(DEFAULT_COLUMN_NAME, DEFAULT_COLUMN_TYPE));
                }
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
            String name = result.get();

            dbControl = DbController.getInstance();
            dbControl.update(Queries.createTable(name));

            TreeItem treeItem = new TreeItem<>(name);
            tableTree.getRoot().getChildren().add(treeItem);
            tableTree.getSelectionModel().select(treeItem);
            tableTree.scrollTo(tableTree.getSelectionModel().getSelectedIndex());
            tableTree.refresh();

            logger.log(Level.INFO, "Added new  table: " + name);
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
            TreeItem item = (TreeItem) tableTree.getSelectionModel().getSelectedItem();
            String name = item.getValue().toString();

            dbControl = DbController.getInstance();
            dbControl.update(Queries.deleteTable(name));

            tableTree.getRoot().getChildren().remove(item);
            tableTree.refresh();

            logger.log(Level.INFO, "Deleted table: " + name);
        }
    }

    /**
     * Change column properties in the table
     *
     * @param column
     */
    public void changeColumnProperties(TableColumn column) {
        Column col = (Column) column.getUserData();
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
        }
    }

    /**
     * Add new column in the table
     */
    public void addColumn(Column column) {
        createNewColumn(column);
        logger.info("Add column in table: " + column.getName());
    }

    /**
     * Create new column
     *
     * @param column
     */
    private void createNewColumn(Column column){

        TableColumn tableColumn = new TableDataResolver().getColumn(column, false);
        mainTable.getColumns().add(tableColumn);

        ObservableList<ObservableList> items = mainTable.getItems();
        if (items != null && !items.isEmpty()) {
            // Fill in the data.
            items.parallelStream().forEach(item -> item.add(DEFAULT_CELL_VALUE));
            mainTable.setItems(items);
        }
    }

}
