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
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.ChoiceBoxTableCell;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.text.Text;
import javafx.util.Callback;
import javafx.util.StringConverter;
import javafx.util.converter.BigIntegerStringConverter;
import javafx.util.converter.DefaultStringConverter;
import javafx.util.converter.DoubleStringConverter;
import javafx.util.converter.NumberStringConverter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
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
                row.getCells().forEach(cell -> newRow.add(cell.getValue() != null ? cell.getValue().toString() : ""));
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

//        if (columnDataType.isLargeObject(columnType)) {
//            return getBlobClobTableColumn(tableColumn);
//        }

        tableColumn.setCellValueFactory(new TypedCellValueFactory(tableColumn, columnType));
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
            tableColumn = loader.load();
            MultipleTableColumnController columnController = loader.getController();
            tableColumn.setUserData(column);
            columnController.setName(column.getColumnName());
            columnController.setType(column.getType());
            columnController.setIsKey(column.isPrimaryKey());
        } catch (IOException e) {
            logger.error("TableDataResolver error in getColumn: " + e);
        }

        return tableColumn;
    }

    /**
     * @param column
     * @return table column with blob or clob type
     */
    private TableColumn getBlobClobTableColumn(TableColumn column) {

        TableColumn tableColumn = column;

        column.setCellFactory(param -> {
            final TableCell cell = new TableCell();
            final Hyperlink hyperlink = new Hyperlink("download");
            hyperlink.setOnAction(event -> {
                int rowIndex = cell.getTableRow().getIndex();
                LobDataManager.getInstance().save(rowIndex, (Column) tableColumn.getUserData(), table);
            });

            cell.setGraphic(hyperlink);

            return cell;
        });

        return tableColumn;
    }

    /**
     * @param columnType
     * @return
     */
    private Callback getCellFactory(int columnType) {

        if (columnDataType.isLargeObject(columnType)) {
            return ChoiceBoxTableCell.forTableColumn();
        }

        return TextFieldTableCell.forTableColumn();
    }

    /**
     * Implementation of CellValueFactory
     */
    private class TypedCellValueFactory implements Callback<TableColumn.CellDataFeatures<ObservableList, String>, ObservableValue<?>> {

        private int columnType;
        private TableColumn tableColumn;

        TypedCellValueFactory(TableColumn tableColumn, int columnType) {
            this.tableColumn = tableColumn;
            this.columnType = columnType;
        }

        @Override
        public ObservableValue<?> call(TableColumn.CellDataFeatures<ObservableList, String> cellData) {

            int index = tableColumn.getTableView().getColumns().indexOf(tableColumn);

            if (columnDataType.isLargeObject(columnType)) {
               return null;
            }

            return new SimpleStringProperty(String.valueOf(cellData.getValue().get(index)));
        }
    }

}
