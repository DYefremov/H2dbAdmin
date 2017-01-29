package by.post.control.db;

import by.post.control.Context;
import by.post.control.ui.TypedTreeItem;
import by.post.data.*;
import by.post.data.type.ColumnDataType;
import by.post.ui.ColumnDialog;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TreeView;
import javafx.scene.image.ImageView;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Dmitriy V.Yefremov
 */
public class TableEditor {

    private DbControl dbControl = null;
    private TableView mainTable;
    private Row lastSelectedRow;
    private Deque<Object> rows;

    private static TableEditor instance = new TableEditor();

    private static final String DEFAULT_CELL_VALUE = "value";
    private static final String DEFAULT_NUM_CELL_VALUE = "0";

    private static final Logger logger = LogManager.getLogger(TableEditor.class);

    private TableEditor() {
        dbControl = DbController.getInstance();
        rows = new ArrayDeque<>();
    }

    public static TableEditor getInstance() {
        return instance;
    }

    public void setTable(TableView mainTable) {
        this.mainTable = mainTable;
    }

    /**
     * Add table in the tree
     *
     * @param tableTree
     */
    public void addTable(TreeView tableTree, Table table, ImageView icon, TableType type) {

        try {
            String name = table.getName();

            dbControl.update(Queries.createTable(table));

            addTableTreeItem(tableTree, icon, type, name);

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
     * @param type
     */
    public void addView(TreeView tableTree, View view, ImageView icon, TableType type) {

        if (view.getTables() == null) {
            new Alert(Alert.AlertType.INFORMATION, "No table is present!").showAndWait();
            return;
        }

        if (view.getTables().size() > 1) {
            new Alert(Alert.AlertType.INFORMATION, "Not implemented yet for more than one table!").showAndWait();
            return;
        }

        try {
            String name = view.getName();

            dbControl.update(Queries.createView(view));

            addTableTreeItem(tableTree, icon, type, name);

            logger.info("Added new  table: " + name);
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

        TypedTreeItem treeItem = new TypedTreeItem(name, icon, type);
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

    /**
     * Change column properties in the table
     *
     * @param oldColumn
     * @param newColumn
     */
    public void changeColumnProperties(Column oldColumn, Column newColumn) {

        try {
            dbControl.update(Queries.changeColumn(oldColumn, newColumn));
        } catch (SQLException e) {
            logger.error("Table editor error[addColumn]: " + e);
            new Alert(Alert.AlertType.ERROR, "Failed to change the column.\nSee more info in console!").showAndWait();
        }

        logger.info("Modified column in the table.");
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
            logger.error("Table editor error[addColumn]: " + e);
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

            logger.info("Column deleted.");
        } catch (Exception e) {
            logger.error("Table editor error[deleteColumn]: " + e);
            new Alert(Alert.AlertType.ERROR, "Failure to remove the column.\nSee more info in console!").showAndWait();
        }
    }

    /**
     * Save changes after row editing
     *
     * @param rowIndex
     */
    public void saveRow(int rowIndex) {

        try {
            if (mainTable.getItems().size() == getDbRowsCount()) {
                changeRow();
            } else {
               while (!rows.isEmpty()) {
                   dbControl.update(Queries.addRow(getRow(Commands.ADD, rowIndex, rows.pollLast())));
               }
            }
        } catch (SQLException e) {
            logger.error("Table editor error[saveRow]: " + e);
            new Alert(Alert.AlertType.ERROR, "Failed to save the row..\nSee more info in console!").showAndWait();
        }
        logger.info("Save changes for row to database.");
    }

    /**
     * Save the currently selected row.
     */
    public void saveCurrentRow() {

        int selectedIndex = mainTable.getSelectionModel().getSelectedIndex();

        if (lastSelectedRow != null && !lastSelectedRow.getTableName().equals(mainTable.getId())) {
            lastSelectedRow = null;
        }

        if (selectedIndex != -1) {
            if (lastSelectedRow == null || lastSelectedRow.getNum() != selectedIndex) {
                lastSelectedRow = getRow(Commands.CHANGE, selectedIndex, null);
            }
        }
    }

    /**
     * @return true if there is unsaved data
     */
    public boolean hasNotSavedData() {
        return !rows.isEmpty();
    }

    /**
     *
     */
    public void clearSavedData() {
        rows.clear();
    }

    /**
     * Add new row to the table
     */
    public void addRow() throws IOException, SQLException {

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

        ObservableList items = mainTable.getSelectionModel().getSelectedItems();

        if (items.isEmpty()) {
            return;
        }

        items.forEach(item -> {
            try {
                if (rows.contains(item)) {
                    rows.remove(item);
                } else {
                    dbControl.update(Queries.deleteRow(getRow(Commands.DELETE, 0, item)));
                }
            } catch (SQLException e) {
                logger.error("Table editor error[deleteRow]: " + e);
                new Alert(Alert.AlertType.ERROR, "Failure to delete the row.\nSee more info in console!").showAndWait();
            }
        });

        mainTable.getItems().removeAll(items);
    }

    /**
     * Changing the row.
     */
    public void changeRow() {

        int selectedIndex = mainTable.getSelectionModel().getSelectedIndex();

        if (selectedIndex == -1 || lastSelectedRow == null) {
            return;
        }

        Row row = getRow(Commands.CHANGE, selectedIndex, null);

        if (!lastSelectedRow.equals(row)) {
            try {
                String query = Queries.changeRow(lastSelectedRow, row);
                dbControl.update(query);
                lastSelectedRow = row;
            } catch (SQLException e) {
                logger.error("Table editor error[changeRow]: " + e);
                new Alert(Alert.AlertType.ERROR, "Failure to change the row.\nSee more info in console!").showAndWait();
            }
        }
    }

    /**
     * Create new row
     */
    private void createNewRow() throws SQLException {

        int selectedIndex = mainTable.getSelectionModel().getSelectedIndex();
        selectedIndex = selectedIndex == -1 ? ++selectedIndex : selectedIndex;

        Row row = getRow(Commands.ADD, selectedIndex, null);
        List values = row.getCells().stream().map(cell -> cell.getValue()).collect(Collectors.toList());

        mainTable.getItems().add(selectedIndex, FXCollections.observableArrayList(values));
        mainTable.getSelectionModel().select(selectedIndex, null);
        rows.add(mainTable.getSelectionModel().getSelectedItem());
    }

    /**
     * Generating row object during the addition, removal or editing row of the table.
     *
     * @param command
     * @return needed row
     */
    private Row getRow(Commands command, int selectedIndex, Object rowItem) {

        boolean add = command.equals(Commands.ADD);
        List<String> rowValues;

        if (rowItem != null) {
            rowValues = (List<String>) rowItem;
        } else {
            rowValues = selectedIndex != -1 ? (List<String>) mainTable.getItems().get(selectedIndex) : null;
        }

        List<TableColumn> columns = mainTable.getColumns();
        List<Cell> cells = new ArrayList<>();

        ColumnDataType columnDataType = Context.getCurrentDataType();

        columns.forEach(c -> {
            Column column = (Column) c.getUserData();
            int index = columns.indexOf(c);
            int dataType = columnDataType.getNumType(column.getType());

            String value;

            if (columnDataType.isLargeObject(dataType)) {
                value = rowValues == null ? "" : rowValues.get(index);
            } else if (columnDataType.isNumericType(dataType)) {
                value = rowValues == null ? DEFAULT_NUM_CELL_VALUE : rowValues.get(index);
            } else {
                value = rowValues == null ? DEFAULT_CELL_VALUE : rowValues.get(index);
            }

            cells.add(new Cell(column.getColumnName(), column.getType(), value));
        });

        Row row = new Row();
        row.setCells(cells);
        row.setNum(add ? ++selectedIndex : selectedIndex);
        row.setTableName(mainTable.getId());

        return row;
    }

    /**
     * Get record count in database.
     *
     * @return
     */
    private int getDbRowsCount() throws SQLException {

        try (Statement statement = dbControl.execute(Queries.getRecordsCount(mainTable.getId()));
             ResultSet resultSet = statement.getResultSet()) {
            resultSet.next();
            return resultSet.getInt(1);
        }
    }
}
