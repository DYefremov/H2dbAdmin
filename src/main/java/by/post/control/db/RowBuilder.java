package by.post.control.db;

import by.post.data.Column;
import by.post.data.Row;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Dmitriy V.Yefremov
 */
public class RowBuilder {
  public List<Row> getRows (String table, Connection connection) throws SQLException{

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
  private Row getRow(int index, ResultSet resultSet) throws SQLException{

    List<Column> columns = new ArrayList<>();
    Row row = new Row();
    row.setNum(index);

    int count = resultSet.getMetaData().getColumnCount();

    for (int i = 1; i < count; i++) {
      columns.add(getColumn(i, resultSet));
    }

    row.setColumns(columns);

    return row;
  }

  /**
   * @return
   */
  private Column getColumn(int num, ResultSet rs) throws SQLException {
    Column column = new Column();
    ResultSetMetaData metaData = rs.getMetaData();
    column.setName(metaData.getColumnName(num));
    column.setType(metaData.getColumnTypeName(num));
    return column;
  }
}
