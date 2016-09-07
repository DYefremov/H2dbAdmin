package by.post.control.db;

import by.post.data.Cell;
import by.post.data.Column;
import by.post.data.Row;
import by.post.ui.ColumnDialog;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
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
     * Save changes after table editing
     *
     * @param name
     */
    public void save(String name) {
        logger.log(Level.INFO, "Save changes for  table: " + name);
    }

    /**
     * Add table in the tree
     *
     * @param tableTree
     */
    public void addTable(TreeView tableTree, String name) {

        try {
            dbControl.update(Queries.createTable(name));

            TreeItem treeItem = new TreeItem<>(name);
            tableTree.getRoot().getChildren().add(treeItem);
            tableTree.getSelectionModel().select(treeItem);
            tableTree.scrollTo(tableTree.getSelectionModel().getSelectedIndex());
            tableTree.refresh();

            logger.log(Level.INFO, "Added new  table: " + name);
        } catch (Exception e) {
            logger.log(Level.ERROR, "Table editor error[addTable]: " + e);
            new Alert(Alert.AlertType.ERROR, "Failure to add  the table.\nSee more info in console!").showAndWait();
        }
    }

    /**
     * Delete table from the tree
     *
     * @param tableTree
     */
    public void deleteTable(TreeView tableTree) {

        try {
            TreeItem item = (TreeItem) tableTree.getSelectionModel().getSelectedItem();
            String name = item.getValue().toString();

            dbControl.update(Queries.deleteTable(name));

            tableTree.getRoot().getChildren().remove(item);
            tableTree.refresh();

            logger.log(Level.INFO, "Deleted table: " + name);
        } catch (Exception e) {
            logger.log(Level.ERROR, "Table editor error[deleteTable]: " + e);
            new Alert(Alert.AlertType.ERROR, "Failure to remove the table.\nSee more info in console!").showAndWait();
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
     * Add new column in the table
     */
    public void addColumn(Column column) {

        try {
            column.setTableName(mainTable.getId());

            dbControl.update(Queries.addColumn(column));

            TableColumn tableColumn = new TableDataResolver().getColumn(column);
            mainTable.getColumns().add(tableColumn);

            ObservableList<ObservableList> items = mainTable.getItems();
            if (items != null && !items.isEmpty()) {
                // Fill in the data.
                items.parallelStream().forEach(item -> item.add(""));
                mainTable.setItems(items);
            }
        } catch (Exception e) {
            logger.log(Level.ERROR, "Table editor error[addColumn]: " + e);
            new Alert(Alert.AlertType.ERROR, "Failure to create the column.\nSee more info in console!").showAndWait();
        }

        logger.info("Add column in table: " + column.getColumnName());
    }

    /**
     * Delete column from the table
     *
     * @param column
     */
    public void deleteColumn(TableColumn column) {

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
            logger.log(Level.ERROR, "Table editor error[deleteColumn]: " + e);
            new Alert(Alert.AlertType.ERROR, "Failure to remove the column.\nSee more info in console!").showAndWait();
        }
    }

    /**
     * Add new row to the table
     */
    public void addRow() throws IOException {

        int size = mainTable.getColumns().size();

        if (size > 0) {
            createNewRow();
        } else {
            // If table has no columns
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
    public void deleteRow() {

        Row row = getRow(false, null);
        int selectedIndex = row.getNum();

        if (selectedIndex == -1) {
            return;
        }

        try {
            dbControl.update(Queries.deleteRow(row));
            mainTable.getItems().remove(selectedIndex);
        } catch (SQLException e) {
            logger.log(Level.ERROR, "Table editor error[deleteRow]: " + e);
            new Alert(Alert.AlertType.ERROR, "Failure to delete the row.\nSee more info in console!").showAndWait();
        }
    }

    /**
     * Changing the row when editing cells.
     */
    public boolean changeRow(Cell changedCell, List<String> rowValues) {
        try {
            dbControl.update(Queries.changeRow(getRow(false, rowValues), changedCell));
            return true;
        } catch (SQLException e) {
            logger.log(Level.ERROR, "Table editor error[changeRow]: " + e);
            new Alert(Alert.AlertType.ERROR, "Failure to change the row.\nSee more info in console!").showAndWait();
        }

        return false;
    }

    /**
     * Create new row
     */
    private void createNewRow() {

        try {
            Row row = getRow(true, null);
            int selectedIndex = row.getNum();
            int columnCount = row.getCells().size();

            dbControl.update(Queries.addRow(row));

            mainTable.getItems().add(selectedIndex, FXCollections.observableArrayList(Collections.nCopies(columnCount, DEFAULT_CELL_VALUE)));
            mainTable.getSelectionModel().select(selectedIndex, null);
        } catch (Exception e) {
            logger.log(Level.ERROR, "Table editor error[createNewRow]: " + e);
            new Alert(Alert.AlertType.ERROR, "Failure to create the row.\nSee more info in console!").showAndWait();
        }
    }

    /**
     *Generating row object during the addition, removal or editing row of the table.
     *
     * @param addRow
     * @return needed row
     */
    private Row getRow(boolean addRow, List<String> rowValues) {

        Row row = new Row();
        int selectedIndex = mainTable.getSelectionModel().getSelectedIndex();

        if (selectedIndex == -1 && !addRow) {
            row.setNum(selectedIndex);
            return row;
        }

        List<TableColumn> columns = mainTable.getColumns();
        List<Cell> cells = new ArrayList<>();

        columns.forEach(c -> {
            int index = columns.indexOf(c);
            Column column = (Column) c.getUserData();
            String value = rowValues == null ? DEFAULT_CELL_VALUE : rowValues.get(index);
            cells.add(new Cell(column.getColumnName(), column.getType(), value));
        });

        row.setCells(cells);
        row.setNum(addRow ? ++selectedIndex : selectedIndex);
        row.setTableName(mainTable.getId());

        return row;
    }
}
