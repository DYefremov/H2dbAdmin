package by.post.control.ui;

import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

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
    private TextField user;
    @FXML
    private PasswordField password;
    @FXML
    private ChoiceBox mode;

    private Map<String, String> settings;

    public DatabaseDialogController() {

    }

    /**
     * @return settings
     */
    public Map<String, String> getSettings() {

        settings.put("host", host.getText());
        settings.put("port", port.getText());
        settings.put("path", path.getText());
        settings.put("user", user.getText());
        settings.put("password", password.getText());

        return settings;
    }

    /**
     * Hyperlink path action
     */
    public void onPath() {

        File dbFile = new OpenFileDialogProvider().getFileDialog("", true);

        if (dbFile != null) {
            path.setText(dbFile.getPath());
            setEmbedded(true);
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

    /**
     * On/Off embedded mode
     *
     * @param mode
     */
    private void setEmbedded(boolean mode) {

    }

}
