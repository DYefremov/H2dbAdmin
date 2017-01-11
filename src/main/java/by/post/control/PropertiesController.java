package by.post.control;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.util.HashMap;
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

        Map<String, String> st = settings;
        //Database settings
        String user =  st.get(Settings.USER);
        String password = st.get(Settings.PASSWORD);
        String host = st.get(Settings.HOST);
        String port = st.get(Settings.PORT);
        String path = st.get(Settings.PATH);
        String mode = st.get(Settings.MODE);
        String exist = st.get(Settings.EXIST);
        String driver = st.get(Settings.DRIVER);

        boolean embedded = mode != null ? mode.equals(Settings.EMBEDDED_MODE) : true;
        String url = getConnectionUrl(host, port, path, embedded);

        properties.put(Settings.DRIVER, driver != null ? driver : Settings.DEFAULT_DRIVER);
        properties.put(Settings.USER, user != null ? user : Settings.DEFAULT_USER);
        properties.put(Settings.PASSWORD, password != null ?password : Settings.DEFAULT_PASSWORD);
        properties.put(Settings.HOST, host != null ? host : Settings.DEFAULT_HOST);
        properties.put(Settings.PATH, path != null ? path : Settings.DEFAULT_PATH);
        properties.put(Settings.URL, url != null ? url : Settings.DEFAULT_URL);
        properties.put(Settings.MODE, mode != null ? mode : Settings.EMBEDDED_MODE);
        properties.put(Settings.EXIST, exist != null ? exist : String.valueOf(true));
        //Ui settings
        String promptIfExit = st.get(Settings.SHOW_PROMPT_IF_EXIT);
        properties.put(Settings.SHOW_PROMPT_IF_EXIT, promptIfExit != null ? promptIfExit : String.valueOf(true));
        String defaultLang = settings.get(Settings.LANG);
        boolean defLang = defaultLang == null || defaultLang.equals(Settings.DEFAULT_LANG);
        properties.put(Settings.LANG, defLang ? Settings.DEFAULT_LANG : Settings.SECOND_LANG);
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
            setProperties(new HashMap<>());
        }

        try (InputStream in = new FileInputStream("config.properties")) {
            properties.load(in);
        } catch (IOException e) {
            logger.error("PropertiesController error: " + e);
        }

        return properties;
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
