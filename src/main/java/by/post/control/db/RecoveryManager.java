package by.post.control.db;

import javafx.util.Callback;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.h2.tools.Recover;
import org.h2.tools.RunScript;

import java.io.File;

/**
 * @author Dmitriy V.Yefremov
 */
public class RecoveryManager implements Recovery {

    private String openPath;
    private String savePath;
    private String db;
    private String user;
    private String password;

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
     * @param done
     * @return
     */
    @Override
    public boolean recover(String openPath, String savePath, Callback<Boolean, Boolean> done) {
        this.openPath = openPath;
        this.savePath = savePath;
        return done.call( recover());
    }

    /**
     * @param openFile
     * @param saveDir
     * @param done
     * @return
     */
    @Override
    public boolean recover(File openFile, File saveDir, Callback<Boolean, Boolean> done) {
        logger.info("Starting recovery...");
        db = openFile.getName();
        db = db.substring(0, db.indexOf("."));
        openPath = openFile.getParent() + File.separator;
        System.out.println(openPath);
//        savePath = saveDir.getPath();
        return done.call( recover());
    }

    private boolean recover() {
        try {
            Recover.execute(openPath, db);
            String url = "jdbc:h2:" + openPath + "recovered";
            String scriptFile = openPath + db + ".h2.sql";
//            RunScript.execute(url, user, password, scriptFile, null, true);
        } catch (Exception e) {
            logger.error("RecoveryManager error: " + e);
            return false;
        }
        return true;
    }
}
