package by.post.control.db;

import by.post.control.recovery.Recovery;
import by.post.control.recovery.RecoveryManager;
import org.junit.Test;

/**
 * @author Dmitriy V.Yefremov
 */
public class RecoveryManagerTest {
    @Test
    public void recover() throws Exception {
        String openPath = "~/test";
        String savePath = "~/recovered";

        Recovery recovery = new RecoveryManager();
    }

}