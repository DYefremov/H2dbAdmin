package by.post.control;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.util.Map;
import java.util.Properties;

/**
 * @author Dmitriy V.Yefremov
 */
public class PropertiesController {

    private static Properties properties = new Properties();

    private static final Logger logger = LogManager.getLogger(PropertiesController.class);

    /**
     * @param settings
     */
    public static void setProperties(Map<String, String> settings) {

        settings.entrySet().forEach(e -> properties.setProperty(e.getKey(), e.getValue()));
        //Database settings for url
        String host = settings.get(Settings.HOST);
        String port = settings.get(Settings.PORT);
        String path = settings.get(Settings.PATH);
        String mode = settings.get(Settings.MODE);
        boolean embedded = mode != null ? mode.equals(Settings.EMBEDDED_MODE) : true;
        String url = getConnectionUrl(host, port, path, embedded);
        properties.setProperty(Settings.URL, url != null ? url : Settings.DEFAULT_URL);
        // Save to file
        save();
    }

    /**
     * @return properties for db connection
     */
    public static Properties getProperties() {

        if (!properties.isEmpty()) {
            return properties;
        }

        File file = new File("config.properties");

        if (!file.exists()) {
            initDefaultProperties();
        }

        try (InputStream in = new FileInputStream("config.properties")) {
            properties.load(in);
        } catch (IOException e) {
            logger.error("PropertiesController error: " + e);
        }

        return properties;
    }

    /**
     * Initialize default properties
     */
    private static void initDefaultProperties() {
        //Database settings
        properties.put(Settings.DRIVER, Settings.DEFAULT_DRIVER);
        properties.put(Settings.USER, Settings.DEFAULT_USER);
        properties.put(Settings.PASSWORD, Settings.DEFAULT_PASSWORD);
        properties.put(Settings.HOST, Settings.DEFAULT_HOST);
        properties.put(Settings.PATH, Settings.DEFAULT_PATH);
        properties.put(Settings.URL, Settings.DEFAULT_URL);
        properties.put(Settings.MODE, Settings.EMBEDDED_MODE);
        properties.put(Settings.EXIST, String.valueOf(true));
        //Ui settings
        properties.put(Settings.SHOW_PROMPT_IF_EXIT, String.valueOf(true));
        properties.put(Settings.LANG, Settings.DEFAULT_LANG);
        // Save to file
        save();
    }

    /**
     * Save to file
     */
    private static void save() {

        try (OutputStream out = new FileOutputStream("config.properties")) {
            properties.store(out, null);
            logger.info("Saving settings.");
        } catch (IOException e) {
            logger.error("PropertiesController error[save]: " + e);
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

        if (host == null || path == null) {
            return Settings.DEFAULT_URL;
        }

        String localPath = "";
        File file = new File(path);
        String fileName = file.getName();

        if (embedded) {
            fileName = fileName.contains(".") ? fileName.substring(0, fileName.indexOf(".")) : fileName;
            localPath = file.getParent() + File.separator + fileName;
        }

        StringBuilder sb = new StringBuilder();
        sb.append("jdbc:h2:");

        boolean defaultPort = port == null || port.equals("default") || port.equals("");
        sb.append(embedded ? localPath : "tcp://" + host + (defaultPort ? "/" : ":" + port + "/") + path);

        String ex = properties.getProperty(Settings.EXIST);
        boolean exist = ex != null ? Boolean.valueOf(ex) : true;
        //The connection only succeeds when the database already exists
        sb.append(exist ? Settings.DEFAULT_EXIST : ";");

        return sb.toString();
    }

}
