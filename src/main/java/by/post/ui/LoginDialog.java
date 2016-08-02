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

/**
 * Custom dialog for entering login and password
 *
 * @author Dmitriy V.Yefremov
 */
public class LoginDialog extends Dialog<Pair<String, String>> {

    private FXMLLoader loader;
    private Parent parent;
    private LoginDialogController controller;

    public LoginDialog() throws Exception {
        init();
    }

    private void init() throws Exception {

        loader = new FXMLLoader(LoginDialog.class.getResource("LoginDialog.fxml"));
        parent = loader.<DialogPane>load();
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
