package by.post.control.ui;

import by.post.control.PropertiesController;
import by.post.control.Settings;
import by.post.ui.ConfirmationDialog;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.util.*;

/**
 * @author Dmitriy V.Yefremov
 */
public class DbSettingsController {

    @FXML
    private TextField path;
    @FXML
    private TextField host;
    @FXML
    private TextField port;
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
    @FXML
    ChoiceBox mode;

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
            Map<String, String> settings = new HashMap<>();
            settings.put(Settings.PATH, path.getText());
            settings.put(Settings.PORT, port.getText());
            settings.put(Settings.USER, login.getText());
            settings.put(Settings.PASSWORD, showPassword.isSelected() ? password.getText() : maskedPassword.getText());
            settings.put(Settings.DRIVER, driver.getSelectionModel().getSelectedItem().toString());

            PropertiesController.setProperties(settings);
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

        host.setText(properties.getProperty(Settings.HOST));
        path.setText(properties.getProperty(Settings.PATH));
        login.setText(properties.getProperty(Settings.USER));
        password.setText(properties.getProperty(Settings.PASSWORD));

        String mod = properties.getProperty(Settings.MODE);
        boolean embedded = Settings.EMBEDDED_MODE.equals(mod != null ? mod : Settings.EMBEDDED_MODE);
        mode.getSelectionModel().select(embedded ? 1 : 0);

        passwordFieldsReverse();

        driver.setItems(FXCollections.observableArrayList(Arrays.asList("org.h2.Driver", "com.mysql.jdbc.Driver")));
        driver.getSelectionModel().selectFirst();
    }
}
