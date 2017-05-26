package by.post.ui;

import by.post.control.Context;
import by.post.control.Settings;
import by.post.control.ui.dialogs.ColumnPropertiesDialogController;
import by.post.data.Column;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
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

    public ColumnPropertiesDialog(Column column) {
       this.column = column;
       init();
    }

    private void init(){

        FXMLLoader loader = new FXMLLoader(getClass().getResource("dialogs/ColumnPropertiesDialogPane.fxml"));
        loader.setResources(ResourceBundle.getBundle("bundles.Lang", Context.getLocale()));
        try {
            this.setDialogPane(loader.load());
        } catch (IOException e) {
            e.printStackTrace();
        }
        controller = loader.getController();
        controller.setData(column);

        Stage stage = (Stage)this.getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image(Resources.LOGO_PATH));
        //Setting translation for close button
        boolean defLang = Context.getLocale().getLanguage().equals(Settings.DEFAULT_LANG);
        Button closeButton = (Button) getDialogPane().lookupButton(ButtonType.CLOSE);
        closeButton.setText(defLang ? ButtonType.CLOSE.getText() : "Закрыть");
    }
}
