package by.post.control.db;

import by.post.control.Context;
import by.post.control.ui.MultipleTableColumnController;
import by.post.data.Column;
import by.post.data.Row;
import by.post.data.Table;
import by.post.data.type.ColumnDataType;
import by.post.ui.MainUiForm;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.Callback;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

/**
 * @author Dmitriy V.Yefremov
 */
public class TableDataResolver {

    private Table table;
    private TableEditor tableEditor;
    private ObservableList tableColumns;
    private ObservableList<Row> items;
    private static ColumnDataType columnDataType;

    private static final Logger logger = LogManager.getLogger(TableDataResolver.class);

    public TableDataResolver() {
        columnDataType = Context.getCurrentDataType();
    }

    public TableDataResolver(Table table) {
        this();
        this.table = table;
        resolve();
    }

    public TableDataResolver(Table table, TableEditor tableEditor) {
        this();
        this.table = table;
        this.tableEditor = tableEditor;
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
        tableColumns = getColumns(table.getColumns() != null ? table.getColumns() : new ArrayList<>());
        // Add data
        items = FXCollections.observableArrayList(table.getRows() != null ? table.getRows() : new ArrayList<>());
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
            String name = col.getColumnName();
            // Set primary key
            col.setPrimaryKey(pk != null && pk.equals(name));
            columns.add(getColumn(col));
        });

        return columns;
    }

    /**
     * Construct table column
     *
     * @param column
     * @return
     */
    public TableColumn getColumn(Column column) {

        int columnType = columnDataType.getNumType(column.getType());

        TableColumn tableColumn = getTableColumn(column);
        tableColumn.setCellValueFactory(getValueFactory(columnType));
        // Add for enable editing
        tableColumn.setCellFactory(getCellFactory(columnType));

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
            loader.setResources(ResourceBundle.getBundle("bundles.Lang", Context.getLocale()));
            tableColumn = loader.load();
            MultipleTableColumnController columnController = loader.getController();
            tableColumn.setUserData(column);
            columnController.setName(column.getColumnName());
            columnController.setType(column.getType());
            columnController.setIsKey(column.isPrimaryKey());
            columnController.setTableEditor(tableEditor);
            //Has context menu only if TableEditor is not null
            columnController.setHasContextMenu(tableEditor != null);
        } catch (IOException e) {
            logger.error("TableDataResolver error in getColumn: " + e);
        }

        return tableColumn;
    }

    /**
     * @param columnType
     * @return
     */
    private Callback getCellFactory(int columnType) {

        if (columnDataType.isLargeObject(columnType)) {
            return LargeObjectCell.forTableColumn(table);
        }

        return TextFieldTableCell.forTableColumn();
    }

    /**
     * Implementation of CellValueFactory
     */
    private Callback<TableColumn.CellDataFeatures<Row, ?>, ObservableValue<?>> getValueFactory(int columnType) {

        return cellData -> {
            TableColumn column = cellData.getTableColumn();
            int index = column.getTableView().getColumns().indexOf(column);
            Object data = cellData.getValue().getCells().get(index).getValue();
            boolean largeObject = columnDataType.isLargeObject(columnType);
            return data != null || !largeObject ? new SimpleStringProperty(String.valueOf(data)) : null;
        };

    }

}
