package by.post.control.recovery;

/**
 * @author Dmitriy V.Yefremov
 */
public interface Recover {

    /**
     * Start service and process new task
     *
     * @param dir
     * @param db
     */
    void process(String dir, String db);

    /**
     * Cancel current task
     */
    void cancel();

    /**
     * @return true if task is running
     */
    boolean isRunning();
}
