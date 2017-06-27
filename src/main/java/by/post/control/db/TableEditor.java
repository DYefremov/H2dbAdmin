package by.post.control.db;

import by.post.control.Context;
import by.post.data.Cell;
import by.post.data.Column;
import by.post.data.Row;
import by.post.data.type.ColumnDataType;
import by.post.ui.ColumnDialog;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.sql.SQLException;
import java.util.*;

/**
 * @author Dmitriy V.Yefremov
 */
public class TableEditor {

    private DbControl dbControl = null;
    private TableView<Row> mainTable;
    private Deque<Row> rows;
    private NavigableMap<Row, Row> changedRows;
    private ColumnDataType columnDataType;

    private static final String DEFAULT_CELL_VALUE = "value";
    private static final String DEFAULT_NUM_CELL_VALUE = "0";

    private static final Logger logger = LogManager.getLogger(TableEditor.class);

    public TableEditor() {
        init();
    }

    public TableEditor(TableView mainTable) {
        this();
        this.mainTable = mainTable;
    }

    public void setTable(TableView mainTable) {
        this.mainTable = mainTable;
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
            logger.error("Table editor error[changeColumnProperties]: " + e);
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

            ObservableList<Row> items = mainTable.getItems();

            if (items != null && !items.isEmpty()) {
                // Fill in the data.
                items.parallelStream().forEach(item -> {
                    //Get default cell  for this column
                    Cell cell = getCell(column);
                    column.setDefaultValue(cell.getValue());
                    item.getCells().add(cell);
                });
                mainTable.setItems(items);
            }
            logger.info("Add column in table: " + column.getColumnName());
        } catch (Exception e) {
            logger.error("Table editor error[addColumn]: " + e);
            new Alert(Alert.AlertType.ERROR, "Failure to create the column.\nSee more info in console!").showAndWait();
        }
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
            ObservableList<Row> items = mainTable.getItems();

