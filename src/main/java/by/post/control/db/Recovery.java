package by.post.control.db;

import java.nio.file.Path;

/**
 * @author Dmitriy V.Yefremov
 */
public interface Recovery {

   void recover(Path dbFile, Path saveDir, String user, String password);

   void cancel();

   boolean isRunning();

}
