package by.post.control.ui;

import by.post.data.Column;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;

/**
 * @author Dmitriy V.Yefremov
 */
public class ColumnPropertiesDialogController {

    @FXML
    Label tableName;
    @FXML
    Label columnName;
    @FXML
    Label columnType;
    @FXML
    CheckBox primKey;
    @FXML
    CheckBox autoIncrement;
    @FXML
    CheckBox notNull;

    public ColumnPropertiesDialogController() {

    }

    public void setData(Column column) {

        if (column == null) {
            return;
        }

        tableName.setText(column.getTableName());
        columnName.setText(column.getColumnName());
        columnType.setText(column.getType());
        primKey.setSelected(column.isPrimaryKey());
        autoIncrement.setSelected(column.isAutoIncrement());
        notNull.setSelected(column.isNotNull());
    }

}
