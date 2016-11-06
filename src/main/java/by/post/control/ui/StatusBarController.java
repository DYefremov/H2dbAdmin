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
    @FXML
    private Label host;

    public StatusBarController() {

    }

    @FXML
    private void initialize() {
        host.setText("Host: " + Resources.HOST_NAME);
        info.setText("Program version: " + Resources.PROGRAM_VERSION);
    }

}
