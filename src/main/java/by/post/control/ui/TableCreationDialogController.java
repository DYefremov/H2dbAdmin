package by.post.control.ui;

import by.post.data.Column;
import by.post.data.ColumnDataType;
import by.post.data.Table;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.converter.IntegerStringConverter;


/**
 * The controller class to build a new table.
 *
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
    private TableColumn<Column, Integer> lengthColumn;
    @FXML
    private TableColumn<Column, Boolean> keyColumn;
    @FXML
    private TableColumn<Column, Boolean> notNullColumn;
    @FXML
    private TableColumn<Column, String> defaultValueColumn;
    @FXML
    private TextField tableName;
    @FXML
    private DialogPane dialogPane;

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

    @FXML
    public void onEditName(TableColumn.CellEditEvent<Column, String> event) {
        event.getRowValue().setColumnName(event.getNewValue());
    }

    @FXML
    public void onEditType(TableColumn.CellEditEvent<Column, String> event) {
        event.getRowValue().setType(event.getNewValue());
    }

    @FXML
    public void onEditLength(TableColumn.CellEditEvent<Column, Integer> event) {
        event.getRowValue().setLength(event.getNewValue());
    }

    @FXML
    public void onEditKey(TableColumn.CellEditEvent<Column, Boolean> event) {
        event.getRowValue().setPrimaryKey(event.getNewValue());
    }

    @FXML
    public void onEditNotNull(TableColumn.CellEditEvent<Column, Boolean> event) {
        event.getRowValue().setNotNull(event.getNewValue());
    }

    @FXML
    public void onEditDefaultValue(TableColumn.CellEditEvent<Column, String> event) {
        event.getRowValue().setDefaultValue(event.getNewValue());
    }

    /**
     * @return table
     */
    public Table getTable() {

        Table table = new Table(tableName.getText());
        table.setColumns(tableView.getItems());

        return table;
    }

    @FXML
    private void initialize() {
        initColumnsCellFactory();
    }

    /**
     * Add new row (Column)
     */
    private void addRow() {

        int selectedIndex = tableView.getSelectionModel().getSelectedIndex();
        tableView.getItems().add(++selectedIndex, getColumn());
        tableView.getSelectionModel().select(selectedIndex, null);
    }

    /**
     * Initialise CellFactory for columns
     * Cell value factory is specified in fxml file!!!
     */
    private void initColumnsCellFactory() {

        nameColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        typeColumn.setCellFactory(ComboBoxTableCell.forTableColumn(FXCollections.observableArrayList(ColumnDataType.getTypes().values())));
        lengthColumn.setCellFactory(TextFieldTableCell.forTableColumn(new CustomIntegerStringConverter()));
        keyColumn.setCellFactory(CheckBoxTableCell.forTableColumn(keyColumn));
        notNullColumn.setCellFactory(CheckBoxTableCell.forTableColumn(notNullColumn));
        defaultValueColumn.setCellFactory(TextFieldTableCell.forTableColumn());
    }

    /**
     * @return default column
     */
    private Column getColumn() {

        Column column = new Column(tableName.getText(), "ColumnName", ColumnDataType.VARCHAR.name());
        column.setDefaultValue("value");
        column.setLength(255);

        return column;
    }

    /**
     * Custom implementation of IntegerStringConverter
     */
    class CustomIntegerStringConverter extends IntegerStringConverter {

        private int defaultValue;

        public CustomIntegerStringConverter() {

        }

        public CustomIntegerStringConverter(int defaultValue) {
            this.defaultValue = defaultValue;
        }

        @Override
        public Integer fromString(String value) {

            if (value == null) {
                return defaultValue > 0 ? defaultValue : null;
            }

            value = value.trim();

            if (value.length() < 1) {
                return defaultValue > 0 ? defaultValue : null;
            }

            if (!value.matches("\\d+")) {
                new Alert(Alert.AlertType.ERROR, "Please specify correct value!").showAndWait();
                return defaultValue > 0 ? defaultValue : null;
            }

            return Integer.valueOf(value);
        }
    }

}




