package by.post.ui;

/**
 * Resources constants
 *
 * @author Dmitriy V.Yefremov
 */
public class Resources {
    // Path for main logo image
    public static final String LOGO_PATH ="/img/logo.jpg";
    // Title for window
    public static final String TITLE = "H2dbAdmin";
    // Min height of app window
    public static final double  MIN_HEIGHT = 510;
    // Min width of app window
    public static final double MIN_WIDTH = 700;
    // Program version
    public final static String PROGRAM_VERSION = "0.0.1 Pre-alpha";
    // Text about program
    public final static String ABOUT_TEXT = "This program is free software and " +
            "distributed in the hope that it will be useful, but WITHOUT ANY " +
            "WARRANTY! \n\n";

    public final static String CREATOR = "Dmitriy Yefremov  2016";

    public final static String USED_RESOURCES = "Used components: \t\t\t\t\t\t\t\t\n" +
            "H2 Database Engine - http://www.h2database.com\n" +
            "Apache Log4j 2 - http://logging.apache.org/log4j/2.x/\n" +
            "All icons are taken from the standard Linux Mint icon sets  or taken away from http://icons8.com\n" +
            "To building the project uses Gradle - https://gradle.org\n\n";

    public final static String JAVA_VERSION = "OS: " + System.getProperty("os.name") +  ". Java version: "
            + System.getProperty("java.version") + " from " + System.getProperty("java.vendor");
}
