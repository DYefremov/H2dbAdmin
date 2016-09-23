package by.post.control.ui;

import by.post.control.PropertiesController;
import by.post.ui.ConfirmationDialog;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.util.Arrays;
import java.util.Optional;
import java.util.Properties;

/**
 * @author Dmitriy V.Yefremov
 */
public class DbSettingsController {

    @FXML
    private TextField path;
    @FXML
    private TextField dbName;
    @FXML
    private TextField login;
    @FXML
    private TextField password;
    @FXML
    private PasswordField maskedPassword;
    @FXML
    private CheckBox showPassword;
    @FXML
    private ComboBox driver;

    private Properties properties;

    public DbSettingsController() {

    }

    @FXML
    public void onShowChange() {
        passwordFieldsReverse();
    }

    @FXML
    public void onSaveAction() {

        Optional result = new ConfirmationDialog("").showAndWait();

        if (result.get() == ButtonType.OK && properties != null) {
            properties.setProperty("path", path.getText());
            properties.setProperty("user", login.getText());
            properties.setProperty("password", showPassword.isSelected() ? password.getText() : maskedPassword.getText());
            properties.setProperty("driver", driver.getSelectionModel().getSelectedItem().toString());

            PropertiesController.setProperties(properties);
        }
    }

    /**
     * Reverse fields for password
     */
    private void passwordFieldsReverse() {

        if (showPassword.isSelected()) {
            password.setText(maskedPassword.getText());
        } else {
            maskedPassword.setText(password.getText());
        }

        password.managedProperty().bind(showPassword.selectedProperty());
        password.visibleProperty().bind(showPassword.selectedProperty());
        maskedPassword.managedProperty().bind(showPassword.selectedProperty().not());
        maskedPassword.visibleProperty().bind(showPassword.selectedProperty().not());
    }

    @FXML
    private void initialize() {

        properties = PropertiesController.getProperties();
        path.setText(properties.getProperty("path"));
        dbName.setText(properties.getProperty("db"));
        login.setText(properties.getProperty("user"));
        password.setText(properties.getProperty("password"));

        passwordFieldsReverse();

        driver.setItems(FXCollections.observableArrayList(Arrays.asList("org.h2.Driver", "com.mysql.jdbc.Driver")));
        driver.getSelectionModel().selectFirst();
    }
}
