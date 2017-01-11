package by.post.control.ui;

import by.post.control.Settings;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Dmitriy V.Yefremov
 */
public class DatabaseDialogController {

    @FXML
    private TextField host;
    @FXML
    private TextField port;
    @FXML
    private TextField path;
    @FXML
    private TextField dbName;
    @FXML
    private TextField user;
    @FXML
    private PasswordField password;
    @FXML
    private ChoiceBox mode;
    @FXML
    private GridPane gridPane;

    private Map<String, String> settings;

    public DatabaseDialogController() {

    }

    /**
     * @return settings
     */
    public Map<String, String> getSettings() {

        boolean serverMode = mode.getValue().equals("Server");

        settings.put(Settings.MODE, serverMode ? Settings.SERVER_MODE : Settings.EMBEDDED_MODE);
        settings.put(Settings.HOST, host.getText());
        settings.put(Settings.PORT, port.getText());
        settings.put(Settings.PATH, path.getText() + File.separator +  dbName.getText());
        settings.put(Settings.USER, user.getText());
        settings.put(Settings.PASSWORD, password.getText());

        return settings;
    }

    /**
     * Hyperlink path action
     */
    public void onPath() {

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
            onPath();
        }
    }

    /**
     * initialize on creation
     */
    @FXML
    private void initialize() {
        settings = new HashMap<>();
    }

    @FXML
    public void onModeSelection() {

        int index = mode.getSelectionModel().getSelectedIndex();
        port.setDisable(index == 1);
        host.setDisable(index == 1);
    }
}
