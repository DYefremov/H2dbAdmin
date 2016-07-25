package by.post.ui;

import by.post.control.ui.TableEditor;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;

/**
 * @author Dmitriy V.Yefremov
 */
public class ColumnContextMenu extends ContextMenu {

    private MenuItem addColumn;
    private MenuItem changeName;
    private MenuItem changeType;
    private MenuItem delColumn;
    private SeparatorMenuItem separator;
    private SeparatorMenuItem separator2;
    private TableEditor tableEditor;

    public ColumnContextMenu() {
        init();
    }

    public ColumnContextMenu(String id) {
        setId(id);
        init();
    }

    private void init() {

        tableEditor = TableEditor.getInstance();

        addColumn = new MenuItem("Add column");
        changeName = new MenuItem("Change name");
        changeType = new MenuItem("Change type");
        delColumn = new MenuItem("Delete column");
        separator = new SeparatorMenuItem();
        separator2 = new SeparatorMenuItem();

        changeName.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                tableEditor.changeColumnName(getId());
            }
        });

        changeType.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                tableEditor.changeColumnType(getId());
            }
        });

        addColumn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                tableEditor.addColumn();
            }
        });

        delColumn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                tableEditor.deleteColumn(getId());
            }
        });

        this.getItems().addAll(addColumn, separator, changeName, changeType, separator2, delColumn);
    }
}
