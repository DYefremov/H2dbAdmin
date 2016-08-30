package by.post.control.ui;

import by.post.control.db.TableEditor;
import by.post.data.Column;
import by.post.ui.ColumnDialog;
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

        Optional<Column> result = new ColumnDialog().showAndWait();

        if (result.isPresent()) {
            tableEditor.addColumn(result.get());
        }
    }

    @FXML
    public void onChange() {

        Column data = (Column) tableColumn.getUserData();

        if (data != null) {
            Optional<Column> result = new ColumnDialog(data).showAndWait();

            if (result.isPresent()) {
                setType(data.getType());
                setName(data.getName());
                tableEditor.changeColumnProperties(tableColumn);
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
