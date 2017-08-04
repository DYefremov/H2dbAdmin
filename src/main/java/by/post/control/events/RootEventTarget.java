package by.post.control.events;

import javafx.event.*;

import java.util.ArrayList;
import java.util.List;

/**
 * This is mediator class and events target.
 */
public class RootEventTarget implements EventTarget {

    public static final RootEventTarget TARGET = new RootEventTarget();

    private static final List<EventHandler> tableSelectionHandlers = new ArrayList<>();

    private static final EventDispatchChain dispatchChain = new EventDispatchChain() {
        @Override
        public EventDispatchChain append(EventDispatcher ignore) {
            return null;
        }

        @Override
        public EventDispatchChain prepend(EventDispatcher ignore) {
            return null;
        }

        @Override
        public Event dispatchEvent(Event event) {

            //Table selection event
            if (event.getEventType().equals(TableSelectionEvent.TABLE_SELECTION_TYPE)) {
               tableSelectionHandlers.forEach(h -> h.handle(event));
               event.consume();
            }

            return event;
        }
    };

    @Override
    public EventDispatchChain buildEventDispatchChain(EventDispatchChain tail) {
        return dispatchChain;
    }

    public static void addTableSelectionHandler(EventHandler<TableSelectionEvent> handler) {
        tableSelectionHandlers.add(handler);
    }

    public static void removeTableSelectionHandler(EventHandler<TableSelectionEvent> handler) {
        tableSelectionHandlers.remove(handler);
    }


}
