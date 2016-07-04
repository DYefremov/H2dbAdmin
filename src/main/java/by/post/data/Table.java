package by.post.data;

import java.util.List;

/**
 * @author Dmitriy V.Yefremov
 */
public class Table implements Data {
    private String name;
    private String primaryKey;
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

    public List<Row> getRows() {
        return rows;
    }

    public void setRows(List<Row> rows) {
        this.rows = rows;
    }
}
