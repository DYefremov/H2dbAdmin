package by.post.control.db;

import by.post.control.PropertiesController;
import by.post.control.Settings;
import java.sql.SQLException;
import java.util.Map;

/**
 * @author Dmitriy V.Yefremov
 */
public class DatabaseManager {

    private DbControl dbControl;

    private static DatabaseManager instance = new DatabaseManager();

    private DatabaseManager() {
        dbControl = DbController.getInstance();
    }

    public static DatabaseManager getInstance() {
        return instance;
    }

    /**
     * Add new database
     *
     * @param settings
     */
    public void addDatabase(Map<String, String> settings) {

        String user = settings.get(Settings.USER);
        String password = settings.get(Settings.PASSWORD);
        String url = getUrl(settings);

        dbControl.getConnection(url, user, password);
        PropertiesController.setProperties(settings);
    }

    /**
     * Drop database
     */
    public void deleteDatabase(boolean drop) throws SQLException {
        dbControl.update(Queries.dropDatabase(drop));
        dbControl.closeConnection();
    }

    /**
     *Constructing url from settings map
     *
     * @param settings
     * @return
     */
    private String getUrl(Map<String, String> settings) {

        String port = settings.get(Settings.PORT);
        boolean serverMode = Settings.SERVER_MODE.equals(settings.get(Settings.MODE));
        boolean portIsChanged = port != null && !port.equals("") && !port.equals("default");

        StringBuilder sb = new StringBuilder("jdbc:h2:");
        sb.append(serverMode ? "tcp://" + settings.get(Settings.HOST) + "/" : "" );
        sb.append(serverMode && portIsChanged ? ":" + port : "");
        sb.append(settings.get(Settings.PATH));

        return sb.toString();
    }
}
