package by.post.ui;

import by.post.control.Context;
import by.post.control.Settings;
import by.post.control.ui.DatabaseDialogController;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * @author Dmitriy V.Yefremov
 */
public class DatabaseDialog extends Dialog<Map<String, String>> {

    private DatabaseDialogController controller;

    private static final Logger logger = LogManager.getLogger(DatabaseDialog.class);

    public DatabaseDialog() {
        init();
    }

    private void init() {

        try {
            FXMLLoader loader = new FXMLLoader(DatabaseDialog.class.getResource("DatabaseDialogPane.fxml"));
            loader.setResources(ResourceBundle.getBundle("bundles.Lang", Context.getLocale()));
            this.setDialogPane(loader.load());
            controller = loader.getController();
        } catch (IOException e) {
            logger.error("DatabaseDialog error[init]: " + e);
        }

        this.setResultConverter(dialogButton -> {
            ButtonBar.ButtonData data = dialogButton == null ? null : dialogButton.getButtonData();
            return data == ButtonBar.ButtonData.OK_DONE ? controller.getSettings() : null;
        });

        Stage stage = (Stage) getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image(Resources.LOGO_PATH));

        //Setting translation
        boolean defLang = Context.getLocale().getLanguage().equals(Settings.DEFAULT_LANG);
        Button okButton = (Button) getDialogPane().lookupButton(ButtonType.OK);
        Button cancelButton = (Button) getDialogPane().lookupButton(ButtonType.CANCEL);
        okButton.setText(defLang ? ButtonType.OK.getText() : "Создать");
        cancelButton.setText(defLang ? ButtonType.CANCEL.getText() : "Отмена");
    }
}
