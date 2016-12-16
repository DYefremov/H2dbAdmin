package by.post.data;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 *This class used in ViewCreationDialogController as data model
 *
 * @author Dmitriy V.Yefremov
 */
public class ConditionRow {

    private SimpleStringProperty name;
    private SimpleStringProperty condition;
    private SimpleBooleanProperty selected;

    public ConditionRow() {
        this.name = new SimpleStringProperty();
        this.condition = new SimpleStringProperty();
        this.selected = new SimpleBooleanProperty(false);
    }

    public ConditionRow(String name, String condition, boolean selected) {
        this.name = new SimpleStringProperty(name);
        this.condition = new SimpleStringProperty(condition);
        this.selected = new SimpleBooleanProperty(selected);
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

    public String getCondition() {
        return condition.get();
    }

    public SimpleStringProperty conditionProperty() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition.set(condition);
    }

    public boolean isSelected() {
        return selected.get();
    }

    public SimpleBooleanProperty selectedProperty() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected.set(selected);
    }

    @Override
    public String toString() {
        return "ConditionRow{" +
                "name=" + name +
                ", condition=" + condition +
                ", selected=" + selected +
                '}';
    }
}
