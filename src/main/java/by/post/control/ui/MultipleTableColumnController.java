package by.post.control.ui;

import by.post.data.Column;
import by.post.data.ColumnDataType;
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
    Label name;

    @FXML
    Label type;

    private TableColumn tableColumn;
    private TableEditor tableEditor;

    public MultipleTableColumnController() {

    }

    /**
     * On context  menu actions
     */
    @FXML
    public void onAdd() {
        tableEditor.addColumn();
    }

    @FXML
    public void onRename() {

        Optional<String> result = new InputDialog("Set new name of column", "New", false).showAndWait();

        if (result.isPresent() && tableColumn.getUserData() != null) {
            String newName =  result.get();
            Column data = (Column) tableColumn.getUserData();
            data.setName(newName);
            name.setText(newName);
            tableEditor.renameColumn(tableColumn);
        }
    }

    @FXML
    public void onChangeType() {

        Optional<String> result = new ChoiceColumnTypeDialog().showAndWait();

        if (result.isPresent() && tableColumn.getUserData() != null) {
            Column data = (Column) tableColumn.getUserData();
            String newType = result.get();
            data.setType(ColumnDataType.getNumType(newType));
            type.setText(newType);
            tableEditor.changeColumnType(tableColumn);
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

    public TableColumn getTableColumn() {
        return tableColumn;
    }

    public void setTableColumn(TableColumn tableColumn) {
        this.tableColumn = tableColumn;
    }

    @FXML
    private void initialize() {
        tableEditor = TableEditor.getInstance();
    }
}
