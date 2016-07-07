package by.post.ui;

import by.post.data.ColumnDataType;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.image.Image;
import javafx.stage.Stage;

/**
 * @author Dmitriy V.Yefremov
 */
public class ChoiceColumnTypeDialog extends ChoiceDialog {

    public ChoiceColumnTypeDialog() {
        super(ColumnDataType.getType(12), ColumnDataType.getTypes().values());
        init();
    }

    private void init() {
        setTitle(Resources.TITLE);
        setHeaderText("");
        setContentText("Please, select type for you column");

        Stage stage =  (Stage) getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image(Resources.LOGO_PATH));
    }
}
