package by.post.data;

/**
 * @author Dmitriy V.Yefremov
 */
public class Column {

    private String name;
    private int type;
    private boolean isPrimaryKey;
    private boolean isNotNull;

    public Column() {

    }

    public Column(String name, int type) {
        this.name = name;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public boolean isPrimaryKey() {
        return isPrimaryKey;
    }

    public void setPrimaryKey(boolean primaryKey) {
        isPrimaryKey = primaryKey;
    }

    public boolean isNotNull() {
        return isNotNull;
    }

    public void setNotNull(boolean notNull) {
        isNotNull = notNull;
    }

    @Override
    public String toString() {

        return "Column{" +
                "name='" + name + '\'' +
                ", type=" + type +
                ", isPrimaryKey=" + isPrimaryKey +
                ", isNotNull=" + isNotNull +
                '}';
    }

    @Override
    public boolean equals(Object o) {

        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Column column = (Column) o;

        if (type != column.type) return false;
        if (isPrimaryKey != column.isPrimaryKey) return false;
        if (isNotNull != column.isNotNull) return false;

        return name != null ? name.equals(column.name) : column.name == null;

    }

    @Override
    public int hashCode() {

        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + type;
        result = 31 * result + (isPrimaryKey ? 1 : 0);
        result = 31 * result + (isNotNull ? 1 : 0);

        return result;
    }
}
