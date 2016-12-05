package by.post.ui;

import javafx.fxml.FXMLLoader;
import javafx.scene.control.Dialog;
import javafx.scene.image.Image;
import javafx.stage.Stage;
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
            Stage stage = (Stage)this.getDialogPane().getScene().getWindow();
            stage.getIcons().add(new Image(Resources.LOGO_PATH));
        } catch (IOException e) {
            logger.error("RecoveryToolDialog error[init]: " + e);
        }
    }
}
