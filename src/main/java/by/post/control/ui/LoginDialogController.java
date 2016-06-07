package by.post.control.ui;

import by.post.ui.LoginDialog;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.util.Pair;

/**
 * @author Dmitriy V.Yefremov
 */
public class LoginDialogController {

    @FXML
    private TextField loginField;
    @FXML
    private PasswordField passwordField;

    private LoginDialog loginDialog;

    public LoginDialogController() {

    }

    /**
     * @param loginDialog
     */
    public void setLoginDialog(LoginDialog loginDialog) {
        this.loginDialog = loginDialog;
    }

    @FXML
    public void onOkButton(ActionEvent event){
//        new Alert(Alert.AlertType.CONFIRMATION, loginField.getText() + " " + passwordField).show();

    }

    public void onCancelButton(ActionEvent event) {

    }

    @FXML
    private void initialize() {

    }


}
