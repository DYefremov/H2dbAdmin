package by.post.data;

import java.util.List;

/**
 * @author Dmitriy V.Yefremov
 */
public class Row implements Data {

    private int num;
    private String tableName;
    private List<Cell> cells;

    public Row() {

    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public List<Cell> getCells() {
        return cells;
    }

    public void setCells(List<Cell> cells) {
        this.cells = cells;
    }

    @Override
    public String toString() {
        return "Row{" +
                "num=" + num +
                ", tableName='" + tableName + '\'' +
                ", cells=" + cells +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Row row = (Row) o;

        if (num != row.num) return false;
        if (tableName != null ? !tableName.equals(row.tableName) : row.tableName != null)
            return false;
        return cells != null ? cells.equals(row.cells) : row.cells == null;

    }

    @Override
    public int hashCode() {
        int result = num;
        result = 31 * result + (tableName != null ? tableName.hashCode() : 0);
        result = 31 * result + (cells != null ? cells.hashCode() : 0);
        return result;
    }

    @Override
    public Object getData() {
        return cells;
    }
}
