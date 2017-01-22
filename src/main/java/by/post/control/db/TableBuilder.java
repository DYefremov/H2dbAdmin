package by.post.control.db;

import by.post.control.Context;
import by.post.data.Cell;
import by.post.data.Column;
import by.post.data.Row;
import by.post.data.Table;
import by.post.data.type.ColumnDataType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Dmitriy V.Yefremov
 */
public class TableBuilder {

    private  ColumnDataType columnDataType;

    private static final Logger logger = LogManager.getLogger(TableBuilder.class);

    public TableBuilder() {
        columnDataType = Context.getCurrentDataType();
    }

    /**
     * @param name
     * @param type
     * @param connection
     * @return constructed table
     */
    public Table getTable(String name, TableType type, Connection connection) {

        Table table = new Table(name);
        boolean isSysTable = type.equals(TableType.SYSTEM_TABLE);

        try (Statement st = connection.createStatement()) {
            DatabaseMetaData dbMetaData = connection.getMetaData();
            st.executeQuery(isSysTable ? Queries.getSystemTable(name) : Queries.getTable(name));
             try (ResultSet rs = st.getResultSet()) {
                 ResultSetMetaData rsMetaData = rs.getMetaData();
                 table.setRows(getRows(rs));
                 table.setColumns(getColumns(rsMetaData));
             }

            try (ResultSet keys = dbMetaData.getPrimaryKeys("", "", name)) {
                table.setPrimaryKey(keys.next() ? keys.getString("COLUMN_NAME") : "");
            }
        } catch (SQLException e) {
            logger.error("TableBuilder error in getTable: " + e);
        }

        return table;
    }

    /**
     * @param rsMetaData
     * @return columns list for table
     * @throws SQLException
     */
    public List<Column> getColumns(ResultSetMetaData rsMetaData) throws SQLException {

        List<Column> columns = new ArrayList<>();

        int count = rsMetaData.getColumnCount();

        for (int i = 1; i <= count; i++) {
            columns.add(getColumn(rsMetaData, i));
        }

        return columns;
    }

    /**
     * @param rs
     * @return rows list for table
     * @throws SQLException
     */
    public List<Row> getRows(ResultSet rs) throws SQLException {

        List<Row> rows = new ArrayList<>();

        while (rs.next()) {
            rows.add(getRow(rs.getRow(), rs));
        }

        return rows;
    }

    /**
     * @param rs
     * @return cells list
     * @throws SQLException
     */
    public List<Cell> getCells(ResultSet rs) throws SQLException {

        List<Cell> cells = new ArrayList<>();
        ResultSetMetaData metaData = rs.getMetaData();

        int count = metaData.getColumnCount();

        for (int i = 1; i <= count; i++) {
            if (columnDataType.isLargeObject(metaData.getColumnType(i))) {
                cells.add(new Cell(null, null, "value"));
            } else {
                cells.add(getCell(i, rs));
            }
        }

        return cells;
    }

    /**
     * @param table
     * @param connection
     */
    public void update(Table table, Connection connection) {

    }

    /**
     * @param rsMetaData
     * @param index
     * @return one column
     * @throws SQLException
     */
    private Column getColumn(ResultSetMetaData rsMetaData, int index) throws SQLException {

        Column column = new Column();
        column.setTableName(rsMetaData.getTableName(index));
        column.setColumnName(rsMetaData.getColumnName(index));
        int type = columnDataType.getValueTypeFromResultSet(rsMetaData, index);
        column.setType(columnDataType.typeName(type));
        column.setNotNull(rsMetaData.isNullable(index) != 1);
        column.setAutoIncrement(rsMetaData.isAutoIncrement(index));
        column.setReadOnly(rsMetaData.isReadOnly(index));
        column.setCaseSensitive(rsMetaData.isCaseSensitive(index));
        column.setSearchable(rsMetaData.isSearchable(index));
        column.setWritable(rsMetaData.isWritable(index));
        column.setSigned(rsMetaData.isSigned(index));
        column.setIndex(index);

        return column;
    }

    /**
     * @return row
     */
    private Row getRow(int index, ResultSet rs) throws SQLException {

        Row row = new Row();
        row.setNum(index);
        row.setCells(getCells(rs));

        return row;
    }

    /**
     * @return one cell
     */
    private Cell getCell(int num, ResultSet rs) throws SQLException {

        Cell cell = new Cell();
        cell.setValue(rs.getNString(num));

        return cell;
    }
}
