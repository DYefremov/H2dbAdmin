package by.post.control.ui;

import by.post.control.Context;
import by.post.control.db.TableEditor;
import by.post.data.Cell;
import by.post.data.Column;
import by.post.data.Row;
import by.post.ui.ColumnDialog;
import by.post.ui.ColumnPropertiesDialog;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
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
    private TableColumn<Row, ?> tableColumn;
    private TableEditor tableEditor;
    private Column oldColumn;

    public MultipleTableColumnController() {

    }

    public void setColumn(Column column) {

        if (column == null) {
            throw new IllegalArgumentException("MultipleTableColumnController error[setColumn]: Argument can not be null!");
        }

        columnName.setText(column.getColumnName());
        type.setText(column.getType());

        if (column.isPrimaryKey()) {
            this.type.getStyleClass().add("key");
        }
    }

    public void setTableEditor(TableEditor tableEditor) {
        this.tableEditor = tableEditor;
    }

    public void setHasContextMenu(boolean has) {

        if (!has) {
            tableColumn.setContextMenu(null);
        }
    }

    /**
     * On context  menu actions
     */
    @FXML
    public void onAdd() throws IOException {

        Optional<Column> result = new ColumnDialog().showAndWait();

        if (result.isPresent()) {
            tableEditor.addColumn(result.get());
        }
    }

    @FXML
    public void onShowProperties() throws IOException {
        new ColumnPropertiesDialog((Column) tableColumn.getUserData()).showAndWait();
    }

    @FXML
    public void onChange() throws IOException {

        Column data = (Column) tableColumn.getUserData();

        if (data != null) {
            saveColumnData(data);
            Optional<Column> result = new ColumnDialog(data).showAndWait();

            if (result.isPresent()) {
                tableEditor.changeColumnProperties(oldColumn, data);
                type.setText(data.getType());
                columnName.setText(data.getColumnName());
            }
        }
    }

    /**
     * Called while starting the cell changes.
     */
    @FXML
    public void onEditStart(TableColumn.CellEditEvent<Row, String> event) {

        if (Context.isLoadData()) {
            event.consume();
            event.getTableView().refresh();
            new Alert(Alert.AlertType.ERROR, "During data loading, editing is not allowed!").showAndWait();
            return;
        }
    }

    /**
     * Called while maintaining the cell changes.
     */
    @FXML
    public void onEditCommit(TableColumn.CellEditEvent<Row, String> event) {

        int rowPos = event.getTablePosition().getRow();
        int colPos = event.getTablePosition().getColumn();

        Row row = event.getTableView().getItems().get(rowPos);
        Row keyRow = getKeyRow(row);
        row.getCells().get(colPos).setValue(event.getNewValue());
        tableEditor.saveCurrentChangedRow(keyRow, row);

        event.getTableView().refresh();
        event.consume();
    }

    public void onDelete() {
        tableEditor.deleteColumn(tableColumn);
    }

    @FXML
    private void initialize() {

    }

    /**
     *Saving the intermediate column. (Old column)
     */
    private void saveColumnData(Column column) {

        if (oldColumn == null) {
           oldColumn = new Column();
        }

        oldColumn.setTableName(column.getTableName());
        oldColumn.setColumnName(column.getColumnName());
        oldColumn.setType(column.getType());
        oldColumn.setPrimaryKey(column.isPrimaryKey());
        oldColumn.setNotNull(column.isNotNull());
        oldColumn.setAutoIncrement(column.isAutoIncrement());
        oldColumn.setCaseSensitive(column.isCaseSensitive());
        oldColumn.setReadOnly(column.isReadOnly());
        oldColumn.setSearchable(column.isSearchable());
        oldColumn.setWritable(column.isWritable());
        oldColumn.setSigned(column.isSigned());
    }

    /**
     * @param row
     * @return
     */
    private Row getKeyRow(Row row) {

        List<Cell> cells = new ArrayList<>();
        row.getCells().forEach(c -> cells.add(new Cell(c.getType(), c.getColumnName(), c.getValue())));
        Row keyRow = new Row(row.getNum(), row.getTableName(), cells);

        return keyRow;
    }
}
