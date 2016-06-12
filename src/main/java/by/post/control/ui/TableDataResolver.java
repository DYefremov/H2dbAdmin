package by.post.control.ui;

import by.post.data.Cell;
import by.post.data.Row;
import by.post.data.Table;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Dmitriy V.Yefremov
 */
public class TableDataResolver {

    private Table table;
    private ObservableList tableColumns;
    private ObservableList<Cell> items;

    public TableDataResolver() {
    }

    public TableDataResolver(Table table) {
        this.table = table;
        resolve();
    }

    public void setTable(Table table) {
        this.table = table;
    }

    public ObservableList getTableColumns() {
        return tableColumns;
    }

    public ObservableList getItems() {
        return items;
    }

    private void resolve() {
        List<Row> rows = table.getRows();
        List<Cell> values;
        tableColumns = FXCollections.observableArrayList();

        if (rows != null && !rows.isEmpty()) {
            values = rows.get(0).getCells();
//            values.forEach(cell -> {
//                TableColumn<Cell, Object> column = new TableColumn(cell.getName());
//                column.setCellValueFactory(new PropertyValueFactory<Cell, Object>(cell.getValue().toString()));
//                column.setCellValueFactory(c -> {
//                    return new SimpleObjectProperty<>(cell.getValue());
//                });
//                tableColumns.add(column);
//            });

            List<Cell> cells = Arrays.asList(new Cell("NAME1","STRING", "VALUE 1"),new Cell("NAME2","STRING", "VALUE 2"));
            TableColumn col1 = new TableColumn<>("FIRST");
            col1.setCellValueFactory(new PropertyValueFactory<String, String>("name"));
            TableColumn col2 = new TableColumn<>("SECOND");
            col2.setCellValueFactory(new PropertyValueFactory<String, String>("value"));
            tableColumns.addAll(col1,col2);

            items = FXCollections.observableArrayList(cells);
        }
    }

}
