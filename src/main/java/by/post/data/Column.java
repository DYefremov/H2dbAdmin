package by.post.data;

/**
 * @author Dmitriy V.Yefremov
 */
public class Column {

    private String name;
    private String type;
    private boolean primaryKey;
    private boolean notNull;
    private boolean autoIncrement;
    private boolean readOnly;
    private boolean caseSensitive;
    private boolean searchable;
    private boolean writable;
    private boolean signed;

    public Column() {

    }

    public Column(String name, String type) {
        this.name = name;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isPrimaryKey() {
        return primaryKey;
    }

    public void setPrimaryKey(boolean primaryKey) {
        this.primaryKey = primaryKey;
    }

    public boolean isNotNull() {
        return notNull;
    }

    public void setNotNull(boolean notNull) {
        this.notNull = notNull;
    }

    public boolean isAutoIncrement() {
        return autoIncrement;
    }

    public void setAutoIncrement(boolean autoIncrement) {
        this.autoIncrement = autoIncrement;
    }

    public boolean isReadOnly() {
        return readOnly;
    }

    public void setReadOnly(boolean readOnly) {
        this.readOnly = readOnly;
    }

    public boolean isCaseSensitive() {
        return caseSensitive;
    }

    public void setCaseSensitive(boolean caseSensitive) {
        this.caseSensitive = caseSensitive;
    }

    public boolean isSearchable() {
        return searchable;
    }

    public void setSearchable(boolean searchable) {
        this.searchable = searchable;
    }

    public boolean isWritable() {
        return writable;
    }

    public void setWritable(boolean writable) {
        this.writable = writable;
    }

    public boolean isSigned() {
        return signed;
    }

    public void setSigned(boolean signed) {
        this.signed = signed;
    }

    @Override
    public String toString() {
        return "Column{" +
                "name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", primaryKey=" + primaryKey +
                ", notNull=" + notNull +
                ", autoIncrement=" + autoIncrement +
                ", readOnly=" + readOnly +
                ", caseSensitive=" + caseSensitive +
                ", searchable=" + searchable +
                ", writable=" + writable +
                ", signed=" + signed +
                '}';
    }

    @Override
    public boolean equals(Object o) {

        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Column column = (Column) o;

        if (primaryKey != column.primaryKey) return false;
        if (notNull != column.notNull) return false;
        if (autoIncrement != column.autoIncrement) return false;
        if (readOnly != column.readOnly) return false;
        if (caseSensitive != column.caseSensitive) return false;
        if (searchable != column.searchable) return false;
        if (writable != column.writable) return false;
        if (signed != column.signed) return false;
        if (name != null ? !name.equals(column.name) : column.name != null)
            return false;
        return type != null ? type.equals(column.type) : column.type == null;

    }

    @Override
    public int hashCode() {

        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (type != null ? type.hashCode() : 0);
        result = 31 * result + (primaryKey ? 1 : 0);
        result = 31 * result + (notNull ? 1 : 0);
        result = 31 * result + (autoIncrement ? 1 : 0);
        result = 31 * result + (readOnly ? 1 : 0);
        result = 31 * result + (caseSensitive ? 1 : 0);
        result = 31 * result + (searchable ? 1 : 0);
        result = 31 * result + (writable ? 1 : 0);
        result = 31 * result + (signed ? 1 : 0);

        return result;
    }
}
