package by.post.control.ui;

import by.post.control.db.TableType;
import javafx.scene.control.TreeItem;
import javafx.scene.image.ImageView;

/**
 * Custom implementation of TreeItem
 */
public class TypedTreeItem extends TreeItem {

    private TableType type;
    private boolean root;

    public TypedTreeItem(final String value, final ImageView graphic, final TableType type, final boolean root) {
        super(value, graphic);
        this.type = type;
        this.root = root;
    }

    public TableType getType() {
        return type;
    }

    public boolean isRoot() {
        return root;
    }
}
