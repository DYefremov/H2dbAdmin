package by.post.control.ui;

import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Dmitriy V.Yefremov
 */
public class OpenDbDialogController {

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
    private CheckBox embedded;

    private Map<String, String> settings;

    public OpenDbDialogController() {

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
        settings.put("embedded", String.valueOf(embedded.isSelected()));

        return settings;
    }

    /**
     * Hyperlink path action
     */
    public void onPath() {

        File dbFile = new OpenFileDialogProvider().getFileDialog("", false);

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

    @FXML
    public void onMode() {
        setEmbedded(embedded.isSelected());
    }

    /**
     * initialize on creation
     */
    @FXML
    private void initialize() {
        settings = new HashMap<>();
        //
        setEmbedded(embedded.isSelected());
    }

    /**
     * On/Off embedded mode
     *
     * @param mode
     */
    private void setEmbedded(boolean mode) {

        embedded.setSelected(mode);
        host.setEditable(!mode);
        host.setText(mode ? "Embedded mode is on!" : "localhost");
        host.getTooltip().setText(mode ? "Turn off the embedded mode for editing." : "Host name");
        port.setEditable(!mode);
        port.setText(mode ? "" : "default");
    }
}
