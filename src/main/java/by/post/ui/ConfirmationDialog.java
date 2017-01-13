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
    }

    public ConfirmationDialog(String contentText) {
        super(AlertType.CONFIRMATION);
        init(contentText);
    }

    public ConfirmationDialog(String contentText, AlertType type) {
        super(type == null ? AlertType.CONFIRMATION : type);
        init(contentText);
    }

    private void init(String contextText) {
        setContentText(contextText);
        setTitle("H2dbAdmin");
        setHeaderText("Are you sure?");
        Stage stage = (Stage) getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image(Resources.LOGO_PATH));
    }
}
