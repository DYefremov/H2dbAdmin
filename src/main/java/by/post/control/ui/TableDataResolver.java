package by.post.control.ui;

import by.post.data.Cell;
import by.post.data.Row;
import by.post.data.Table;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Dmitriy V.Yefremov
 */
public class TableDataResolver {

    private Table table;
    private List<TableColumn> tableColumns;
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

    public List<TableColumn> getTableColumns() {
        return tableColumns;
    }

    public ObservableList getItems() {
        return items;
    }

    private void resolve() {
        List<Row> rows = table.getRows();
        List<Cell> values;
        tableColumns = new ArrayList<>();

        if (rows != null && !rows.isEmpty()) {
            values = rows.get(0).getCells();
            values.forEach(cell -> {
                TableColumn<Object, Object> column = new TableColumn(cell.getName());

                column.setCellValueFactory(c -> {
                    return new SimpleObjectProperty<>(cell.getValue());
                });
                tableColumns.add(column);
            });

            items = FXCollections.observableArrayList(values);
        }
    }
}
