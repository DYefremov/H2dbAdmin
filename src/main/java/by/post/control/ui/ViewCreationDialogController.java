package by.post.control.ui;

import by.post.data.View;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;

/**
 * @author Dmitriy V.Yefremov
 */
public class ViewCreationDialogController {

    private View view;

    public ViewCreationDialogController() {

    }

    public View getView() {
        return view != null ? view : new View();
    }

    @FXML
    public void onAddButton() {
        new Alert(Alert.AlertType.INFORMATION, "Not implemented yet!").showAndWait();
    }

    @FXML
    public void onDeleteButton() {
        new Alert(Alert.AlertType.INFORMATION, "Not implemented yet!").showAndWait();
    }
}
