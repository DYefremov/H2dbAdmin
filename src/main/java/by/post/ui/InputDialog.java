package by.post.ui;

import javafx.scene.control.TextInputDialog;
import javafx.scene.image.Image;
import javafx.stage.Stage;

/**
 * @author Dmitriy V.Yefremov
 */
public class InputDialog extends TextInputDialog {
    public InputDialog(){
        super("New table");
        setTitle("H2dbAdmin");
        setHeaderText("Please, write table name!");
        Stage stage =  (Stage) getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image(Resources.ICON_PATH));
    }

}
