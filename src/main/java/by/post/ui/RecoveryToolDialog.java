package by.post.ui;

import by.post.control.Context;
import by.post.control.Settings;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ResourceBundle;

/**
 * @author Dmitriy V.Yefremov
 */
public class RecoveryToolDialog extends Dialog {

    public RecoveryToolDialog() throws IOException {
        init();
    }

    private void init() throws IOException {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("dialogs/RecoveryDialogPane.fxml"));
        loader.setResources(ResourceBundle.getBundle("bundles.Lang", Context.getLocale()));
        this.setDialogPane(loader.load());
        Stage stage = (Stage)this.getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image(Resources.LOGO_PATH));
        //Setting translation
        boolean defLang = Context.getLocale().getLanguage().equals(Settings.DEFAULT_LANG);
        Button cancelButton = (Button) getDialogPane().lookupButton(ButtonType.CANCEL);
        cancelButton.setText(defLang ? ButtonType.CANCEL.getText() : "Отмена");
    }
}
