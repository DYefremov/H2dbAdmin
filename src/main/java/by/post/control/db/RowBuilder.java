package by.post.control.db;

import by.post.data.Cell;
import by.post.data.Row;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Dmitriy V.Yefremov
 */
public class RowBuilder {
    public List<Row> getRows(String table, Connection connection) throws SQLException {

        List<Row> rows = new ArrayList<>();
        Statement st = connection.createStatement();
        ResultSet rs = st.executeQuery("SELECT * FROM " + table);

        while (rs.next()) {
            rows.add(getRow(rs.getRow(), rs));
        }

        return rows;
    }

    /**
     * @return
     */
    private Row getRow(int index, ResultSet resultSet) throws SQLException {

        List<Cell> cells = new ArrayList<>();
        Row row = new Row();
        row.setNum(index);

        int count = resultSet.getMetaData().getColumnCount();

        for (int i = 1; i < count; i++) {
            cells.add(getCell(i, resultSet));
        }

        row.setCells(cells);

        return row;
    }

    /**
     * @return
     */
    private Cell getCell(int num, ResultSet rs) throws SQLException {
        Cell cell = new Cell();
        ResultSetMetaData metaData = rs.getMetaData();
        cell.setName(metaData.getColumnName(num));
        cell.setType(metaData.getColumnTypeName(num));
        cell.setValue(rs.getNString(num));

        return cell;
    }
}
