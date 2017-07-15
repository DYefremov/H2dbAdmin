package by.post.control.db;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.h2.tools.Recover;
import org.h2.tools.RunScript;

import java.io.File;
import java.nio.file.Path;
import java.sql.SQLException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * @author Dmitriy V.Yefremov
 */
public class RecoveryManager implements Recovery {

    private static final String RECOVERED_DB_NAME = "recovered";

    private static final Logger logger = LogManager.getLogger(RecoveryManager.class);

    private static final ExecutorService service = Executors.newSingleThreadExecutor();
    private Future future;

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
    private void recover(String dbPath, String dbName, String pathToSave, String user, String password) {

        try {
            // Dumps the contents of a database to a SQL script file.
            Recover.execute(dbPath, dbName);
            String scriptFile = dbPath + File.separator + dbName + ".h2.sql";
            String url = "jdbc:h2:" + pathToSave;
            // Executes the SQL commands in a script file.
            System.out.println("SCRIPT DONE");
            RunScript.execute(url, user, password, scriptFile, null, true);
            System.out.println("EXECUTE DONE");
        } catch (SQLException e) {
            logger.error("RecoveryManager error: " + e);
        }
    }

    /**
     * @param dbFile
     * @param saveDir
     * @param user
     * @param password
     */
    @Override
    public void recover(Path dbFile, Path saveDir, String user, String password) {

        final String dbPath = dbFile.getParent().toString();
        String name = String.valueOf(dbFile.getFileName());
        final String dbName = name.substring(0, name.indexOf('.'));
        final String savePath = saveDir + File.separator + RECOVERED_DB_NAME;

        Thread thread = new Thread(() -> recover(dbPath, dbName, savePath, user, password));
        thread.setDaemon(true);
        future = service.submit(thread);
    }

    @Override
    public void cancel() {
        future.cancel(true);
    }

    @Override
    public boolean isRunning() {
        return future != null && !future.isDone();
    }
}