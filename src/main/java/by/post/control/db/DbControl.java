package by.post.control.db;

import by.post.data.Table;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collection;
import java.util.List;

/**
 * @author Dmitriy V.Yefremov
 */
public interface DbControl {

    /**
     * @param url
     * @param user
     * @param password
     */
    void connect(String url, String user, String password) ;

    /**
     * @param url
     * @param user
     * @param password
     * @return connection
     */
    Connection getConnection(String url, String user, String password);

    /**
     * @return current connection
     */
    Connection getCurrentConnection();

    /**
     * @return current database name
     */
    String getCurrentDbName();

    /**
     * @return table names list
     */
    List<String> getTablesList(String type);

    /**
     * @param name
     * @return table by name
     */
    Table getTable(String name, TableType type);

    /**
     * @param tableName
     * @param type
     * @return data for table
     */
    Collection<?> getTableData(String tableName, TableType type);

    /**
     * @param table
     */
    void update(Table table);

    /**
     * @param sql
     */
    Statement update(String sql) throws SQLException;

    /**
     * Execute custom sql query
     *
     * @param sql
     */
    Statement execute(String sql) throws SQLException;

    /**
     * Close opened connection
     */
    void closeConnection();

    /**
     * @return true if connection is closed
     */
    boolean isClosed();
}
