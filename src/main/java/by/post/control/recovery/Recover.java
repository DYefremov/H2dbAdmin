package by.post.control.recovery;

/**
 * @author Dmitriy V.Yefremov
 */
public interface Recover {

    void process(String dir, String db);

    void cancel();

    boolean isRunning();
}
