package by.post.control.ui;

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

    private void init() {

        changeName = new MenuItem("Change name");
        changeType = new MenuItem("Change type");
        this.getItems().addAll(changeName, changeType);

    }
}
