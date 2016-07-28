package by.post.control.ui;

import by.post.data.Column;
import by.post.data.ColumnDataType;
import by.post.data.Row;
import by.post.data.Table;
import by.post.ui.MainUiForm;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.Callback;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Dmitriy V.Yefremov
 */
public class TableDataResolver {

    private Table table;
    private ObservableList tableColumns;
    private ObservableList<ObservableList> items;

    private static final Logger logger = LogManager.getLogger(TableDataResolver.class);

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
        // Add columns
        tableColumns = getColumns(table.getColumns() != null ? table.getColumns() : new ArrayList<Column>());
        // Add data
        items = FXCollections.observableArrayList();
        List<Row> rows = table.getRows();

        if (rows != null && !rows.isEmpty()) {
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
    public ObservableList getColumns(List<Column> values) {

        String pk = table != null ? table.getPrimaryKey() : null;
        ObservableList columns = FXCollections.observableArrayList();

        values.forEach(col -> {
            final int index = values.indexOf(col);
            String name = col.getName();
            columns.add(getColumn(col, index, pk != null && pk.equals(name)));
        });

        return columns;
    }

    /**
     * Construct table column
     *
     * @param column
     * @param index
     * @return
     */
    public TableColumn getColumn(Column column, int index, boolean isKey) {

        TableColumn tableColumn = getTableColumn(column);
        //Set style for primary key tableColumn
        if (isKey) {
            tableColumn.getStyleClass().add("key");
        }

        tableColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ObservableList, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<ObservableList, String> param) {
                return new SimpleStringProperty(param.getValue().get(index).toString());
            }
        });
        // Add for enable editing
        tableColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        tableColumn.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<ObservableList, String>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<ObservableList, String> event) {
                int rowPos = event.getTablePosition().getRow();
                int colPos = event.getTablePosition().getColumn();
                event.getTableView().getItems().get(rowPos).set(colPos, event.getNewValue());
            }
        });

        return tableColumn;
    }

    /**
     * Get custom table column from fxml
     *
     * @param column
     * @return
     */
    private TableColumn getTableColumn(Column column) {
        TableColumn tableColumn = null;
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainUiForm.class.getResource("MultipleTableColumn.fxml"));
            tableColumn = (TableColumn) loader.load();
            MultipleTableColumnController columnController = loader.getController();
            tableColumn.setUserData(column);
            columnController.setName(column.getName());
            columnController.setType(ColumnDataType.getType(column.getType()));
            columnController.setTableColumn(tableColumn);
        } catch (IOException e) {
            logger.error("TableDataResolver error in getColumn: " + e);
        }

        return tableColumn;
    }
}
