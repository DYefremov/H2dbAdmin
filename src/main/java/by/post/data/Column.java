package by.post.data;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

/**
 * @author Dmitriy V.Yefremov
 */
public class Column {

    private String tableName;
    private String columnName;
    private String type;
    private BooleanProperty autoIncrement = new SimpleBooleanProperty(false);
    private BooleanProperty primaryKey = new SimpleBooleanProperty(false);
    private BooleanProperty notNull = new SimpleBooleanProperty(false);
    private String defaultValue;
    private boolean readOnly;
    private boolean caseSensitive;
    private boolean searchable;
    private boolean writable;
    private boolean signed;
    private int index;
    private int length;
    //It's used in view creation
    private String condition;

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

    public boolean isAutoIncrement() {
        return autoIncrement.get();
    }

    public BooleanProperty autoIncrementProperty() {
        return autoIncrement;
    }

    public void setAutoIncrement(boolean autoIncrement) {
        this.autoIncrement.set(autoIncrement);
    }

    public boolean isPrimaryKey() {
        return primaryKey.get();
    }

    public BooleanProperty primaryKeyProperty() {
        return primaryKey;
    }

    public void setPrimaryKey(boolean primaryKey) {
        this.primaryKey.set(primaryKey);
    }

    public boolean isNotNull() {
        return notNull.get();
    }

    public BooleanProperty notNullProperty() {
        return notNull;
    }

    public void setNotNull(boolean notNull) {
        this.notNull.set(notNull);
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
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

    public void setIndex(int index) {
        this.index = index;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    @Override
    public String toString() {
        return "Column{" +
                "tableName='" + tableName + '\'' +
                ", columnName='" + columnName + '\'' +
                ", type='" + type + '\'' +
                ", autoIncrement=" + autoIncrement +
                ", primaryKey=" + primaryKey +
                ", notNull=" + notNull +
                ", defaultValue='" + defaultValue + '\'' +
                ", readOnly=" + readOnly +
                ", caseSensitive=" + caseSensitive +
                ", searchable=" + searchable +
                ", writable=" + writable +
                ", signed=" + signed +
                ", index=" + index +
                ", length=" + length +
                ", condition='" + condition + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {

        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Column column = (Column) o;

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
        if (autoIncrement != null ? !autoIncrement.equals(column.autoIncrement) : column.autoIncrement != null)
            return false;
        if (primaryKey != null ? !primaryKey.equals(column.primaryKey) : column.primaryKey != null)
            return false;
        if (notNull != null ? !notNull.equals(column.notNull) : column.notNull != null)
            return false;
        if (defaultValue != null ? !defaultValue.equals(column.defaultValue) : column.defaultValue != null)
            return false;
        return condition != null ? condition.equals(column.condition) : column.condition == null;
    }

    @Override
    public int hashCode() {

        int result = tableName != null ? tableName.hashCode() : 0;
        result = 31 * result + (columnName != null ? columnName.hashCode() : 0);
        result = 31 * result + (type != null ? type.hashCode() : 0);
        result = 31 * result + (autoIncrement != null ? autoIncrement.hashCode() : 0);
        result = 31 * result + (primaryKey != null ? primaryKey.hashCode() : 0);
        result = 31 * result + (notNull != null ? notNull.hashCode() : 0);
        result = 31 * result + (defaultValue != null ? defaultValue.hashCode() : 0);
        result = 31 * result + (readOnly ? 1 : 0);
        result = 31 * result + (caseSensitive ? 1 : 0);
        result = 31 * result + (searchable ? 1 : 0);
        result = 31 * result + (writable ? 1 : 0);
        result = 31 * result + (signed ? 1 : 0);
        result = 31 * result + index;
        result = 31 * result + length;
        result = 31 * result + (condition != null ? condition.hashCode() : 0);

        return result;
    }
}