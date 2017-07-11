package by.post.ui;

import by.post.control.Context;
import by.post.control.ui.dialogs.ColumnPropertiesDialogController;
import by.post.data.Column;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Dialog;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ResourceBundle;

/**
 * @author Dmitriy V.Yefremov
 */
public class ColumnPropertiesDialog extends Dialog {

    private ColumnPropertiesDialogController controller;
    private Column column;

    public ColumnPropertiesDialog(Column column) throws IOException {
       this.column = column;
       init();
    }

    private void init() throws IOException {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("dialogs/ColumnPropertiesDialogPane.fxml"));
        loader.setResources(ResourceBundle.getBundle("bundles.Lang", Context.getLocale()));
        setDialogPane(loader.load());
        controller = loader.getController();
        controller.setData(column);

        Stage stage = (Stage)this.getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image(Resources.LOGO_PATH));
    }
}
