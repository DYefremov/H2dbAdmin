package by.post.control.ui;

import by.post.data.Cell;
import by.post.data.Row;
import by.post.data.Table;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Dmitriy V.Yefremov
 */
public class TableDataResolver {

    private Table table;
    private ObservableList tableColumns;
    private ObservableList<ObservableList> items;

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
        items = FXCollections.observableArrayList();

        if (rows != null && !rows.isEmpty()) {
            values = rows.get(0).getCells();
            doRow(values);
        }
    }

    /**
     * @param values
     */
    private void doRow(List<Cell> values) {
        // *** Row!!!! ***
        ObservableList<String> row = FXCollections.observableArrayList();

        for (Cell cell: values) {
            final int index = values.indexOf(cell);
            TableColumn column = new TableColumn(cell.getName());

            column.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ObservableList,String>,ObservableValue<String>>(){
                public ObservableValue<String> call(TableColumn.CellDataFeatures<ObservableList, String> param) {
                    return new SimpleStringProperty(param.getValue().get(index).toString());
                }
            });

            tableColumns.addAll(column);
            // null поля будем выводить как пустые ("")
            row.add(cell.getValue() != null ? cell.getValue().toString() : "");
        }
        items.add(row);
    }

}
