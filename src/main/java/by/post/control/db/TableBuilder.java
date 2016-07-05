package by.post.control.db;

import by.post.data.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Dmitriy V.Yefremov
 */
public class TableBuilder {

    /**
     * @param name
     * @param connection
     * @return constructed table
     */
    public Table getTable(String name, Connection connection) throws SQLException {

        Statement st = connection.createStatement();
        DatabaseMetaData dbMetaData = connection.getMetaData();
        ResultSet rs = st.executeQuery("SELECT * FROM " + name);
        ResultSetMetaData rsMetaData = rs.getMetaData();

        Table table = new Table(name);
        table.setRows(getRows(rs));
        table.setColumns(getColumns(rsMetaData));

        ResultSet keys = dbMetaData.getPrimaryKeys("", "", name);

        while (keys.next()) {
            String pk = keys.getString("COLUMN_NAME");
            table.setPrimaryKey(pk);
        }

        keys.close();
        rs.close();
        st.close();

        return table;
    }

    private List<Column> getColumns(ResultSetMetaData rsMetaData) throws SQLException {

        List<Column> columns = new ArrayList<>();

        int count = rsMetaData.getColumnCount();

        for (int i = 1; i < count; i++) {
           Column column = new Column(rsMetaData.getColumnName(i), rsMetaData.getColumnType(i));
            columns.add(column);
        }

        return columns;
    }

    /**
     * @param rs
     * @return rows list for table
     * @throws SQLException
     */
    private List<Row> getRows(ResultSet rs) throws SQLException {

        List<Row> rows = new ArrayList<>();

        while (rs.next()) {
            rows.add(getRow(rs.getRow(), rs));
        }

        return rows;
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
     * @param rs
     * @return cells list
     * @throws SQLException
     */
    private List<Cell> getCells(ResultSet rs) throws SQLException {

        List<Cell> cells = new ArrayList<>();
        ResultSetMetaData metaData = rs.getMetaData();

        int count = metaData.getColumnCount();

        for (int i = 1; i < count; i++) {
            cells.add(getCell(i, rs));
        }

        return cells;
    }

    /**
     * @return one cell
     */
    private Cell getCell(int num, ResultSet rs) throws SQLException {

        Cell cell = new Cell();
        cell.setValue(rs.getNString(num));

        return cell;
    }

    /**
     * @param table
     * @param connection
     */
    public void update(Table table, Connection connection) {

    }
}
