package by.post.control.db;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;

/**
 * @author Dmitriy V.Yefremov
 */
public class RecoveryManager implements Recovery {

    private String openPath;
    private String savePath;
    private File openFile;
    private File saveDir;

    private static final Logger logger = LogManager.getLogger(RecoveryManager.class);

    public RecoveryManager() {

    }

    public RecoveryManager(String openPath, String savePath) {
        this.openPath = openPath;
        this.savePath = savePath;
    }

    /**
     * @param openPath
     * @param savePath
     */
    @Override
    public void recover(String openPath, String savePath) {
        this.openPath = openPath;
        this.savePath = savePath;
        recover();
    }

    /**
     * @param openFile
     * @param saveDir
     */
    @Override
    public void recover(File openFile, File saveDir) {
        logger.info("Starting recovery...");
        this.openFile = openFile;
        this.saveDir = saveDir;
        recover();
    }

    private void recover() {
        logger.info("Starting recovery...");
    }
}
