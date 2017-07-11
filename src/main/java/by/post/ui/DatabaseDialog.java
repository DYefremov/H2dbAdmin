package by.post.ui;

import by.post.control.Context;
import by.post.control.ui.dialogs.DatabaseDialogController;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.Dialog;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * @author Dmitriy V.Yefremov
 */
public class DatabaseDialog extends Dialog<Map<String, String>> {

    private DatabaseDialogController controller;

    public DatabaseDialog() throws IOException {
        init();
    }

    private void init() throws IOException {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("dialogs/DatabaseDialogPane.fxml"));
        loader.setResources(ResourceBundle.getBundle("bundles.Lang", Context.getLocale()));
        this.setDialogPane(loader.load());
        controller = loader.getController();

        this.setResultConverter(dialogButton -> {
            ButtonBar.ButtonData data = dialogButton == null ? null : dialogButton.getButtonData();
            return data == ButtonBar.ButtonData.OK_DONE ? controller.getSettings() : null;
        });

        Stage stage = (Stage) getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image(Resources.LOGO_PATH));
    }
}
