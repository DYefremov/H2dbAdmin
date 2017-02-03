package by.post.data;

/**
 * @author Dmitriy V.Yefremov
 */
public class Cell implements Data {
    // 0 == NULL
    private int type;
    private String columnName;
    private String value;

    public Cell() {

    }

    public Cell(int type, String columnName,  String value) {
        this.type = type;
        this.columnName = columnName;
        this.value = value;
    }

    public String getName() {
        return columnName;
    }

    public void setName(String name) {
        this.columnName = name;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public Object getData() {
        return value;
    }

    @Override
    public boolean equals(Object o) {

        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Cell cell = (Cell) o;

        if (type != cell.type) return false;
        if (columnName != null ? !columnName.equals(cell.columnName) : cell.columnName != null)
            return false;
        return value != null ? value.equals(cell.value) : cell.value == null;
    }

    @Override
    public int hashCode() {

        int result = type;
        result = 31 * result + (columnName != null ? columnName.hashCode() : 0);
        result = 31 * result + (value != null ? value.hashCode() : 0);

        return result;
    }

    @Override
    public String toString() {

        return "Cell{" +
                "type=" + type +
                ", columnName='" + columnName + '\'' +
                ", value='" + value + '\'' +
                '}';
    }
}
