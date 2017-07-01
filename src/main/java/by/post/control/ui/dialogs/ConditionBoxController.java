package by.post.control.ui.dialogs;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;

import java.util.List;

/**
 * @author Dmitriy V.Yefremov
 */
public class ConditionBoxController {

    @FXML
    private HBox mainHBox;
    private ConditionCell cell;

    public ConditionBoxController() {

    }

    public void setCell(ConditionCell cell) {
        this.cell = cell;
    }

    @FXML
    public void onAdd() {

        cell.getTableRow().getTableView().getSelectionModel().select(cell.getIndex());

        TextField textField = new TextField();
        textField.setOnKeyReleased(event -> {

            textField.setStyle(null);

            if (event.getCode().equals(KeyCode.ENTER)) {
                if (textField.getText().isEmpty()) {
                    textField.setStyle("-fx-border-color: red;" );
                    event.consume();
                    return;
                }
                Platform.runLater(() -> setCellValue(getCondition()));
            }
        });

        mainHBox.getChildren().add(textField);
    }

    @FXML
    public void onDelete() {

        cell.getTableRow().getTableView().getSelectionModel().select(cell.getIndex());
        mainHBox.getChildren().remove(3, mainHBox.getChildren().size());
        Platform.runLater(() -> setCellValue(null));
    }

    @FXML
    private void initialize() {

    }

    private void setCellValue(String value) {

        cell.startEdit();
        cell.commitEdit(value);
        cell.getTableRow().getTableView().getSelectionModel().select(cell.getIndex());
        cell.requestFocus();
    }

    private String getCondition() {

        String column = cell.getTableView().getItems().get(cell.getIndex()).getColumnName();
        StringBuilder sb = new StringBuilder();
        List<Node> nodes = mainHBox.getChildren();

        int lastIndex = nodes.size() - 1;

        for (Node node : nodes) {

            if (node instanceof ChoiceBox) {
                ChoiceBox choiceBox = (ChoiceBox) node;
                sb.append(" " + choiceBox.getValue() + " ");
            }

            if (node instanceof TextField) {
                int index = nodes.indexOf(node);
                String value = ((TextField) node).getText();
                sb.append(column);
                sb.append(value.toUpperCase().equals("NULL") ? " IS NULL" : " = '" + value + "'");
                sb.append(index == lastIndex ? "" : " OR ");
            }
        }

        return sb.toString();
    }

}
