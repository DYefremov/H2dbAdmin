package by.post.ui;

import by.post.control.Context;
import by.post.control.Settings;
import by.post.ui.Resources;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.stage.Stage;

/**
 * @author Dmitriy V.Yefremov
 */
public class ConfirmationDialog extends Alert {

    public ConfirmationDialog() {
        super(AlertType.CONFIRMATION);
        init("");
    }

    public ConfirmationDialog(String contentText) {
        super(AlertType.CONFIRMATION);
        init(contentText);
    }

    public ConfirmationDialog(String contentText, AlertType type) {
        super(type == null ? AlertType.CONFIRMATION : type);
        init(contentText);
    }

    private void init(String contextText) {
        setContentText(contextText);
        setTitle("H2dbAdmin");
        Stage stage = (Stage) getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image(Resources.LOGO_PATH));

        boolean defLang = Context.getLocale().getLanguage().equals(Settings.DEFAULT_LANG);
        //Setting translation
        setHeaderText(defLang ? "Are you sure?" : "Вы уверены?");
        Button okButton = (Button) getDialogPane().lookupButton(ButtonType.OK);
        Button cancelButton = (Button) getDialogPane().lookupButton(ButtonType.CANCEL);
        okButton.setText(defLang ? ButtonType.OK.getText() : "Да");
        cancelButton.setText(defLang ? ButtonType.CANCEL.getText() : "Отмена");
    }
}
