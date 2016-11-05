package by.post.control.ui;

import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

/**
 * @author Dmitriy V.Yefremov
 */
public class LoginDialogController {

    @FXML
    private TextField login;
    @FXML
    private PasswordField password;

    public LoginDialogController() {

    }

    public String getLogin() {
        return login.getText();
    }

    public String getPassword() {
        return password.getText();
    }
}
