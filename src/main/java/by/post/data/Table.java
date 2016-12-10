package by.post.data;

import java.util.Collection;
import java.util.List;

/**
 * @author Dmitriy V.Yefremov
 */
public class Table implements Data {

    private String name;
    private String primaryKey;
    private List<Column> columns;
    private List<Row> rows;

    public Table() {
    }

    public Table(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrimaryKey() {
        return primaryKey;
    }

    public void setPrimaryKey(String primaryKey) {
        this.primaryKey = primaryKey;
    }

    public List<Column> getColumns() {
        return columns;
    }

    public void setColumns(List<Column> columns) {
        this.columns = columns;
    }

    public List<Row> getRows() {
        return rows;
    }

    public void setRows(List<Row> rows) {
        this.rows = rows;
    }

    @Override
    public String toString() {
        return "Table{" +
                "name='" + name + '\'' +
                ", primaryKey='" + primaryKey + '\'' +
                ", columns=" + columns +
                ", rows=" + rows +
                '}';
    }

    @Override
    public Collection getData() {
        return rows;
    }
}
