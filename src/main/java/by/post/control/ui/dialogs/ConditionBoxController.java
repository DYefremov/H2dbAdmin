package by.post.control.ui.dialogs;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;

/**
 * @author Dmitriy V.Yefremov
 */
public class ConditionBoxController {

    @FXML
    private HBox mainHBox;

    public ConditionBoxController() {

    }

    @FXML
    public void onAdd() {
        TextField textField = new TextField();
        mainHBox.getChildren().add(textField);
    }

    @FXML
    public void onDelete() {
        mainHBox.getChildren().remove(2, mainHBox.getChildren().size());
    }

    @FXML
    private void initialize() {

    }

}
