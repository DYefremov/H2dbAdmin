package by.post.ui;

import by.post.control.Context;
import by.post.control.Settings;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.ResourceBundle;

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
            FXMLLoader loader = new FXMLLoader(RecoveryToolDialog.class.getResource("RecoveryPane.fxml"));
            loader.setResources(ResourceBundle.getBundle("bundles.Lang", Context.getLocale()));
            this.setDialogPane(loader.load());
            Stage stage = (Stage)this.getDialogPane().getScene().getWindow();
            stage.getIcons().add(new Image(Resources.LOGO_PATH));

            //Setting translation
            boolean defLang = Context.getLocale().getLanguage().equals(Settings.DEFAULT_LANG);
            Button cancelButton = (Button) getDialogPane().lookupButton(ButtonType.CANCEL);
            cancelButton.setText(defLang ? ButtonType.CANCEL.getText() : "Отмена");
        } catch (IOException e) {
            logger.error("RecoveryToolDialog error[init]: " + e);
        }
    }
}
