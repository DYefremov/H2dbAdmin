package by.post.control.ui.dialogs;

import by.post.data.User;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;


/**
 * @author Dmitriy V.Yefremov
 */
public class UsersDialogController {

    @FXML
    private TextField userField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private CheckBox adminCheckBox;

    public UsersDialogController() {

    }

    public User getUser() {
        return new User(userField.getText(), passwordField.getText(), "", adminCheckBox.isSelected());
    }

    @FXML
    private void onNameKeyReleased() {
        String value = userField.getText();
        //For compatibility, only unquoted or uppercase user names are allowed!
        userField.setText(value.toUpperCase());
        userField.positionCaret(value.length());
    }

}
