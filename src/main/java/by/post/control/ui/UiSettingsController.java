package by.post.control.ui;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * @author Dmitriy V.Yefremov
 */
public class UiSettingsController {

    @FXML
    CheckBox notShowClosingDialog;

    public UiSettingsController() {

    }

    @FXML
    public void onNotShowClosingDialog() {
        new Alert(Alert.AlertType.INFORMATION, "Not implemented!").showAndWait();
    }

    @FXML
    private void initialize() {

    }
}
