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
     * @param name
     * @return
     */
    public static String getTable(String name) {
        return "SELECT * FROM " + name.toUpperCase();
    }

    /**
     * @param name
     * @return
     */
    public static String createTable(String name) {
        return "CREATE TABLE " + name.toUpperCase();
    }

    /**
     * @param name
     * @return
     */
    public static String deleteTable(String name){
        return "DROP TABLE " + name.toUpperCase();
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
     * @param column
     * @return
     */
    public static String updateColumn(Column column) {
        return "";
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
}
