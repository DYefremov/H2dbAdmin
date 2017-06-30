package by.post.ui;

import by.post.control.Context;
import by.post.control.Settings;
import by.post.control.ui.dialogs.SettingsDialogController;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * @author Dmitriy V.Yefremov
 */
public class SettingsDialog extends Dialog {

    private SettingsDialogController controller;

    private static final Logger logger = LogManager.getLogger(SettingsDialog.class);

    public SettingsDialog() throws IOException {
        init();
    }

    private void init() throws IOException {

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("dialogs/SettingsDialogPane.fxml"));
        loader.setResources(ResourceBundle.getBundle("bundles.Lang", Context.getLocale()));
        this.setDialogPane(loader.load());
        controller = loader.getController();

        Stage stage = (Stage)this.getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image(Resources.LOGO_PATH));
        //Consume apply button event if canceled in confirmation dialog
        final Button applyButton = (Button) getDialogPane().lookupButton(ButtonType.APPLY);
        applyButton.addEventFilter(ActionEvent.ACTION, event -> {
            Optional<ButtonType> result = new ConfirmationDialog().showAndWait();
            if (result.get() == ButtonType.OK) {
                controller.saveSettings();
            }
            event.consume();
        });
        //Setting translation
        boolean defLang = Context.getLocale().getLanguage().equals(Settings.DEFAULT_LANG);
        Button cancelButton = (Button) getDialogPane().lookupButton(ButtonType.CANCEL);
        applyButton.setText(defLang ? ButtonType.APPLY.getText() : "Применить");
        cancelButton.setText(defLang ? ButtonType.CANCEL.getText() : "Отмена");
    }
}