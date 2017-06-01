package by.post.control.db;

import by.post.control.PropertiesController;
import by.post.data.Table;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Control work with database
 *
 * @author Dmitriy V.Yefremov
 */
public class DbController implements DbControl {

    private Connection connection = null;
    private TableBuilder tableBuilder;

    private static final DbController instance = new DbController();

    private static final Logger logger = LogManager.getLogger(DbController.class);

    private DbController() {
            tableBuilder = new TableBuilder();
    }

    /**
     * @return instance for DbControl
     */
    public static DbControl getInstance() {

        return instance;
    }

    /**
     * Connect to database
     *
     * @param url
     * @param user
     * @param password
     */
    @Override
    public void connect(String url, String user, String password) {

        if (connection == null) {
            try {
                Class.forName(PropertiesController.getProperties().getProperty("driver"));
                connection = DriverManager.getConnection(url, user, password);
            } catch (ClassNotFoundException e) {
                logger.error("DbController error [connect]: " + e);
            } catch (SQLException e) {
                logger.error("DbController error [connect]: " + e);
            }
        } else {
            try {
                if (!connection.isClosed()) {
                    connection.close();
                }
                connection = DriverManager.getConnection(url, user, password);
            } catch (SQLException e) {
                logger.error("DbController error [connect]: " + e);
            }
        }
    }

    /**
     * @param url
     * @param user
     * @param password
     * @return connection
     */
    @Override
    public Connection getConnection(String url, String user, String password) {

        connect(url, user, password);

        return connection;
    }

    @Override
    public Connection getCurrentConnection() {
        return connection;
    }

    @Override
    public String getCurrentDbName() {

        String name = null;

        if (connection != null) {
            try {
                name = connection.getCatalog();
            } catch (SQLException e) {
                logger.error("DbController error [getCurrentDbName]: " + e);
            }
        }

        return name != null ? name : "unknown";
    }

    /**
     * @return table names list
     */
    @Override
    public List<String> getTablesList(String type) {

        List<String> tables = new ArrayList<>();

        if (connection != null) {
            try (ResultSet rs = connection.getMetaData().getTables(null, null, "%", new String[]{type})){
                while (rs.next()) {
                    tables.add(rs.getString("TABLE_NAME"));
                }
            } catch (SQLException e) {
                logger.error("DbController error [getTablesList]: " + e);
            } 
        }

        return tables;
    }

    /**
     * @param name
     * @return table
     */
    @Override
    public Table getTable(String name, TableType type) {

        if (connection == null) {
            return new Table(name);
        }

        Table table = tableBuilder.getTable(name, type, connection);

        return table != null ? table : new Table(name);
    }

    @Override
    public Collection<?> getTableData(String tableName, TableType type) {
        return tableBuilder.getTableData(tableName, type, connection);
    }

    /**
     * @param table
     */
    @Override
    public void update(Table table) {

        if (connection == null) {
            return;
        }

        tableBuilder.update(table, connection);
    }

    /**
     * @param sql
     * @return statement
     * @throws SQLException
     */
    @Override
    public Statement update(String sql) throws SQLException {

        Statement statement = connection.createStatement();
        statement.executeUpdate(sql);

        return statement;
    }

    /**
     * @param sql
     * @return statement
     * @throws SQLException
     */
    @Override
    public Statement execute(String sql) throws SQLException {

        Statement  statement = connection.createStatement();
        statement.execute(sql);

        return statement;
    }

    /**
     * Close connection to database
     */
    @Override
    public void closeConnection() {

        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                logger.error("DbController error [closeConnection]: " + e);
            }
        }
    }

    @Override
    public boolean isClosed() {

        try {
            return connection == null ? true : connection.isClosed();
        } catch (SQLException e) {
            logger.error("DbController error [isClosed]: " + e);
        }
        return false;
    }

}

