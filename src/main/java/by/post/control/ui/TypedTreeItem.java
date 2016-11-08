package by.post.control.ui;

import by.post.control.db.TableType;
import javafx.scene.control.TreeItem;
import javafx.scene.image.ImageView;

/**
 * Custom implementation of TreeItem
 */
public class TypedTreeItem extends TreeItem {

    TableType type;

    public TypedTreeItem(final String value, final ImageView graphic, final TableType type) {
        super(value, graphic);
        this.type = type;
    }

    public TableType getType() {
        return type;
    }
}
