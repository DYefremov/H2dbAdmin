package by.post.control.ui.dialogs;

import by.post.control.Settings;
import by.post.ui.ConfirmationDialog;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

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
    @FXML
    private DialogPane dialogPane;
    @FXML
    private ButtonType connectButton;

    private Map<String, String> settings;

    public OpenDbDialogController() {

    }

    /**
     * @return settings
     */
    public Map<String, String> getSettings() {

        settings.put(Settings.PORT, port.getText());
        settings.put(Settings.PATH, path.getText());
        settings.put(Settings.USER, user.getText());
        settings.put(Settings.PASSWORD, password.getText());
        boolean emb = embedded.isSelected();
        settings.put(Settings.EMBEDDED_MODE, String.valueOf(emb));
        settings.put(Settings.MODE, emb ? Settings.EMBEDDED_MODE : Settings.SERVER_MODE );
        settings.put(Settings.HOST, emb ? Settings.DEFAULT_HOST : host.getText());

        return settings;
    }

    /**
     * Hyperlink path action
     */
    public void onPath() {

        File dbFile = new OpenFileDialogProvider().getFileDialog("Select db file...", false, true);

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
        setEmbedded(embedded.isSelected());
        //Consume ok button event if canceled in confirmation dialog
        dialogPane.lookupButton(connectButton).addEventFilter(ActionEvent.ACTION, event -> {
            Optional<ButtonType> result = new ConfirmationDialog().showAndWait();
            if (result.get() != ButtonType.OK) {
                event.consume();
            }
        });
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
        host.setDisable(mode);
        port.setDisable(mode);
    }
}
