package by.post.ui;


import by.post.control.Context;
import by.post.control.ui.dialogs.DataSelectionDialogController;
import by.post.data.Table;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Dialog;

import java.io.IOException;
import java.util.ResourceBundle;

/**
 * @author Dmitriy V.Yefremov
 */
public class DataSelectionDialog extends Dialog<Table> {

    private Table table;
    private DataSelectionDialogController controller;

    public DataSelectionDialog() {
        init();
    }

    public DataSelectionDialog(Table table) {
        this.table = table;
        init();
    }
    private void init() {

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("dialogs/DataSelectionDialogPane.fxml"));
            loader.setResources(ResourceBundle.getBundle("bundles.Lang", Context.getLocale()));
            setDialogPane(loader.load());
            controller = loader.getController();
            controller.setTable(table);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
