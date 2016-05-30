package by.post.control;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Control work with database
 *
 * @author Dmitriy V.Yefremov
 */
public class DbController implements DbControl {

  private String driver = "org.h2.Driver";

  private static Connection connection = null;

  private static final Logger logger = LogManager.getLogger(DbControl.class);

  public DbController(){
  }

  /**
   * @param driver
   */
  public DbController(String driver){
    this.driver = driver;
  }

  /**
   * Connect to database
   *
   * @param db путь + имя базы
   * @param user
   * @param password
   */
  @Override
  public void connect(String db, String user, String password) {

    String url = "jdbc:h2:" + db;

    if (connection == null) {
      try {
        connection = DriverManager.getConnection(url, user, password);
      } catch (SQLException e) {
        logger.error("DbController error: " +  e);
      }
    } else {
      try {
        connection.close();
        connection = DriverManager.getConnection(url, user, password);
      } catch (SQLException e) {
        logger.error("DbController error: " +  e);
      }
    }

  }
}

