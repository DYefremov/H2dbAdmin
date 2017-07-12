package by.post.control.ui.dialogs;

import by.post.control.Context;
import by.post.control.ui.CheckedIntegerStringConverter;
import by.post.data.Column;
import by.post.data.Table;
import by.post.data.type.DefaultColumnDataType;
import by.post.ui.ConfirmationDialog;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.TextFieldTableCell;

import java.util.Collection;
import java.util.Optional;


/**
 * The controller class for table build dialog.
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
    @FXML
    private ButtonType createButton;

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
        Integer value = event.getNewValue();
        event.getRowValue().setLength(value != null ? value : event.getOldValue());
        tableView.refresh();
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
    public void initialize() {

        initColumnsCellFactory();
        //Consume ok button event if canceled in confirmation dialog
        dialogPane.lookupButton(createButton).addEventFilter(ActionEvent.ACTION, event -> {
            Optional<ButtonType> result = new ConfirmationDialog().showAndWait();
            if (result.get() != ButtonType.OK) {
                event.consume();
            }
        });
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

        Collection<String> columnTypes = Context.getCurrentDataType().getValues();

        nameColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        typeColumn.setCellFactory(ComboBoxTableCell.forTableColumn(FXCollections.observableArrayList(columnTypes)));
        lengthColumn.setCellFactory(TextFieldTableCell.forTableColumn(new CheckedIntegerStringConverter()));
        keyColumn.setCellFactory(CheckBoxTableCell.forTableColumn(keyColumn));
        notNullColumn.setCellFactory(CheckBoxTableCell.forTableColumn(notNullColumn));
        defaultValueColumn.setCellFactory(TextFieldTableCell.forTableColumn());
    }

    /**
     * @return default column
     */
    private Column getColumn() {

        Column column = new Column(tableName.getText(), "ColumnName", DefaultColumnDataType.VARCHAR);
        column.setLength(255);

        return column;
    }
}




