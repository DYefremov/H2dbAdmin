package by.post.control.db;

import javafx.util.Callback;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.h2.tools.Recover;

import java.nio.file.Path;
import java.sql.SQLException;

/**
 * @author Dmitriy V.Yefremov
 */
public class RecoveryManager implements Recovery {

    private boolean recoveryDone;

    private static final Logger logger = LogManager.getLogger(RecoveryManager.class);

    public RecoveryManager() {

    }

    private boolean recover(String dbPath, String dbName, String user, String password) {

        try {
            Recover.execute(dbPath, dbName);
            recoveryDone = true;
        } catch (SQLException e) {
            logger.error("RecoveryManager error: " + e);
            recoveryDone = false;
        }
//            String url = "jdbc:h2:" + openPath + "recovered";
//            String scriptFile = openPath + db + ".h2.sql";
//            RunScript.execute(url, user, password, scriptFile, null, true);

        return recoveryDone;
    }

    @Override
    public boolean recover(Path dbFile, Path saveDir, String user, String password, Callback<Boolean, Boolean> done) {

        String dbPath = dbFile.getParent().toString();
        String dbName =  String.valueOf(dbFile.getFileName());
        dbName = dbName.substring(0, dbName.indexOf('.'));

        return done.call(recover(dbPath, dbName, user, password));
    }
}
