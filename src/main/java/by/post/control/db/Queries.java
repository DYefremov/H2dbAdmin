package by.post.control.db;

import by.post.data.Cell;
import by.post.data.Column;
import by.post.data.Row;

import java.util.List;

/**
 * @author Dmitriy V.Yefremov
 */
public class Queries {

    /**
     * @param tableName
     * @return
     */
    public static String getTable(String tableName) {
        return "SELECT * FROM " + tableName.toUpperCase();
    }

    /**
     * @param tableName
     * @return
     */
    public static String createTable(String tableName) {
        return "CREATE TABLE " + tableName.toUpperCase();
    }

    /**
     * @param tableName
     * @return
     */
    public static String deleteTable(String tableName){
        return "DROP TABLE " + tableName.toUpperCase();
    }

    public static String getRecordsCount(String tableName) {
        return "SELECT COUNT(*) FROM " + tableName;
    }

    /**
     * @param column
     * @return string query for deleting column
     */
    public static String deleteColumn(Column column) {
        return "ALTER TABLE " + column.getTableName() + " DROP COLUMN " + column.getColumnName();
    }

    /**
     * @param column
     * @return string query for adding column
     */
    public static String addColumn(Column column) {

        StringBuilder sb = new StringBuilder("ALTER TABLE ");
        sb.append(column.getTableName());
        sb.append(" ADD " + column.getColumnName());
        sb.append(" " + column.getType());
        sb.append(column.isNotNull() || column.isPrimaryKey() ? " NOT NULL" : "");
        sb.append(column.isPrimaryKey() ? "; ALTER TABLE " + column.getTableName() +
                " ADD PRIMARY KEY (" + column.getColumnName() + ")": "" );

        return sb.toString() ;
    }

    /**
     * @param oldColumn
     * @param newColumn
     * @return
     */
    public static String changeColumn(Column oldColumn, Column newColumn) {

        StringBuilder sb = new StringBuilder();
        String tableName = oldColumn.getTableName();
        String columnName = oldColumn.getColumnName();
        String type = newColumn.getType();
        String alterQuery = "ALTER TABLE " + tableName  + " ALTER COLUMN ";

        boolean nameIsChanged = !columnName.equals(newColumn.getColumnName());
        boolean notNullIsChanged = oldColumn.isNotNull() != newColumn.isNotNull();
        boolean typeIsChanged = !type.equals(oldColumn.getType());
        // Rename column if changed
        sb.append(nameIsChanged ? alterQuery + columnName + " RENAME TO " + newColumn.getColumnName() + ";\n" : "");
        // Add column name to the query
        alterQuery = nameIsChanged ? alterQuery + newColumn.getColumnName() : alterQuery + columnName;

        sb.append(typeIsChanged ? alterQuery + " " + type +";\n" : "");
        sb.append(notNullIsChanged ? newColumn.isNotNull() ? alterQuery + " SET NOT NULL;\n" : alterQuery + " SET NULL;\n" : "");

        return sb.toString();
    }

    /**
     * @param row
     * @return string query for adding row
     */
    public static String addRow(Row row) {

        List<Cell> cells = row.getCells();

        if (cells == null || cells.isEmpty()) {
            return "";
        }

        int lastIndex = cells.size() - 1;

        StringBuilder sb = new StringBuilder("INSERT INTO " + row.getTableName() + " (");
        cells.forEach(c -> {
            sb.append(cells.indexOf(c) != lastIndex ? c.getName() +", " : c.getName());
        });

        sb.append(")\nVALUES (");

        cells.forEach(c -> {
            String value = "'" +  c.getValue() + "'";
            sb.append(cells.indexOf(c) != lastIndex ? value + ", " : value);
        });

        sb.append(");");

        return sb.toString();
    }

    /**
     * @param row
     * @return  string query for deleting row
     */
    public static String deleteRow(Row row) {

        // TODO Think may be add delete by row number (by index)

        List<Cell> cells = row.getCells();

        if (cells == null || cells.isEmpty()) {
            return "";
        }

        int lastIndex = cells.size() - 1;

        StringBuilder sb = new StringBuilder("DELETE FROM " + row.getTableName() + "\nWHERE ");

        cells.forEach(c -> {
            String value = c.getName() + "='" + c.getValue() + "'";
            sb.append(cells.indexOf(c) != lastIndex ? value + " AND " : value + ";");
        });

        return sb.toString();
    }

    /**
     * @param oldRow
     * @param changedRow
     * @return
     */
    public static String changeRow(Row oldRow, Row changedRow) {

        List<Cell> oldCells = oldRow.getCells();
        List<Cell> changedCells = changedRow.getCells();

        if (oldCells == null || changedCells == null) {
            return "";
        }
        // Removing all equals cells.
        changedCells.removeAll(oldCells);

        StringBuilder sb = new StringBuilder("UPDATE " + oldRow.getTableName() + "\nSET ");

        int lastChangedIndex = changedCells.size() - 1;

        changedCells.forEach(cc -> {
            String value = cc.getName() + "='" + cc.getValue() +"'";
            sb.append(changedCells.indexOf(cc) != lastChangedIndex ? value +"," : value + " \nWHERE ");
        });

        int lastOldIndex = oldCells.size() - 1;

        oldCells.forEach(c -> {
            String columnName = c.getName();
            String value = (String) c.getValue();
            System.out.println("value = " + value);
            value = value.equals("") ? columnName + " IS NULL OR " + columnName + "=''" : columnName + "='"+ value + "'";
            sb.append(oldCells.indexOf(c) != lastOldIndex ? value + " AND " : value + ";");
        });

        return sb.toString();
    }

}
