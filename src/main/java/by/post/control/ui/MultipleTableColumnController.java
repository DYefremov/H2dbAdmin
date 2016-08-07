package by.post.control.ui;

import by.post.data.Column;
import by.post.data.ColumnDataType;
import by.post.ui.AddColumnDialog;
import by.post.ui.ChoiceColumnTypeDialog;
import by.post.ui.InputDialog;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;

import java.util.Optional;

/**
 * @author Dmitriy V.Yefremov
 */
public class MultipleTableColumnController {

    @FXML
    private Label name;
    @FXML
    private Label type;
    @FXML
    private TableColumn tableColumn;
    private TableEditor tableEditor;

    public MultipleTableColumnController() {

    }

    /**
     * On context  menu actions
     */
    @FXML
    public void onAdd() {

        Optional<Column> result = new AddColumnDialog().showAndWait();

        if (result.isPresent()) {
            tableEditor.addColumn(result.get());
        }
    }

    @FXML
    public void onRename() {

        Optional<String> result = new InputDialog("Set new name of column", "New", false).showAndWait();

        if (result.isPresent() && tableColumn.getUserData() != null) {
            String newName = result.get();
            Column data = (Column) tableColumn.getUserData();
            data.setName(newName);
            setName(newName);
            tableEditor.renameColumn(tableColumn);
        }
    }

    @FXML
    public void onChangeType() {

        Column data = (Column) tableColumn.getUserData();

        if (data != null) {
            Optional<Column> result = new ChoiceColumnTypeDialog(data).showAndWait();

            if (result.isPresent()) {
                setType(ColumnDataType.getType(data.getType()));
                tableEditor.changeColumnType(tableColumn);
            }
        }
    }

    public void onDelete() {
        tableEditor.deleteColumn(tableColumn);
    }

    public void setName(String name) {
        this.name.setText(name);
    }

    public void setType(String type) {
        this.type.setText(type);
    }

    @FXML
    private void initialize() {
        tableEditor = TableEditor.getInstance();
    }
}
