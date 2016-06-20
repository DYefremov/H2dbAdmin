package by.post.control.db;

import by.post.control.PropertiesController;
import by.post.data.Table;
import by.post.ui.Resources;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Control work with database
 *
 * @author Dmitriy V.Yefremov
 */
public class DbController implements DbControl {

    private String db;
    //The connection only succeeds when the database already exists
    private String exists = ";IFEXISTS=TRUE";
    private Connection connection = null;

    private static DbControl instance = new DbController();

    private static final Logger logger = LogManager.getLogger(DbController.class);

    private DbController() {
        try {
            Class.forName(PropertiesController.getProperties().getProperty("driver"));
        } catch (ClassNotFoundException e) {
            logger.error("DbController error: " + e);
        }
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
        String url = "jdbc:h2:" + path + db + exists;

        if (connection == null) {
            try {
                connection = DriverManager.getConnection(url, user, password);
            } catch (SQLException e) {
                logger.error("DbController connect error: " + e);
            }
        } else {
            try {
                connection.close();
                connection = DriverManager.getConnection(url, user, password);
            } catch (SQLException e) {
                logger.error("DbController connect error: " + e);
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
                ResultSet rs = connection.getMetaData().getTables(null, null, "%", new String[]{"TABLE"});
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

}

