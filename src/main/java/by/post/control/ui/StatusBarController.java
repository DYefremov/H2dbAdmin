package by.post.control.ui;

import by.post.ui.Resources;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

/**
 * @author Dmitriy V.Yefremov
 */
public class StatusBarController {

    @FXML
    private Label info;

    public StatusBarController() {

    }

    @FXML
    private void initialize() {
        info.setText(Resources.PROGRAM_VERSION);
    }

}
