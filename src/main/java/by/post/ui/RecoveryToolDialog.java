package by.post.ui;

import javafx.fxml.FXMLLoader;
import javafx.scene.control.Dialog;
import javafx.stage.StageStyle;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

/**
 * @author Dmitriy V.Yefremov
 */
public class RecoveryToolDialog extends Dialog {

    private static final Logger logger = LogManager.getLogger(RecoveryToolDialog.class);

    public RecoveryToolDialog() {
        init();
    }

    private void init() {
        try {
            this.setDialogPane(FXMLLoader.load(RecoveryToolDialog.class.getResource("RecoveryPane.fxml")));
            this.setHeaderText("Recovery tool");
            this.initStyle(StageStyle.UTILITY);
        } catch (IOException e) {
            logger.error("RecoveryToolDialog error[init]: " + e);
        }
    }

}
