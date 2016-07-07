package by.post.ui;

import by.post.control.ui.TableEditor;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;

/**
 * @author Dmitriy V.Yefremov
 */
public class ColumnEditContextMenu extends ContextMenu {

    private MenuItem changeName;
    private MenuItem changeType;

    public ColumnEditContextMenu() {
        init();
    }

    public ColumnEditContextMenu(String id){
        setId(id);
        init();
    }

    private void init() {

        changeName = new MenuItem("Change name");
        changeType = new MenuItem("Change type");
        this.getItems().addAll(changeName, changeType);

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
