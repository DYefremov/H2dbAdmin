package by.post.control.ui.dialogs;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;

/**
 * @author Dmitriy V.Yefremov
 */
public class ConditionBoxController {

    @FXML
    private HBox mainHBox;
    private ConditionCell cell;

    public ConditionBoxController() {

    }

    @FXML
    public void onAdd() {

        TextField textField = new TextField();
        textField.setOnKeyReleased(event -> {

            if (event.getCode().equals(KeyCode.ENTER)) {
                Platform.runLater(() -> {
                    cell.startEdit();
                    cell.commitEdit(textField.getText());
                    mainHBox.requestFocus();
                });
            }
        });

        mainHBox.getChildren().add(textField);
    }

    @FXML
    public void onDelete() {

        Platform.runLater(() -> {
            mainHBox.getChildren().remove(2, mainHBox.getChildren().size());
            cell.startEdit();
            cell.commitEdit(null);
        });
    }

    @FXML
    private void initialize() {

    }

    public void setCell(ConditionCell cell) {
        this.cell = cell;
    }
}
