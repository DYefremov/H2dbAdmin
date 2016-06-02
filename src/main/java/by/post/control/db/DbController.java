package by.post.control.db;

import by.post.data.Table;
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

    private String driver = "org.h2.Driver";
    private String db;

    private Connection connection = null;

    private static final Logger logger = LogManager.getLogger(DbControl.class);

    public DbController() {
        try {
            Class.forName(driver);
        } catch (ClassNotFoundException e) {
            logger.error("DbController error: " + e);
        }
    }

    /**
     * @param driver
     */
    public DbController(String driver) {
        this.driver = driver;
        try {
            Class.forName(driver);
        } catch (ClassNotFoundException e) {
            logger.error("DbController error: " + e);
        }
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
        String url = "jdbc:h2:" + path + db;

        if (connection == null) {
            try {
                connection = DriverManager.getConnection(url, user, password);
            } catch (SQLException e) {
                logger.error("DbController error: " + e);
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

        if (name.equals(db)) {
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
        TableBuilder builder = new TableBuilder();
        builder.update(table, connection);
    }

}

