package by.post.ui;

import by.post.control.ui.LoginDialogController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.util.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

/**
 * Custom dialog for entering login and password
 *
 * @author Dmitriy V.Yefremov
 */
public class LoginDialog extends Dialog<Pair<String, String>> {

    private FXMLLoader loader;
    private Parent parent;
    private LoginDialogController controller;

    private static final Logger logger = LogManager.getLogger(LoginDialog.class);

    public LoginDialog() {
        init();
    }

    private void init() {

        loader = new FXMLLoader(LoginDialog.class.getResource("LoginDialog.fxml"));
        try {
            parent = loader.<DialogPane>load();
        } catch (IOException e) {
           logger.error("LoginDialog error: " + e);
        }
        controller = loader.getController();

        this.setDialogPane((DialogPane) parent);
        ((DialogPane) parent).getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        setResultConverter((dialogButton) -> {
            String login = controller.getLogin();
            String password = controller.getPassword();
            ButtonBar.ButtonData data = dialogButton == null ? null : dialogButton.getButtonData();
            return data == ButtonBar.ButtonData.OK_DONE ? new Pair(login, password) : null;
        });

        Stage stage = (Stage) getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image(Resources.LOGO_PATH));
    }

}
