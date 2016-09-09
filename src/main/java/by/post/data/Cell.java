package by.post.data;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 * @author Dmitriy V.Yefremov
 */
public class Cell implements Data {

    private SimpleStringProperty name;
    private SimpleStringProperty type;
    private SimpleObjectProperty value;

    public Cell() {
        this.name = new SimpleStringProperty();
        this.type = new SimpleStringProperty();
        this.value = new SimpleObjectProperty();
    }

    public Cell(String name, String type, Object value) {
        this.name = new SimpleStringProperty(name);
        this.type = new SimpleStringProperty(type);
        this.value = new SimpleObjectProperty(value);
    }

    public String getName() {
        return name.get();
    }

    public void setName(String name) {
        this.name.set(name != null ? name : "");
    }

    public String getType() {
        return type.get();
    }

    public void setType(String type) {
        this.type.set(type);
    }

    public Object getValue() {
        return value.get();
    }


    public void setValue(Object value) {
        this.value.set(value);
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

        if (name != null ? !name.getValue().equals(cell.name.getValue()) : cell.name != null)
            return false;
        if (type != null ? !type.getValue().equals(cell.type.getValue()) : cell.type != null)
            return false;
        return value != null ? value.getValue().equals(cell.value.getValue()) : cell.value == null;
    }

    @Override
    public int hashCode() {

        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (type != null ? type.hashCode() : 0);
        result = 31 * result + (value != null ? value.hashCode() : 0);

        return result;
    }

    @Override
    public String toString() {
        return "Cell{" +
                "name=" + name +
                ", type=" + type +
                ", value=" + value +
                '}';
    }
}
