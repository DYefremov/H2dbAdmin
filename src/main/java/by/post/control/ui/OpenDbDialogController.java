package by.post.control.ui;

import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;

/**
 * @author Dmitriy V.Yefremov
 */
public class OpenDbDialogController {

    public OpenDbDialogController() {

    }

    /**
     * @param event
     */
    @FXML
    public void onHost(MouseEvent event) {
        if (event.getClickCount() == 2) {
            new OpenFileDialogProvider().getFileDialog("", false);
        }
    }

}
