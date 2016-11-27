package by.post.control.db;

import by.post.control.ui.OpenFileDialogProvider;
import javafx.scene.control.Alert;
import javafx.scene.control.TableCell;

import java.io.File;

/**
 * @author Dmitriy V.Yefremov
 */
public class LobDataManager {

    public LobDataManager() {

    }

    /**
     * @param cell
     */
    public void save(TableCell cell) {

        File file = new OpenFileDialogProvider().getSaveFileDialog("Save to file");

        if (file != null) {
            new Alert(Alert.AlertType.ERROR, "Not implemented!").showAndWait();
        }
    }
}
