package by.post.control.events;

import by.post.data.Table;
import javafx.event.Event;
import javafx.event.EventType;

public class TableSelectionEvent extends Event {

    private final Table table;

    public TableSelectionEvent(Table table) {
        super(TABLE_SELECTION_TYPE);
        this.table = table;
    }

    public Table getTable() {
        return table;
    }

    public static final EventType<TableSelectionEvent> TABLE_SELECTION_TYPE = new EventType<>(Event.ANY, "TABLE_SELECTION_TYPE");
}

