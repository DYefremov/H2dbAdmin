package by.post.ui;

import by.post.control.Context;
import by.post.control.ui.dialogs.UsersDialogController;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Dialog;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ResourceBundle;

/**
 * @author Dmitriy V.Yefremov
 */
public class UsersDialog extends Dialog {

    private UsersDialogController controller;

    public UsersDialog() {
        init();
    }

    private void init() {

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("dialogs/UsersDialogPane.fxml"));
        loader.setResources(ResourceBundle.getBundle("bundles.Lang", Context.getLocale()));
        try {
            this.setDialogPane(loader.load());
        } catch (IOException e) {
            e.printStackTrace();
        }
        controller = loader.getController();
        Stage stage = (Stage)this.getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image(Resources.LOGO_PATH));

    }
}
