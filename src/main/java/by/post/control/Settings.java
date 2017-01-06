package by.post.control;

/**
 * Constants of settings names
 *
 * @author Dmitriy V.Yefremov
 */
public class Settings {
    /**
     * Database settings
     */
    public static final String HOST = "host";
    public static final String PORT = "port";
    public static final String URL = "url";
    public static final String PATH = "path";
    public static final String USER = "user";
    public static final String PASSWORD = "password";
    public static final String DRIVER = "driver";
    public static final String MODE = "mode";
    public static final String EMBEDDED_MODE = "embedded";
    public static final String SERVER_MODE = "server";
    //The connection only succeeds when the database already exists ";IFEXISTS=TRUE"
    public static final String EXIST = "ifexist";
    //Default values
    public static final String DEFAULT_USER = "sa";
    public static final String DEFAULT_PASSWORD = "";
    public static final String DEFAULT_PATH = "~/";
    public static final String DEFAULT_HOST = "localhost";
    public static final String DEFAULT_DRIVER = "org.h2.Driver";
    public static final String DEFAULT_URL = "jdbc:h2:file:~/test;IFEXISTS=TRUE";
    public static final String DEFAULT_EXIST = ";IFEXISTS=TRUE";
    public static final String DEFAULT_PORT = "8082";
    /**
     * Ui settings
     */
    public static final String SHOW_PROMPT_IF_EXIT = "guiShowPromptIfExit";

}
