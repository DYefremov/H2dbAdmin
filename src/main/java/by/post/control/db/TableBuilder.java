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
import java.util.Collection;
import java.util.List;

/**
 * @author Dmitriy V.Yefremov
 */
public class TableBuilder {

    private ColumnDataType columnDataType;
    //The limit for the maximum number of rows for the first request receiving table
    public static final int MAX_ROWS = 100;
    public static final String DEF_VALUE = "";
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

        Table table = new Table(name, type);
        boolean isSysTable = type.equals(TableType.SYSTEM_TABLE);

        try (Statement st = connection.createStatement()) {
            DatabaseMetaData dbMetaData = connection.getMetaData();
            st.setMaxRows(MAX_ROWS);
            st.executeQuery(isSysTable ? Queries.getSystemTable(name) : Queries.getTable(name));

            try (ResultSet rs = st.getResultSet()) {
                ResultSetMetaData rsMetaData = rs.getMetaData();
                table.setColumns(getColumns(rsMetaData));
                table.setRows(getRows(rs));
            }

            try (ResultSet keys = dbMetaData.getPrimaryKeys("", "", name)) {
                table.setPrimaryKey(keys.next() ? keys.getString("COLUMN_NAME") : "");
            }
            //Sets primary key if exist
            String pk = table.getPrimaryKey();
            table.getColumns().forEach(c -> c.setPrimaryKey(pk != null && pk.equals(c.getColumnName())));
        } catch (SQLException e) {
            logger.error("TableBuilder error in getTable: " + e);
        }

        return table;
    }

    /**
     * @param name
     * @param type
     * @param connection
     * @return
     */
    public Collection<?> getTableData(String name, TableType type, Connection connection) {

        boolean isSysTable = type.equals(TableType.SYSTEM_TABLE);

        try (Statement st = connection.createStatement()) {
            st.executeQuery(isSysTable ? Queries.getSystemTable(name) : Queries.getTableWithLimit(name, MAX_ROWS, 0, true));

            try (ResultSet rs = st.getResultSet()) {
                return getRows(rs);
            }
        } catch (SQLException e) {
            logger.error("TableBuilder error in getTable: " + e);
        }

        return new ArrayList<>();
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

        Context.setLoadData(true);

        List<Row> rows = new ArrayList<>();

        while (rs.next() && Context.isLoadData()) {
            rows.add(getRow(rs.getRow(), rs));
        }

        if (!Context.isLoadData()) {
            rows.clear();
        }

        Context.setLoadData(false);

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
            int type = columnDataType.getNumType(metaData.getColumnTypeName(i));
            cells.add(getCell(i, type, rs));
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
        column.setType(rsMetaData.getColumnTypeName(index));
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
    private Cell getCell(int num, int columnType, ResultSet rs) throws SQLException {

        Cell cell = new Cell(columnType, null, null);

        if (columnDataType.isLargeObject(columnType)) {
            rs.getObject(num);
            cell.setValue(rs.wasNull() ? null : DEF_VALUE);
            return cell;
        } else {
            cell.setValue(rs.getNString(num));
        }

        return cell;
    }
}
