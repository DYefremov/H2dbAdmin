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
public class ColumnEditContextMenu extends ContextMenu {

    private MenuItem addColumn;
    private MenuItem changeName;
    private MenuItem changeType;
    private MenuItem delColumn;
    private SeparatorMenuItem separator;
    private SeparatorMenuItem separator2;

    public ColumnEditContextMenu() {
        init();
    }

    public ColumnEditContextMenu(String id){
        setId(id);
        init();
    }

    private void init() {

        addColumn = new MenuItem("Add column");
        changeName = new MenuItem("Change name");
        changeType = new MenuItem("Change type");
        delColumn = new MenuItem("Delete column");
        separator = new SeparatorMenuItem();
        separator2 = new SeparatorMenuItem();

        this.getItems().addAll(addColumn, separator, changeName, changeType, separator2, delColumn);

        changeName.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                TableEditor.getInstance().changeColumnName(getId());
            }
        });

        changeType.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                TableEditor.getInstance().changeColumnType(getId());
            }
        });

    }
}
