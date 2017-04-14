package by.post.data;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 * This class used in Triggers tool as data model
 *
 * @author Dmitriy V.Yefremov
 */
public class Trigger {

    private SimpleStringProperty name;
    private SimpleStringProperty type;
    private SimpleStringProperty tableName;
    private SimpleStringProperty javaClass;
    private SimpleStringProperty remarks;
    private SimpleIntegerProperty id;
    private SimpleIntegerProperty queueSize;
    private SimpleBooleanProperty before;
    private SimpleBooleanProperty noWait;

    public Trigger() {

        this.name = new SimpleStringProperty();
        this.type = new SimpleStringProperty();
        this.tableName = new SimpleStringProperty();
        this.javaClass = new SimpleStringProperty();
        this.remarks = new SimpleStringProperty();
        this.id = new SimpleIntegerProperty();
        this.queueSize = new SimpleIntegerProperty();
        this.before = new SimpleBooleanProperty(false);
        this.noWait = new SimpleBooleanProperty(false);
    }

    public String getName() {
        return name.get();
    }

    public SimpleStringProperty nameProperty() {
        return name;
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public String getType() {
        return type.get();
    }

    public SimpleStringProperty typeProperty() {
        return type;
    }

    public void setType(String type) {
        this.type.set(type);
    }

    public String getTableName() {
        return tableName.get();
    }

    public SimpleStringProperty tableNameProperty() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName.set(tableName);
    }

    public String getJavaClass() {
        return javaClass.get();
    }

    public SimpleStringProperty javaClassProperty() {
        return javaClass;
    }

    public void setJavaClass(String javaClass) {
        this.javaClass.set(javaClass);
    }

    public String getRemarks() {
        return remarks.get();
    }

    public SimpleStringProperty remarksProperty() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks.set(remarks);
    }

    public int getId() {
        return id.get();
    }

    public SimpleIntegerProperty idProperty() {
        return id;
    }

    public void setId(int id) {
        this.id.set(id);
    }

    public int getQueueSize() {
        return queueSize.get();
    }

    public SimpleIntegerProperty queueSizeProperty() {
        return queueSize;
    }

    public void setQueueSize(int queueSize) {
        this.queueSize.set(queueSize);
    }

    public boolean isBefore() {
        return before.get();
    }

    public SimpleBooleanProperty beforeProperty() {
        return before;
    }

    public void setBefore(boolean before) {
        this.before.set(before);
    }

    public boolean isNoWait() {
        return noWait.get();
    }

    public SimpleBooleanProperty noWaitProperty() {
        return noWait;
    }

    public void setNoWait(boolean noWait) {
        this.noWait.set(noWait);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Trigger trigger = (Trigger) o;

        if (name != null ? !name.equals(trigger.name) : trigger.name != null)
            return false;
        if (type != null ? !type.equals(trigger.type) : trigger.type != null)
            return false;
        if (tableName != null ? !tableName.equals(trigger.tableName) : trigger.tableName != null)
            return false;
        if (javaClass != null ? !javaClass.equals(trigger.javaClass) : trigger.javaClass != null)
            return false;
        if (remarks != null ? !remarks.equals(trigger.remarks) : trigger.remarks != null)
            return false;
        if (id != null ? !id.equals(trigger.id) : trigger.id != null)
            return false;
        if (queueSize != null ? !queueSize.equals(trigger.queueSize) : trigger.queueSize != null)
            return false;
        if (before != null ? !before.equals(trigger.before) : trigger.before != null)
            return false;
        return noWait != null ? noWait.equals(trigger.noWait) : trigger.noWait == null;
    }

    @Override
    public int hashCode() {

        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (type != null ? type.hashCode() : 0);
        result = 31 * result + (tableName != null ? tableName.hashCode() : 0);
        result = 31 * result + (javaClass != null ? javaClass.hashCode() : 0);
        result = 31 * result + (remarks != null ? remarks.hashCode() : 0);
        result = 31 * result + (id != null ? id.hashCode() : 0);
        result = 31 * result + (queueSize != null ? queueSize.hashCode() : 0);
        result = 31 * result + (before != null ? before.hashCode() : 0);
        result = 31 * result + (noWait != null ? noWait.hashCode() : 0);

        return result;
    }

    @Override
    public String toString() {
        return "Trigger{" +
                "name=" + name +
                ", type=" + type +
                ", tableName=" + tableName +
                ", javaClass=" + javaClass +
                ", remarks=" + remarks +
                ", id=" + id +
                ", queueSize=" + queueSize +
                ", before=" + before +
                ", noWait=" + noWait +
                '}';
    }
}
