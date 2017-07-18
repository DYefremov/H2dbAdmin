package by.post.control.recovery;

import java.io.File;
import java.nio.file.Path;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * @author Dmitriy V.Yefremov
 */
public class RecoveryManager implements Recovery {

    private Future future;
    private Recover recover;

    private static final String RECOVERED_DB_NAME = "recovered";
    private static final ExecutorService service = Executors.newSingleThreadExecutor();

    public RecoveryManager() {
        recover = new H2Recover();
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

        Thread thread = new Thread(() -> recover.process(dbPath, dbName, savePath, user, password));
        thread.setDaemon(true);
        future = service.submit(thread);
    }

    @Override
    public void cancel() {
        recover.cancel();
        future.cancel(true);
    }

    @Override
    public void shutdown() {
        cancel();
        service.shutdownNow();
    }

    @Override
    public boolean isRunning() {
        return future != null && !future.isDone();
    }

}