package by.post.control.ui;

import by.post.control.db.TableEditor;
import by.post.data.Cell;
import by.post.data.Column;
import by.post.ui.ColumnDialog;
import by.post.ui.ConfirmationDialog;
import by.post.ui.Resources;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.image.ImageView;

import java.util.Optional;

/**
 * @author Dmitriy V.Yefremov
 */
public class MultipleTableColumnController {

    @FXML
    private Label columnName;
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
    public void onShowProperties() {

        //TODO add extra dialog
        Column column = (Column) tableColumn.getUserData();
        StringBuilder sb = new StringBuilder();
        sb.append("TABLE NAME:     " + column.getTableName() +"\n");
        sb.append("NAME:           " + column.getColumnName() + "\n");
        sb.append("TYPE:           " + column.getType() + "\n");
        sb.append("AUTO_INCREMENT: " + column.isAutoIncrement() + "\n");
        sb.append("NOT NULL:       " + column.isNotNull() + "\n");

        Alert alert = new Alert(Alert.AlertType.INFORMATION, sb.toString());
        alert.setTitle(Resources.TITLE);
        alert.setHeaderText("Column: " + column.getColumnName());
        alert.showAndWait();
    }

    @FXML
    public void onChange() {

        Column data = (Column) tableColumn.getUserData();

        if (data != null) {
            Optional<Column> result = new ColumnDialog(data).showAndWait();

            if (result.isPresent()) {
                setType(data.getType());
                setName(data.getColumnName());
                tableEditor.changeColumnProperties(data);
            }
        }
    }

    /**
     * Called while maintaining the cell changes.
     */
    @FXML
    public void onEditCommit(TableColumn.CellEditEvent<ObservableList, String> event) {

        int rowPos = event.getTablePosition().getRow();
        int colPos = event.getTablePosition().getColumn();

        Optional<ButtonType> result = new ConfirmationDialog().showAndWait();

        if (result.get() == ButtonType.OK) {
            Column column = (Column) tableColumn.getUserData();
            Cell cell = new Cell(column.getColumnName(), column.getType(), event.getNewValue());
            ObservableList<String> rowValues = event.getTableView().getItems().get(rowPos);

            if (tableEditor.changeRow(cell, rowValues)) {
                rowValues.set(colPos, event.getNewValue());
            }
        }

        event.getTableView().refresh();
        event.consume();
    }

    public void onDelete() {
        tableEditor.deleteColumn(tableColumn);
    }

    public void setName(String name) {
        this.columnName.setText(name);
    }

    public void setType(String type) {
        this.type.setText(type);
    }

    public void setIsKey(boolean isKey) {
        if (isKey) {
            this.type.getStyleClass().add("key");
        }
    }

    @FXML
    private void initialize() {
        tableEditor = TableEditor.getInstance();
    }
}
