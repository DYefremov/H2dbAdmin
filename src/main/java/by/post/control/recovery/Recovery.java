package by.post.control.recovery;

import java.nio.file.Path;

/**
 * @author Dmitriy V.Yefremov
 */
public interface Recovery {

    /**
     * Start service and process new task
     *
     * @param dbFile
     * @param saveDir
     * @param user
     * @param password
     */
    void recover(Path dbFile, Path saveDir, String user, String password);

    /**
     * Cancel current task
     */
    void cancel();

    /**
     * Full service shutdown
     */
    void shutdown();

    /**
     * @return true if eny task is running
     */
    boolean isRunning();

}
