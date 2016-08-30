package by.post.control.db;

import by.post.data.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Dmitriy V.Yefremov
 */
public class TableBuilder {

    private static final Logger logger = LogManager.getLogger(TableBuilder.class);

    /**
     * @param name
     * @param connection
     * @return constructed table
     */
    public Table getTable(String name, Connection connection) {

        Table table = new Table(name);

        Statement st = null;
        DatabaseMetaData dbMetaData = null;
        ResultSet rs = null;
        ResultSetMetaData rsMetaData = null;
        ResultSet keys = null;

        try {
            st = connection.createStatement();
            dbMetaData = connection.getMetaData();
            st.executeQuery(Queries.getTable(name));
            rs = st.getResultSet();
            rsMetaData = rs.getMetaData();
            keys = dbMetaData.getPrimaryKeys("", "", name);

            while (keys.next()) {
                String pk = keys.getString("COLUMN_NAME");
                table.setPrimaryKey(pk);
            }

            table.setRows(getRows(rs));
            table.setColumns(getColumns(rsMetaData));
        } catch (SQLException e) {
            logger.error("TableBuilder error in getTable: " + e);
        } finally {
            try {
                if (keys != null) {
                    keys.close();
                }

                if (rs != null) {
                    rs.close();
                }

                if (st != null) {
                    st.close();
                }
            } catch (SQLException e) {
                logger.error("TableBuilder error in getTable[finally]: " + e);
            }
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
            cells.add(getCell(i, rs));
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
        column.setName(rsMetaData.getColumnName(index));
        column.setType(rsMetaData.getColumnTypeName(index));
        column.setNotNull(rsMetaData.isNullable(index) == 1);
        column.setAutoIncrement(rsMetaData.isAutoIncrement(index));
        column.setReadOnly(rsMetaData.isReadOnly(index));
        column.setCaseSensitive(rsMetaData.isCaseSensitive(index));
        column.setSearchable(rsMetaData.isSearchable(index));
        column.setWritable(rsMetaData.isWritable(index));
        column.setSigned(rsMetaData.isSigned(index));

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
