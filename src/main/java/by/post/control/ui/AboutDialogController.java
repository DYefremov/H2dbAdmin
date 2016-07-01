package by.post.control.ui;

import by.post.ui.AboutDialog;
import javafx.fxml.FXML;
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

    private AboutDialog aboutDialog;

    private final static String VERSION = "0.0.0";

    private final static String ABOUT_TEXT = "This program is free software and " +
            "distributed in the hope that it will be useful, but WITHOUT ANY " +
            "WARRANTY! \n  Dmitriy Yefremov  2016";

    public AboutDialogController() {

    }

    public void setAboutDialog(AboutDialog aboutDialog) {
        this.aboutDialog = aboutDialog;
    }

    @FXML
    private void initialize(){
        textArea.setText(ABOUT_TEXT);
        versionLabel.setText("H2dbAdmin ver: " + VERSION);
    }

}
