package by.post.control.db;

import by.post.data.Table;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * @author Dmitriy V.Yefremov
 */
public class TableBuilder {

    /**
     * @param name
     * @param connection
     * @return
     */
    public Table getTable(String name, Connection connection) throws SQLException {
        Table table = new Table(name);
        table.setRows(new RowBuilder().getRows(name, connection));
        return table;
    }

    /**
     * @param table
     * @param connection
     */
    public void update(Table table, Connection connection) {

    }
}
