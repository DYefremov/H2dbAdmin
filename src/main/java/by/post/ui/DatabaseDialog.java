package by.post.ui;

import by.post.control.ui.DatabaseDialogController;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.Dialog;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.Map;

/**
 * @author Dmitriy V.Yefremov
 */
public class DatabaseDialog extends Dialog<Map<String, String>> {

    private DatabaseDialogController controller;

    private static final Logger logger = LogManager.getLogger(DatabaseDialog.class);

    public DatabaseDialog() {
        init();
    }

    private void init() {

        try {
            FXMLLoader loader = new FXMLLoader(DatabaseDialog.class.getResource("DatabaseDialogPane.fxml"));
            this.setDialogPane(loader.load());
            controller = loader.getController();
        } catch (IOException e) {
            logger.error("DatabaseDialog error[init]: " + e);
        }

        this.setResultConverter(dialogButton -> {
            ButtonBar.ButtonData data = dialogButton == null ? null : dialogButton.getButtonData();
            return data == ButtonBar.ButtonData.OK_DONE ? controller.getSettings() : null;
        });

        Stage stage = (Stage) getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image(Resources.LOGO_PATH));
    }
}
