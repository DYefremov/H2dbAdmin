package by.post.control.db;

import by.post.control.PropertiesController;
import by.post.data.Table;
import by.post.ui.Resources;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Control work with database
 *
 * @author Dmitriy V.Yefremov
 */
public class DbController implements DbControl {

    private String db;
    private Connection connection = null;

    private static final DbControl instance = new DbController();
    //The connection only succeeds when the database already exists
    private static final String EXISTS_FLAG = ";IFEXISTS=TRUE";
    private static final Logger logger = LogManager.getLogger(DbController.class);

    private DbController() {

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
     * @param path
     * @param db
     * @param user
     * @param password
     */
    @Override
    public void connect(String path, String db, String user, String password) {

        this.db = db;
        String url = "jdbc:h2:" + path + db + EXISTS_FLAG;

        if (connection == null) {
            try {
                Class.forName(PropertiesController.getProperties().getProperty("driver"));
                connection = DriverManager.getConnection(url, user, password);
            } catch (ClassNotFoundException e) {
                logger.error("DbController error in connect: " + e);
            } catch (SQLException e) {
                logger.error("DbController error in connect: " + e);
            }
        } else {
            try {
                connection.close();
                connection = DriverManager.getConnection(url, user, password);
            } catch (SQLException e) {
                logger.error("DbController error in connect: " + e);
            }
        }
    }

    /**
     * @param path
     * @param db
     * @param user
     * @param password
     * @return connection
     */
    @Override
    public Connection getConnection(String path, String db, String user, String password) {

        connect(path, db, user, password);

        return connection;
    }

    /**
     * @return table names list
     */
    @Override
    public List<String> getTablesList() {

        List<String> tables = new ArrayList<>();

        if (connection != null) {
            try {
                ResultSet rs = connection.getMetaData().getTables(null, null, "%", new String[]{"TABLE", "VIEW"});
                while (rs.next()) {
                    tables.add(rs.getString("TABLE_NAME"));
                }
            } catch (SQLException e) {
                logger.error("DbController error in getTablesList: " + e);
            }
        }

        return tables;
    }

    /**
     * @param name
     * @return table
     */
    @Override
    public Table getTable(String name) {

        if (connection == null || name.equals(db)) {
            return new Table(name);
        }

        Table table = null;

        try {
            table = new TableBuilder().getTable(name, connection);
        } catch (SQLException e) {
            logger.error("DbController error in getTable: " + e);
        }

        return table != null ? table : new Table(name);
    }

    /**
     * @param table
     */
    @Override
    public void update(Table table) {

        if (connection == null) {
            return;
        }

        TableBuilder builder = new TableBuilder();
        builder.update(table, connection);
    }

    @Override
    public void update(String sql) {

        Statement statement = null;

        try {
            statement = connection.createStatement();
            statement.executeUpdate(sql);
        } catch (SQLException e) {
            logger.error("DbController error in update[sql]: " + e);
        }
    }

    @Override
    public void execute(String sql) {

    }

}

