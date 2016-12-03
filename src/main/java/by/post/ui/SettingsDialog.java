package by.post.ui;

import javafx.fxml.FXMLLoader;
import javafx.scene.control.Dialog;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

/**
 * @author Dmitriy V.Yefremov
 */
public class SettingsDialog extends Dialog {

    private static final Logger logger = LogManager.getLogger(SettingsDialog.class);

    public SettingsDialog() {
        init();
    }

    private void init() {
        try {
            this.setDialogPane(FXMLLoader.load(SettingsDialog.class.getResource("SettingsDialogPane.fxml")));
            this.initStyle(StageStyle.UTILITY);
        } catch (IOException e) {
            logger.error("RecoveryToolDialog error[init]: " + e);
        }
    }

}
