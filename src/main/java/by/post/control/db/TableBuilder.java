package by.post.control.db;

import by.post.data.Table;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
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

        DatabaseMetaData dm = connection.getMetaData( );
        ResultSet keys = dm.getPrimaryKeys( "" , "" , name);

        while(keys.next( ))
        {
            String pk = keys.getString("COLUMN_NAME");
            table.setPrimaryKey(pk);
        }
        keys.close();

        return table;
    }

    /**
     * @param table
     * @param connection
     */
    public void update(Table table, Connection connection) {

    }
}
