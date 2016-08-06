package by.post.control.ui;

import by.post.data.ColumnDataType;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

/**
 * @author Dmitriy V.Yefremov
 */
public class AddColumnDialogController {

    @FXML
    private TextField name;
    @FXML
    private ComboBox type;
    @FXML
    private CheckBox primaryKey;
    @FXML
    private CheckBox notNull;

    public AddColumnDialogController() {

    }

    public String getName() {
        return name.getText();
    }

    public String getType() {
        return String.valueOf(type.getSelectionModel().getSelectedItem());
    }

    public boolean getPrimaryKey() {
        return primaryKey.isSelected();
    }

    public boolean getNotNull() {
        return notNull.isSelected();
    }

    @FXML
    private void initialize() {
        name.setText("New column");
        type.setItems(FXCollections.observableArrayList(ColumnDataType.getTypes().values()));
        type.getSelectionModel().select(ColumnDataType.VARCHAR.name());
    }
}
