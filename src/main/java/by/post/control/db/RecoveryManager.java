package by.post.control.db;

import javafx.util.Callback;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.h2.tools.Recover;
import org.h2.tools.RunScript;

import java.io.File;
import java.nio.file.Path;
import java.sql.SQLException;

/**
 * @author Dmitriy V.Yefremov
 */
public class RecoveryManager implements Recovery {

    private boolean recoveryDone;

    private final String RECOVERED_DB_NAME = "recovered";

    private static final Logger logger = LogManager.getLogger(RecoveryManager.class);

    public RecoveryManager() {

    }

    /**
     * @param dbPath
     * @param dbName
     * @param pathToSave
     * @param user
     * @param password
     * @return
     */
    private boolean recover(String dbPath, String dbName, String pathToSave, String user, String password) {

        try {
            // Dumps the contents of a database to a SQL script file.
            Recover.execute(dbPath, dbName);
            String scriptFile = dbPath + File.separator + dbName + ".h2.sql";
            String url = "jdbc:h2:" + pathToSave;
            // Executes the SQL commands in a script file.
            RunScript.execute(url, user, password, scriptFile, null, true);
            recoveryDone = true;
        } catch (SQLException e) {
            logger.error("RecoveryManager error: " + e);
            recoveryDone = false;
        }

        return recoveryDone;
    }

    /**
     * @param dbFile
     * @param saveDir
     * @param user
     * @param password
     * @param done
     * @return true if done
     */
    @Override
    public boolean recover(Path dbFile, Path saveDir, String user, String password, Callback<Boolean, Boolean> done) {

        String dbPath = dbFile.getParent().toString();
        String dbName =  String.valueOf(dbFile.getFileName());
        dbName = dbName.substring(0, dbName.indexOf('.'));

        String savePath = saveDir + File.separator + RECOVERED_DB_NAME;

        return done.call(recover(dbPath, dbName, savePath, user, password));
    }
}
