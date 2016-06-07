package by.post.control.ui;

import by.post.ui.AboutDialog;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;

/**
 * Controller for about dialog
 *
 * @author Dmitriy V.Yefremov
 */
public class AboutDialogController {

    private AboutDialog aboutDialog;

    public AboutDialogController() {

    }

    public void setAboutDialog(AboutDialog aboutDialog) {
        this.aboutDialog = aboutDialog;
    }

    @FXML
    private void initialize(){

    }

}
