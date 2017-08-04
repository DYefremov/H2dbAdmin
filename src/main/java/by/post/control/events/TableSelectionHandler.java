package by.post.control.events;

import by.post.data.Table;
import javafx.event.EventHandler;

public interface TableSelectionHandler extends EventHandler<TableSelectionEvent> {

    @Override
    default void handle(TableSelectionEvent event) {
        selectTable(event.getTable());
    }

    void selectTable(Table table);

}