            if (items != null && !items.isEmpty()) {
                // Remove cell from rows by index.
                items.parallelStream().forEach(item -> item.getCells().remove(index));
                logger.info("Column deleted.");
            }
        } catch (Exception e) {
            logger.error("Table editor error[deleteColumn]: " + e);
            new Alert(Alert.AlertType.ERROR, "Failure to remove the column.\nSee more info in console!").showAndWait();
        }
    }

    /**
     * Save changes after rows adding or editing
     */
    public void saveRow() {

        try {
            Set<Row> keys = new HashSet<>();
            while (!rows.isEmpty()) {
                Row row = rows.pollLast();
                dbControl.update(Queries.addRow(row));

                if (changedRows.containsValue(row)) {
                   for (Map.Entry entry : changedRows.entrySet()) {
                       if ( entry.getValue().equals(row)) {
                           keys.add((Row) entry.getKey());
                       }
                   }
                }
            }

            keys.forEach(key -> changedRows.remove(key));

            if (!changedRows.isEmpty()) {
                changeRows();
            }

            logger.info("Save changes for row to database.");
        } catch (SQLException e) {
            logger.error("Table editor error[saveRow]: " + e);
            new Alert(Alert.AlertType.ERROR, "Failed to save the row..\nSee more info in console!").showAndWait();
        }
    }

    /**
     * @return true if there is unsaved data
     */
    public boolean hasNotSavedData() {
        return !rows.isEmpty() || !changedRows.isEmpty();
    }

    /**
     *
     */
    public void clearSavedData() {
        rows.clear();
        changedRows.clear();
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
     * Remove  selected rows from the table
     */
    public void deleteRow() {

        List<Row> items = new ArrayList<>(mainTable.getSelectionModel().getSelectedItems());

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
                mainTable.getItems().remove(item);
            } catch (SQLException e) {
                logger.error("Table editor error[deleteRow]: " + e);
                new Alert(Alert.AlertType.ERROR, "Failure to delete the row.\nSee more info in console!").showAndWait();
            }
        });

        mainTable.refresh();
    }

    /**
     * Saving changed rows.
     */
    public void changeRows() {

        while (!changedRows.isEmpty()) {
            Map.Entry<Row, Row> entry = changedRows.pollLastEntry();
            Row oldRowValue = getRow(Commands.CHANGE, 0, entry.getKey());
            Row newRowValue = getRow(Commands.CHANGE, 0, entry.getValue());

            try {
                String query = Queries.changeRow(oldRowValue, newRowValue);
                dbControl.update(query);
            } catch (SQLException e) {
                logger.error("Table editor error[changeRow]: " + e);
                new Alert(Alert.AlertType.ERROR, "Failure to change the row.\nSee more info in console!").showAndWait();
            }
        }
    }

    /**
     * @param oldValue
     * @param newValue
     */
    public void saveCurrentChangedRow(Row oldValue, Row newValue) {

        if (changedRows.containsKey(oldValue)) {
            changedRows.replace(oldValue, oldValue, newValue);
        } else {
            changedRows.put(oldValue, newValue);
        }
    }

    /**
     * Create new row
     */
    private void createNewRow() throws SQLException {

        int selectedIndex = mainTable.getSelectionModel().getSelectedIndex();
        Row row = getRow(Commands.ADD, selectedIndex, null);
        selectedIndex = selectedIndex == -1 ? ++selectedIndex : selectedIndex;

        mainTable.getItems().add(selectedIndex, row);
        mainTable.getSelectionModel().select(selectedIndex, null);
        rows.add(mainTable.getSelectionModel().getSelectedItem());
    }

    /**
     * Generating row object during the addition, removal or editing row of the table.
     *
     * @param command
     * @return needed row
     */
    private Row getRow(Commands command, int selectedIndex, Row rowItem) {

        Row row = null;

        switch (command) {
            case ADD:
                row = getNewRow(selectedIndex);
                break;
            case CHANGE:
            case DELETE:
                row = rowItem == null ? null : getRowForDeleting(rowItem);
                break;
            default:
                break;
        }

        return row;
    }

    /**
     * @param rowItem
     * @return prepared row object for deleting
     */
    private Row getRowForDeleting(Row rowItem) {

        ObservableList<TableColumn<Row, ?>> columns = mainTable.getColumns();

        List<Cell> cells = rowItem.getCells();
        columns.forEach(c -> {
            Column column = (Column) c.getUserData();
            cells.get(columns.indexOf(c)).setColumnName(column.getColumnName());
        });

        rowItem.setTableName(mainTable.getId());

        return rowItem;
    }

    /**
     * @param selectedIndex
     * @return new row object
     */
    private Row getNewRow(int selectedIndex) {

        Row row = new Row();

        ObservableList<TableColumn<Row, ?>> columns = mainTable.getColumns();
        List<Cell> cells = new ArrayList<>();

        columns.forEach(c -> {
            Column column = (Column) c.getUserData();
            Cell cell = getCell(column);
            cells.add(columns.indexOf(c), cell);
        });

        row.setCells(cells);
        row.setNum(++selectedIndex);
        row.setTableName(mainTable.getId());

        return row;
    }

    /**
     * @param column
     * @return
     */
    private Cell getCell(Column column) {

        int dataType = columnDataType.getNumType(column.getType());
        boolean isNotNull = column.isNotNull();

        String value;

        if (columnDataType.isLargeObject(dataType)) {
            value = null;
        } else if (columnDataType.isNumericType(dataType)) {
            value = isNotNull ? DEFAULT_NUM_CELL_VALUE : null;
        } else {
            value = isNotNull ? DEFAULT_CELL_VALUE : null;
        }

        return new Cell(dataType, column.getColumnName(), value);
    }

    /**
     * Initialize
     */
    private void init() {

        dbControl = DbController.getInstance();
        rows = new ArrayDeque<>();
        columnDataType = Context.getCurrentDataType();

        changedRows = new TreeMap<>((o1, o2) -> {
            int num1 = o1.getNum();
            int num2 = o2.getNum();

            if (num1 == num2) {
                return o1.getCells() != null && o1.getCells().equals(o2.getCells()) ? 0 : -1;
            }

            return num1 > num2 ? 1 : -1;
        });
    }

}
