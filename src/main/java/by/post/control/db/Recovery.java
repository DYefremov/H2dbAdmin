package by.post.control.db;

import javafx.util.Callback;

import java.io.File;

/**
 * @author Dmitriy V.Yefremov
 */
public interface Recovery {

    boolean recover(String openPath, String savePath, Callback<Boolean, Boolean> done);

    boolean recover(File openFile, File saveDir, Callback<Boolean, Boolean> done);
}
