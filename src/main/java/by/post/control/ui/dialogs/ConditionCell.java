package by.post.control.ui.dialogs;

import by.post.ui.MainUiForm;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.layout.HBox;
import javafx.util.Callback;

import java.io.IOException;

/**
 * Custom implementation of Table Cell for condition construction
 *
 * @author Dmitriy V.Yefremov
 */
public class ConditionCell extends TableCell {

    private HBox mainHbox;
    private ConditionBoxController conditionBoxController;

    public ConditionCell() {
        initGraphic();
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

    private void initGraphic() {

        FXMLLoader loader = new FXMLLoader(MainUiForm.class.getResource("dialogs/ConditionBox.fxml"));
        setAlignment(Pos.CENTER_LEFT);

        try {
            mainHbox = loader.load();
            conditionBoxController = loader.getController();
            setGraphic(mainHbox);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
