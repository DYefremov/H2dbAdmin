package by.post.ui;


import by.post.control.Context;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Dialog;

import java.io.IOException;
import java.util.ResourceBundle;

/**
 * @author Dmitriy V.Yefremov
 */
public class DataSelectionDialog extends Dialog {

    public DataSelectionDialog() {
        init();
    }

    private void init() {

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("dialogs/DataSelectionDialogPane.fxml"));
            loader.setResources(ResourceBundle.getBundle("bundles.Lang", Context.getLocale()));
            this.setDialogPane(loader.load());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
