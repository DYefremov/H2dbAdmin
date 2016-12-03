package by.post.data;

/**
 * @author Dmitriy V.Yefremov
 */
public class Column {

    private String tableName;
    private String columnName;
    private String type;
    private String defaultValue;
    private boolean primaryKey;
    private boolean notNull;
    private boolean autoIncrement;
    private boolean readOnly;
    private boolean caseSensitive;
    private boolean searchable;
    private boolean writable;
    private boolean signed;
    private int index;
    private int length;


    public Column() {

    }

    public Column(String tableName, String columnName, String type) {
        this.tableName = tableName;
        this.columnName = columnName;
        this.type = type;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
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

    public int getIndex() {
        return index;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    @Override
    public String toString() {
        return "Column{" +
                "tableName='" + tableName + '\'' +
                ", columnName='" + columnName + '\'' +
                ", type='" + type + '\'' +
                ", defaultValue='" + defaultValue + '\'' +
                ", primaryKey=" + primaryKey +
                ", notNull=" + notNull +
                ", autoIncrement=" + autoIncrement +
                ", readOnly=" + readOnly +
                ", caseSensitive=" + caseSensitive +
                ", searchable=" + searchable +
                ", writable=" + writable +
                ", signed=" + signed +
                ", index=" + index +
                ", length=" + length +
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
        if (index != column.index) return false;
        if (length != column.length) return false;
        if (tableName != null ? !tableName.equals(column.tableName) : column.tableName != null)
            return false;
        if (columnName != null ? !columnName.equals(column.columnName) : column.columnName != null)
            return false;
        if (type != null ? !type.equals(column.type) : column.type != null)
            return false;
        return defaultValue != null ? defaultValue.equals(column.defaultValue) : column.defaultValue == null;
    }

    @Override
    public int hashCode() {

        int result = tableName != null ? tableName.hashCode() : 0;
        result = 31 * result + (columnName != null ? columnName.hashCode() : 0);
        result = 31 * result + (type != null ? type.hashCode() : 0);
        result = 31 * result + (defaultValue != null ? defaultValue.hashCode() : 0);
        result = 31 * result + (primaryKey ? 1 : 0);
        result = 31 * result + (notNull ? 1 : 0);
        result = 31 * result + (autoIncrement ? 1 : 0);
        result = 31 * result + (readOnly ? 1 : 0);
        result = 31 * result + (caseSensitive ? 1 : 0);
        result = 31 * result + (searchable ? 1 : 0);
        result = 31 * result + (writable ? 1 : 0);
        result = 31 * result + (signed ? 1 : 0);
        result = 31 * result + index;
        result = 31 * result + length;

        return result;
    }
}