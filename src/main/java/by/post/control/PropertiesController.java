package by.post.control;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.util.Enumeration;
import java.util.Map;
import java.util.Properties;

/**
 * @author Dmitriy V.Yefremov
 */
public class PropertiesController {

    private static final String URL = "~/";
    private static final String USER = "sa";
    private static final String PASSWORD = "";
    private static final String DRIVER = "org.h2.Driver";

    private static Properties properties = new Properties();

    private static final Logger logger = LogManager.getLogger(PropertiesController.class);

    /**
     * Set properties for db connection
     *
     * @param url
     * @param user
     * @param password
     */
    public static void setProperties(String url, String user, String password) {

        properties.put("driver", DRIVER);
        properties.put("url", url != null ? url : URL);
        properties.put("user", user != null ? user : USER);
        properties.put("password", password != null ? password : PASSWORD);

        save();
    }

    /**
     * @param settings
     */
    public static void setProperties(Map<String, String> settings) {

        Map<String, String> st = settings;
        String user = st.get("user");
        String password = st.get("password");
        boolean embedded = Boolean.valueOf(st.get("embedded"));
        String url = getConnectionUrl(st.get("host"), st.get("port"), st.get("path"), embedded);

        setProperties(url, user, password);
    }

    /**
     * @param properties
     */
    public static void setProperties(Properties properties) {

        Enumeration<String> keys = (Enumeration<String>) properties.propertyNames();

        while (keys.hasMoreElements()) {
            String key = keys.nextElement();
            properties.put(key, properties.getProperty(key));
        }

        save();
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
                setProperties(URL, USER, PASSWORD);
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

    /**
     * Save to file
     */
    private static void save() {

        OutputStream out = null;

        try {
            out = new FileOutputStream("config.properties");
            properties.store(out, null);
            logger.info("Saving settings.");
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
     * @param host
     * @param port
     * @param path
     * @param embedded
     * @return url string
     */
    private static String getConnectionUrl(String host, String port, String path, boolean embedded) {

        String localPath = "";
        File file = new File(path);
        String fileName = file.getName();

        if (embedded) {
            fileName = fileName.contains(".") ? fileName.substring(0, fileName.indexOf(".")) : fileName;
            localPath = file.getParent() + File.separator + fileName;
        }

        StringBuilder sb = new StringBuilder();
        sb.append("jdbc:h2:");
        sb.append(embedded ? localPath : "tcp://" + host + (port.equals("default") ? "/" : ":" + port + "/") + path);
        //TODO add to setup dialog
        boolean exist = true;
        //The connection only succeeds when the database already exists
        sb.append(exist ? ";IFEXISTS=TRUE" : ";");

        return sb.toString();
    }

}
