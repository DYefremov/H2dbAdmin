package by.post.control.ui;

import by.post.data.Column;
import by.post.data.Table;
import by.post.ui.ColumnDialog;
import by.post.ui.ConfirmationDialog;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;

import java.util.Optional;

/**
 * @author Dmitriy V.Yefremov
 */
public class TableTabController {

    @FXML
    private TableView<Column> columnsTable;
    @FXML
    private TableColumn<Column, String> nameColumn;
    @FXML
    private TableColumn<Column, String> typeColumn;
    @FXML
    private TableColumn<Column, Boolean> keyColumn;
    @FXML
    private TableColumn<Column, Boolean> incrementColumn;
    @FXML
    private TableColumn<Column, Boolean> notNullColumn;
    @FXML
    private Label tableNameLabel;
    @FXML
    private Label typeLabel;
    @FXML
    private MainTableController mainTableController;

    private Table table;

    public TableTabController() {

    }

    @FXML
    public void onShowData() {
        mainTableController.setTable(table);
    }

    @FXML
    public void onNewColumnAdd() {

        Optional<Column> result = new ColumnDialog().showAndWait();

        if (result.isPresent()) {
            System.out.println(result.get());
        }
    }

    @FXML
    public void onEditColumn() {

        Column column = columnsTable.getSelectionModel().getSelectedItem();

        if (column == null) {
            new Alert(Alert.AlertType.ERROR, "No item is selected!").showAndWait();
            return;
        }

        Optional<Column> result = new ColumnDialog().showAndWait();

        if (result.isPresent()) {
            System.out.println(result.get());
        }
    }

    @FXML
    public void onDeleteColumn() {

        Optional<ButtonType> result = new ConfirmationDialog().showAndWait();

        if (result.get() == ButtonType.OK) {

        }
    }

    /**
     * @param table
     */
    public void setTable(Table table) {

        this.table = table;
        tableNameLabel.setText(table.getName());
        typeLabel.setText(table.getType() != null ? table.getType().name() : "");
        columnsTable.getItems().addAll(table.getColumns());
    }

    @FXML
    private void initialize() {

        initColumns();
    }

    /**
     * Initialize columns (sets cell factory)
     */
    private void initColumns() {

        keyColumn.setCellFactory(CheckBoxTableCell.forTableColumn(keyColumn));
        incrementColumn.setCellFactory(CheckBoxTableCell.forTableColumn(incrementColumn));
        notNullColumn.setCellFactory(CheckBoxTableCell.forTableColumn(notNullColumn));
    }
}
