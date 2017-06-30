package by.post.data;

import by.post.control.db.TableType;

import java.util.List;

/**
 * @author Dmitriy V.Yefremov
 */
public class Table {

    private String name;
    private TableType type;
    private String primaryKey;
    private List<Column> columns;
    private List<Row> rows;
    //Indicates that the update request
    private boolean update;
    //Indicates whether the update request was successful
    private boolean updated;

    public Table() {
    }

    public Table(String name) {
        this.name = name;
    }

    public Table(String name, TableType type) {
        this.name = name;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public TableType getType() {
        return type;
    }

    public void setType(TableType type) {
        this.type = type;
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

    public boolean isUpdated() {
        return updated;
    }

    public void setUpdated(boolean updated) {
        this.updated = updated;
    }

    public boolean isUpdate() {
        return update;
    }

    public void setUpdate(boolean update) {
        this.update = update;
    }

    @Override
    public String toString() {
        return "Table{" +
                "name='" + name + '\'' +
                ", type=" + type +
                ", primaryKey='" + primaryKey + '\'' +
                ", columns=" + columns +
                ", rows=" + rows +
                ", update=" + update +
                ", updated=" + updated +
                '}';
    }
}
