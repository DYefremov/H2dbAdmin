package by.post.control.ui.dialogs;

import by.post.data.Column;
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
public class ConditionCell extends TableCell<Column, String> {

    private HBox mainHbox;
    private ConditionBoxController controller;

    public ConditionCell() {
        initGraphic();
    }

    @Override
    public void startEdit() {
        super.startEdit();
    }

    @Override
    public void commitEdit(String value) {
        super.commitEdit(value);
    }

    public static Callback<TableColumn<Column, String>, TableCell<Column, String>> forTableColumn() {
        return list -> new ConditionCell();
    }

    private void initGraphic() {

        FXMLLoader loader = new FXMLLoader(MainUiForm.class.getResource("dialogs/ConditionBox.fxml"));
        setAlignment(Pos.CENTER_LEFT);

        try {
            mainHbox = loader.load();
            controller = loader.getController();
            controller.setCell(this);
            setGraphic(mainHbox);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
