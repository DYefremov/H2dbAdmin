package by.post.control.ui;

import by.post.control.Context;
import by.post.data.Column;
import by.post.data.type.DefaultColumnDataType;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

/**
 * @author Dmitriy V.Yefremov
 */
public class ColumnDialogController {

    @FXML
    private TextField name;
    @FXML
    private ComboBox type;
    @FXML
    private CheckBox isKey;
    @FXML
    private CheckBox isNotNull;

    private Column column;

    public ColumnDialogController() {

    }

    public String getName() {
        return name.getText();
    }

    public String getType() {
        return String.valueOf(type.getSelectionModel().getSelectedItem());
    }

    public boolean isKey() {
        return isKey.isSelected();
    }

    public boolean isNotNull() {
        return isNotNull.isSelected();
    }

    public Column getColumn() {
        return column;
    }

    public void setColumn(Column column) {
        this.column = column;
        name.setText(column.getColumnName());
        isKey.setSelected(column.isPrimaryKey());
        isNotNull.setSelected(column.isNotNull());
        type.getSelectionModel().select(column != null ? column.getType() : DefaultColumnDataType.VARCHAR.name());
    }

    @FXML
    public void initialize() {
        type.setItems(FXCollections.observableArrayList(Context.getCurrentDataType().getValues()));
        type.getSelectionModel().select(DefaultColumnDataType.VARCHAR.name());
    }
}
