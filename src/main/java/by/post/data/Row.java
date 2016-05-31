package by.post.data;

import java.util.List;

/**
 * @author Dmitriy V.Yefremov
 */
public class Row implements Data {
  private int num;
  private List<Column> columns;

  public Row() {
  }

  public int getNum() {
    return num;
  }

  public void setNum(int num) {
    this.num = num;
  }

  public List<Column> getColumns() {
    return columns;
  }

  public void setColumns(List<Column> columns) {
    this.columns = columns;
  }

  @Override
  public String toString() {
    return "Row{" +
        "num=" + num +
        ", columns=" + columns +
        '}';
  }
}
