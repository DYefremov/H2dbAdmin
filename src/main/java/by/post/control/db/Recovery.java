package by.post.control.db;

import javafx.util.Callback;

import java.nio.file.Path;

/**
 * @author Dmitriy V.Yefremov
 */
public interface Recovery {

    boolean recover(Path dbFile, Path saveDir, String user, String password, Callback<Boolean, Boolean> done);
}
