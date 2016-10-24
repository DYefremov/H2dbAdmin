package by.post.control.db;

import by.post.data.Cell;
import by.post.data.Column;
import by.post.data.Row;
import by.post.ui.ColumnDialog;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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
    private Row lastSelectedRow;

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
     * Save changes after row editing
     *
     * @param rowIndex
     */
    public void saveRow(int rowIndex) {

        try {
            if (mainTable.getItems().size() == getDbRowsCount()) {
                changeRow();
            } else {
                dbControl.update(Queries.addRow(getRow(Commands.ADD, rowIndex)));
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

        if (lastSelectedRow !=null && !lastSelectedRow.getTableName().equals(mainTable.getId())) {
            lastSelectedRow = null;
        }

        if (selectedIndex != -1) {
            if (lastSelectedRow == null || lastSelectedRow.getNum() != selectedIndex) {
                lastSelectedRow = getRow(Commands.CHANGE, selectedIndex);
            }
        }
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

            logger.info("Added new  table: " + name);
        } catch (Exception e) {
            logger.error("Table editor error[addTable]: " + e);
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

        int selectedIndex = mainTable.getSelectionModel().getSelectedIndex();

        if (selectedIndex == -1) {
            return;
        }

        Row row = getRow(Commands.DELETE, selectedIndex);

        try {
            dbControl.update(Queries.deleteRow(row));
            mainTable.getItems().remove(selectedIndex);
        } catch (SQLException e) {
            logger.error("Table editor error[deleteRow]: " + e);
            new Alert(Alert.AlertType.ERROR, "Failure to delete the row.\nSee more info in console!").showAndWait();
        }
    }

    /**
     * Changing the row.
     */
    public void changeRow() {

        int selectedIndex = mainTable.getSelectionModel().getSelectedIndex();

        if (selectedIndex == -1 || lastSelectedRow == null) {
            return;
        }

        Row row = getRow(Commands.CHANGE, selectedIndex);

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


        if (mainTable.getItems().size() != getDbRowsCount()) {
            new Alert(Alert.AlertType.ERROR, "Please, save previous row!").showAndWait();
            return;
        }

        Row row = getRow(Commands.ADD, selectedIndex);
        int columnCount = row.getCells().size();

        selectedIndex = selectedIndex == -1 ? ++selectedIndex : selectedIndex;

        mainTable.getItems().add(selectedIndex, FXCollections.observableArrayList(Collections.nCopies(columnCount, DEFAULT_CELL_VALUE)));
        mainTable.getSelectionModel().select(selectedIndex, null);
    }

    /**
     * Generating row object during the addition, removal or editing row of the table.
     *
     * @param command
     * @return needed row
     */
    private Row getRow(Commands command, int selectedIndex) {

        boolean add = command.equals(Commands.ADD);

        List<String> rowValues = selectedIndex != -1 ? (List<String>) mainTable.getItems().get(selectedIndex) : null;
        List<TableColumn> columns = mainTable.getColumns();
        List<Cell> cells = new ArrayList<>();

        columns.forEach(c -> {
            int index = columns.indexOf(c);
            Column column = (Column) c.getUserData();
            String value = rowValues == null ? DEFAULT_CELL_VALUE : rowValues.get(index);
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

        Statement statement = dbControl.execute(Queries.getRecordsCount(mainTable.getId()));
        ResultSet resultSet = statement.getResultSet();
        resultSet.next();

        int count = resultSet.getInt(1);

        if (statement != null) {
            statement.close();
        }

        if (resultSet != null) {
            resultSet.close();
        }

        return count;
    }
}
