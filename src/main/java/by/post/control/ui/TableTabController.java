package by.post.control.ui;

import by.post.control.db.TableDataResolver;
import by.post.data.Column;
import by.post.data.Row;
import by.post.data.Table;
import by.post.ui.ColumnDialog;
import by.post.ui.ConfirmationDialog;
import javafx.fxml.FXML;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableView;

import java.util.Optional;

/**
 * @author Dmitriy V.Yefremov
 */
public class TableTabController {

    @FXML
    private TableView<Column> columnsTable;
    @FXML
    private TableView<Row> dataTable;


    public TableTabController() {

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

        Optional<Column> result = new ColumnDialog(columnsTable.getSelectionModel().getSelectedItem()).showAndWait();

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

    public void setTable(Table table) {

        TableDataResolver resolver = new TableDataResolver(table);
        columnsTable.getItems().addAll(table.getColumns());
        dataTable.getColumns().addAll(resolver.getTableColumns());
        dataTable.getItems().addAll(resolver.getItems());
    }

    @FXML
    private void initialize() {

    }
}
