package by.post.control.ui;

import by.post.control.PropertiesController;
import by.post.control.Settings;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * @author Dmitriy V.Yefremov
 */
public class SettingsDialogController {
    //Database settings
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
    @FXML
    VBox dbSettingsPane;
    //Ui settings
    @FXML
    VBox uiSettingsPane;
    @FXML
    CheckBox promptClosingProgram;
    //Main elements
    @FXML
    StackPane stackPane;
    @FXML
    private ListView<String> settingsList;

    private Properties properties;

    private static final Logger logger = LogManager.getLogger(SettingsDialogController.class);

    public SettingsDialogController() {

    }

    /**
     * Navigation in settings list by mouse
     */
    @FXML
    public void onSettingsMouseClicked() {
        selectSettingsItem();
    }

    /**
     * Navigation in settings list by keyboard
     */
    public void onSettingsKeyReleased() {
        selectSettingsItem();
    }

    @FXML
    public void onOpenDbFile() {

        File dbFile = new OpenFileDialogProvider().getFileDialog("", true, false);

        if (dbFile != null) {
            path.setText(dbFile.getPath());
        }
    }

    /**
     * Double click on host field
     *
     * @param event
     */
    @FXML
    public void onPathClicked(MouseEvent event) {

        if (event.getClickCount() == 2) {
            onOpenDbFile();
        }
    }

    /**
     *
     */
    public void saveSettings() {

        if (properties != null) {
            Map<String, String> settings = new HashMap<>();
            //Database
            settings.put(Settings.HOST, host.getText());
            settings.put(Settings.PATH, path.getText());
            settings.put(Settings.PORT, port.getText());
            settings.put(Settings.USER, login.getText());
            settings.put(Settings.PASSWORD, showPassword.isSelected() ? password.getText() : maskedPassword.getText());
            settings.put(Settings.DRIVER, driver.getSelectionModel().getSelectedItem().toString());
            settings.put(Settings.MODE, mode.getSelectionModel().getSelectedIndex() == 0 ? Settings.SERVER_MODE : Settings.EMBEDDED_MODE);
            //Ui
            settings.put(Settings.SHOW_PROMPT_IF_EXIT, String.valueOf(promptClosingProgram.isSelected()));

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

        if (properties != null) {
            initDatabaseProperties();
            initUiProperties();
            selectSettingsItem();
        }
    }

    /**
     *Initialize database properties
     */
    private void initDatabaseProperties() {

        host.setText(properties.getProperty(Settings.HOST));
        path.setText(properties.getProperty(Settings.PATH));
        login.setText(properties.getProperty(Settings.USER));
        password.setText(properties.getProperty(Settings.PASSWORD));

        String mod = properties.getProperty(Settings.MODE);
        boolean embedded = Settings.EMBEDDED_MODE.equals(mod != null ? mod : Settings.EMBEDDED_MODE);
        mode.getSelectionModel().select(embedded ? 1 : 0);

        passwordFieldsReverse();

        driver.setItems(FXCollections.observableArrayList(Arrays.asList("org.h2.Driver")));
        driver.getSelectionModel().selectFirst();
    }

    /**
     * Initialize ui properties
     */
    private void initUiProperties() {
        promptClosingProgram.setSelected(Boolean.valueOf(properties.getProperty(Settings.SHOW_PROMPT_IF_EXIT)));
    }

    /**
     * Selecting value from items list
     */
    private void selectSettingsItem() {

        SelectionModel model = settingsList.getSelectionModel();

        if (model.getSelectedItem() == null) {
            model.select(0);
        }

        setSettings(model.getSelectedItem().toString());
    }

    /**
     * @param name
     */
    private void setSettings(String name) {

        if (name.equals(settingsList.getItems().get(0))) {
            dbSettingsPane.setVisible(true);
            uiSettingsPane.setVisible(false);
        } else  if (name.equals(settingsList.getItems().get(1))) {
            dbSettingsPane.setVisible(false);
            uiSettingsPane.setVisible(true);
        }
    }
}
