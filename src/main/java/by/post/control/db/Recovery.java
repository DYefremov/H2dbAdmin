package by.post.control.db;

import java.io.File;

/**
 * @author Dmitriy V.Yefremov
 */
public interface Recovery {

    void recover(String openPath, String savePath);

    void recover(File openFile, File saveDir);
}
