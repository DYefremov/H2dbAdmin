package by.post.control.ui;

import by.post.data.Cell;
import by.post.data.Row;
import by.post.data.Table;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.Callback;

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
        return tableColumns == null ? FXCollections.emptyObservableList() : tableColumns;
    }

    public ObservableList getItems() {
        return items;
    }

    /**
     * Resolve data from table
     */
    private void resolve() {

        List<Row> rows = table.getRows();
        items = FXCollections.observableArrayList();

        if (rows != null && !rows.isEmpty()) {
            // Add columns by first row in table
            tableColumns = getColumns(rows.get(0).getCells());
            // Add data
            rows.stream().forEach(row -> {
                ObservableList<String> newRow = FXCollections.observableArrayList();
                row.getCells().forEach(cell -> {
                    newRow.add(cell.getValue() != null ? cell.getValue().toString() : "");
                });
                items.add(newRow);
            });
        }
    }

    /**
     * Add columns by first row in table
     *
     * @param values
     */
    public ObservableList getColumns(List<Cell> values) {

        ObservableList columns = FXCollections.observableArrayList();

        values.forEach(cell -> {
            final int index = values.indexOf(cell);
            String colName = cell.getName();
            String pk = table.getPrimaryKey();

            TableColumn column = new TableColumn(colName);
            //Set style for primary key column
            if (pk !=null && pk.equals(colName)) {
                column.getStyleClass().add("key");
            }

            column.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ObservableList, String>, ObservableValue<String>>() {
                @Override
                public ObservableValue<String> call(TableColumn.CellDataFeatures<ObservableList, String> param) {
                    return new SimpleStringProperty(param.getValue().get(index).toString());
                }
            });
            // Add for enable editing
            column.setCellFactory(TextFieldTableCell.forTableColumn());
            column.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<ObservableList, String>>() {
                @Override
                public void handle(TableColumn.CellEditEvent<ObservableList, String> event) {
                    int rowPos = event.getTablePosition().getRow();
                    int colPos = event.getTablePosition().getColumn();
                    event.getTableView().getItems().get(rowPos).set(colPos, event.getNewValue());
                }
            });
            columns.addAll(column);
        });
        return columns;
    }
}
