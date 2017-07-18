package by.post.control.recovery;

/**
 * @author Dmitriy V.Yefremov
 */
public interface Recover {

    /**
     * Start service and process new task
     *
     * @param dbPath
     * @param dbName
     * @param pathToSave
     * @param user
     * @param password
     */
    void process(String dbPath,String dbName, String pathToSave, String user, String password);

    /**
     * Cancel current task
     */
    void cancel();

    /**
     * @return true if task is running
     */
    boolean isRunning();
}
