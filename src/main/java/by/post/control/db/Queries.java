package by.post.control.db;

import by.post.data.Column;

/**
 * @author Dmitriy V.Yefremov
 */
public class Queries {

    public static String getTable(String name) {
        return "SELECT * FROM " + name.toUpperCase();
    }

    public static String createTable(String name) {
        return "CREATE TABLE " + name.toUpperCase();
    }

    public static String deleteTable(String name){
        return "DROP TABLE " + name.toUpperCase();
    }

    public static String deleteColumn(Column column) {
        return "ALTER TABLE " + column.getTableName() + " DROP COLUMN " + column.getColumnName();
    }

    public static String addColumn(Column column) {
        StringBuilder sb = new StringBuilder("ALTER TABLE ");
        sb.append(column.getTableName());
        sb.append(" ADD " + column.getColumnName());
        sb.append(" " + column.getType());
        sb.append(column.isNotNull() ? " NOT NULL" : "");

        return sb.toString() ;
    }

    public static String updateColumn(Column column) {
        return "";
    }
}
