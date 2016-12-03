package by.post.control.ui;

import by.post.data.Column;
import by.post.data.ColumnDataType;
import by.post.data.Table;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;


/**
 * @author Dmitriy V.Yefremov
 */
public class TableCreationDialogController {

    @FXML
    private TableView tableView;
    @FXML
    private TableColumn nameColumn;
    @FXML
    private TableColumn typeColumn;
    @FXML
    private TableColumn lengthColumn;
    @FXML
    private TableColumn keyColumn;
    @FXML
    private TableColumn notNullColumn;
    @FXML
    private TableColumn defaultValueColumn;
    @FXML
    private TextField tableName;
    @FXML
    private DialogPane dialogPane;

    private Table table;

    public TableCreationDialogController() {

    }

    @FXML
    public void onAddButton() {
        addRow();
    }

    @FXML
    public void onDeleteButton() {

        int selectedIndex = tableView.getSelectionModel().getSelectedIndex();

        if (selectedIndex == -1) {
            return;
        }

        tableView.getItems().remove(selectedIndex);
    }

    /**
     * @return
     */
    public Table getTable() {
        return table == null ? new Table(tableName.getText()) : table;
    }

    @FXML
    private void initialize() {
        initColumns();
    }

    /**
     *
     */
    private void addRow() {

        int selectedIndex = tableView.getSelectionModel().getSelectedIndex();
        selectedIndex = selectedIndex == -1 ? ++selectedIndex : selectedIndex;

        tableView.getItems().add(selectedIndex, getColumn());
        tableView.getSelectionModel().select(selectedIndex, null);
    }

    /**
     *
     */
    private void initColumns() {

        nameColumn.setCellValueFactory(new PropertyValueFactory<Column, String>("columnName"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<Column, String>("type"));
        keyColumn.setCellValueFactory(new PropertyValueFactory<Column, String>("primaryKey"));
        notNullColumn.setCellValueFactory(new PropertyValueFactory<Column, String>("notNull"));
        lengthColumn.setCellValueFactory(new PropertyValueFactory<Column, String>("length"));
        defaultValueColumn.setCellValueFactory(new PropertyValueFactory<Column, String>("defaultValue"));
    }

    /**
     *
     * @return default column
     */
    private Column getColumn() {

        Column column = new Column(tableName.getText(), "ColumnName", ColumnDataType.VARCHAR.name());
        column.setDefaultValue("value");
        column.setLength(255);

        return column;
    }

    /**
     * @param column
     */
    private void setColumnGraphic(TableColumn column, Node node) {

        column.setCellFactory(param -> {
            TableCell cell = new TableCell();
            cell.setGraphic(node);
            return cell;
        });
    }
}

