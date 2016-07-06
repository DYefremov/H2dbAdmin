package by.post.control;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.util.Properties;

/**
 * @author Dmitriy V.Yefremov
 */
public class PropertiesController {

    private static final String PATH = "~/";
    private static final String DB = "test";
    private static final String USER = "sa";
    private static final String PASSWORD = "";
    private static final String DRIVER = "org.h2.Driver";
    private static Properties properties = new Properties();

    private static final Logger logger = LogManager.getLogger(PropertiesController.class);

    /**
     * Set properties for db connection
     *
     * @param path
     * @param dbName
     * @param user
     * @param password
     */
    public static void setProperties(String path, String dbName, String user, String password) {

        properties.put("driver", DRIVER);
        properties.put("path", path != null ? path : PATH);
        properties.put("db", dbName != null ? dbName : DB);
        properties.put("user", user != null ? user : USER);
        properties.put("password", password != null ? password : PASSWORD);

        OutputStream out = null;

        try {
            out = new FileOutputStream("config.properties");
            properties.store(out, null);
        } catch (IOException e) {
            logger.error("PropertiesController error: " + e);
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    logger.error("PropertiesController error: " + e);
                }
            }
        }

    }

    /**
     * @return properties for db connection
     */
    public static Properties getProperties() {

        if (!properties.isEmpty()) {
            return properties;
        }

        InputStream in = null;

        try {
            File file = new File("config.properties");

            if (!file.exists()) {
                setProperties(PATH, DB, USER, PASSWORD);
            }

            in = new FileInputStream("config.properties");
            properties.load(in);
        } catch (IOException e) {
            logger.error("PropertiesController error: " + e);
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    logger.error("PropertiesController error: " + e);
                }
            }
        }

        return properties;
    }

}
