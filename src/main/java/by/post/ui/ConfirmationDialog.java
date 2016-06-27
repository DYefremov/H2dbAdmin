package by.post.ui;

import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.stage.Stage;

/**
 * @author Dmitriy V.Yefremov
 */
public class ConfirmationDialog extends Alert {
    public ConfirmationDialog() {
        super(AlertType.CONFIRMATION);
        setTitle("H2dbAdmin");
        setHeaderText("Are you sure?");
        Stage stage =  (Stage) getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image(Resources.ICON_PATH));
    }
}