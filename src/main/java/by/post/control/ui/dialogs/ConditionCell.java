package by.post.control.ui.dialogs;

import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.util.Callback;

/**
 * Custom implementation of Table Cell for condition construction
 *
 * @author Dmitriy V.Yefremov
 */
public class ConditionCell extends TableCell {


    public ConditionCell() {

    }

    @Override
    public void updateItem(Object item, boolean empty) {

        super.updateItem(item, empty);
    }

    @Override
    public void startEdit() {

        if (!isEditable() || !getTableView().isEditable() || !getTableColumn().isEditable()) {
            return;
        }

        super.startEdit();
        setText(null);
    }

    @Override
    public void cancelEdit() {

        super.cancelEdit();
    }

    public static <S> Callback<TableColumn<S,String>, TableCell<S,String>> forTableColumn() {
        return list -> new ConditionCell();
    }
}
