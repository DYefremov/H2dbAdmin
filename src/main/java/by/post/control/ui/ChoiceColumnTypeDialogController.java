package by.post.control.ui;

import by.post.data.Column;
import by.post.data.ColumnDataType;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;

/**
 * @author Dmitriy V.Yefremov
 */
public class ChoiceColumnTypeDialogController {

    @FXML
    private ChoiceBox type;
    @FXML
    private CheckBox isKey;
    @FXML
    private CheckBox isNotNull;

    private Column column;

    public ChoiceColumnTypeDialogController() {

    }

    public boolean isKey() {
       return isKey.isSelected();
    }

    public boolean isNotNull() {
        return isNotNull.isSelected();
    }

    public int getType() {
        return ColumnDataType.getNumType(String.valueOf(type.getSelectionModel().getSelectedItem()));
    }

    public void setColumn(Column column) {
        this.column = column;
        isKey.setSelected(column.isPrimaryKey());
        isNotNull.setSelected(column.isNotNull());
        type.getSelectionModel().select(column != null ? ColumnDataType.getType(column.getType()) : ColumnDataType.VARCHAR.name());
    }

    @FXML
    private void initialize() {
        type.setItems(FXCollections.observableArrayList(ColumnDataType.getTypes().values()));
        type.getSelectionModel().select(ColumnDataType.VARCHAR.name());
    }
}
