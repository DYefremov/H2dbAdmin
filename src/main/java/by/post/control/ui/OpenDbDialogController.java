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
    private CheckBox showPassword;
    @FXML
    private CheckBox embedded;

    private Map<String, String> settings;

    public OpenDbDialogController() {

    }

    public Map<String, String> getSettings() {
        return settings;
    }

    /**
     *Hyperlink path action
     */
    public void onPath() {

        File dbFile = new OpenFileDialogProvider().getFileDialog("", false);

        if (dbFile != null) {
            path.setText(dbFile.getPath());
            embedded.setSelected(true);
        }
    }

    /**
     * Double click on host field
     *
     * @param event
     */
    @FXML
    public void onHost(MouseEvent event) {
        if (event.getClickCount() == 2) {
            new OpenFileDialogProvider().getFileDialog("", false);
        }
    }


    @FXML
    private void initialize() {

        settings = new HashMap<>();
    }

}
