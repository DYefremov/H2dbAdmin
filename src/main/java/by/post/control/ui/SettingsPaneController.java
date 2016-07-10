package by.post.control.ui;

import by.post.control.PropertiesController;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

import java.util.Arrays;
import java.util.Properties;


/**
 * @author Dmitriy V.Yefremov
 */
public class SettingsPaneController {

    @FXML
    private TextField path;
    @FXML
    private TextField login;
    @FXML
    private TextField password;
    @FXML
    private CheckBox showPassword;
    @FXML
    private ComboBox driver;

    public SettingsPaneController() {

    }

    @FXML
    public void onSaveAction() {
        System.out.println("save");
    }

    @FXML
    public void onCancelAction() {
        System.out.println("Cancel");
    }

    @FXML
    public void onShowChange() {
        System.out.println(password.getText());
//        password.getStyleClass().add("password-field");
//        password.setAccessibleRole(AccessibleRole.PASSWORD_FIELD);
    }

    @FXML
    private void initialize() {

        Properties properties = PropertiesController.getProperties();
        path.setText(properties.getProperty("path"));
        login.setText(properties.getProperty("user"));
        password.setText(properties.getProperty("password"));
        showPassword.setSelected(true);

        driver.setItems(FXCollections.observableArrayList(Arrays.asList("H2", "MySQL")));
        driver.getSelectionModel().selectFirst();
    }
}
