package by.post.control.db;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author Dmitriy V.Yefremov
 */
public class RecoveryManagerTest {
    @Test
    public void recover() throws Exception {
        String openPath = "~/test";
        String savePath = "~/recovered";

        Recovery recovery = new RecoveryManager(openPath, savePath);
        recovery.recover();
    }

}