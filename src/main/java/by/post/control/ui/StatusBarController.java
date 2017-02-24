package by.post.control.ui;

import by.post.control.Context;
import by.post.ui.Resources;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;

/**
 * @author Dmitriy V.Yefremov
 */
public class StatusBarController {

    @FXML
    private Label info;
    @FXML
    private Label host;
    @FXML
    private ProgressBar loadDataStatus;
    @FXML
    private Label loadDataLabel;

    public StatusBarController() {

    }

    @FXML
    public void initialize() {
        host.setText("Host: " + Resources.HOST_NAME);
        info.setText("Program version: " + Resources.PROGRAM_VERSION);
        loadDataStatus.visibleProperty().bind(Context.getIsLoadDataProperty());
        loadDataLabel.visibleProperty().bind(Context.getIsLoadDataProperty());
    }

}
