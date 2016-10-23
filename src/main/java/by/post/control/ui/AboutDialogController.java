package by.post.control.ui;

import by.post.ui.Resources;
import javafx.application.HostServices;
import javafx.fxml.FXML;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;

/**
 * Controller for about dialog
 *
 * @author Dmitriy V.Yefremov
 */
public class AboutDialogController {

    @FXML
    private TextArea textArea;
    @FXML
    private Label versionLabel;
    @FXML
    private Label javaVersion;
    @FXML
    private Hyperlink hyperlink;

    private HostServices hostServices;

    public AboutDialogController() {

    }

    public void setHostServices(HostServices hostServices) {
        this.hostServices = hostServices;
    }

    @FXML
    public void onHyperlink() {
        if (hostServices != null) {
            hostServices.showDocument("mailto:" + hyperlink.getText());
        }
    }

    @FXML
    private void initialize(){
        textArea.setText(Resources.ABOUT_TEXT + Resources.USED_RESOURCES + Resources.CREATOR);
        versionLabel.setText("H2dbAdmin ver: " + Resources.PROGRAM_VERSION);
        javaVersion.setText(Resources.JAVA_VERSION);
    }

}
