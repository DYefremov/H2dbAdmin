package by.post.control.ui;

import by.post.data.*;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;


/**
 * @author Dmitriy V.Yefremov
 */
public class TableCreationDialogController {

    @FXML
    private TableView<Column> tableView;
    @FXML
    private TableColumn<Column, String> nameColumn;
    @FXML
    private TableColumn<Column, String> typeColumn;
    @FXML
    private TableColumn<Column, String> lengthColumn;
    @FXML
    private TableColumn<Column, String> keyColumn;
    @FXML
    private TableColumn<Column, String> notNullColumn;
    @FXML
    private TableColumn<Column, String> defaultValueColumn;
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

        nameColumn.setCellValueFactory(new PropertyValueFactory<>("columnName"));
        nameColumn.setCellFactory(TextFieldTableCell.forTableColumn());

        typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        typeColumn.setCellFactory(TextFieldTableCell.forTableColumn());

        keyColumn.setCellValueFactory(new PropertyValueFactory<>("primaryKey"));
//        keyColumn.setCellFactory(TextFieldTableCell.forTableColumn());

        notNullColumn.setCellValueFactory(new PropertyValueFactory<>("notNull"));
//        notNullColumn.setCellFactory(TextFieldTableCell.<Column>forTableColumn());

        lengthColumn.setCellValueFactory(new PropertyValueFactory<>("length"));
//        lengthColumn.setCellFactory(TextFieldTableCell.<Column>forTableColumn());

        defaultValueColumn.setCellValueFactory(new PropertyValueFactory<>("defaultValue"));
        defaultValueColumn.setCellFactory(TextFieldTableCell.<Column>forTableColumn());
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

