package by.post.ui;

import by.post.control.Context;
import by.post.control.Settings;
import by.post.control.ui.OpenDbDialogController;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * @author Dmitriy V.Yefremov
 */
public class OpenDbDialog extends Dialog<Map<String, String>> {

    private FXMLLoader loader;
    private Parent parent;
    private OpenDbDialogController controller;

    private static final Logger logger = LogManager.getLogger(OpenDbDialog.class);

    public OpenDbDialog() {
        init();
    }

    private void init() {

        loader = new FXMLLoader(OpenDbDialog.class.getResource("OpenDbDialog.fxml"));
        loader.setResources(ResourceBundle.getBundle("bundles.Lang", Context.getLocale()));

        try {
            parent = loader.<DialogPane>load();
        } catch (IOException e) {
            logger.error("OpenDbDialog error: " + e);
        }

        controller = loader.getController();

        this.setDialogPane((DialogPane) parent);
        this.setTitle("Setup connection...");

        Stage stage = (Stage) getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image(Resources.LOGO_PATH));

        initButtons();
    }

    private void initButtons() {
        //Consume ok button event if canceled in confirmation dialog
        final Button okButton = (Button) getDialogPane().lookupButton(ButtonType.OK);
        okButton.addEventFilter(ActionEvent.ACTION, event -> {
            Optional<ButtonType> result = new ConfirmationDialog().showAndWait();
            if (result.get() != ButtonType.OK) {
                event.consume();
            }
        });

        setResultConverter((dialogButton) -> {
            ButtonBar.ButtonData data = dialogButton == null ? null : dialogButton.getButtonData();
            return data == ButtonBar.ButtonData.OK_DONE ? controller.getSettings() : null;
        });
        //Setting translation for cancel button
        boolean defLang = Context.getLocale().getLanguage().equals(Settings.DEFAULT_LANG);
        Button cancelButton = (Button) getDialogPane().lookupButton(ButtonType.CANCEL);
        cancelButton.setText(defLang ? ButtonType.CANCEL.getText() : "Отмена");
    }

}
