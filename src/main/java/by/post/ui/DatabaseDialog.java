package by.post.ui;

import by.post.control.ui.DatabaseDialogController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
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

    private FXMLLoader loader;
    private Parent parent;
    private DatabaseDialogController controller;

    private static final Logger logger = LogManager.getLogger(OpenDbDialog.class);

    public DatabaseDialog() {
        init();
    }

    private void init() {

        loader = new FXMLLoader(OpenDbDialog.class.getResource("DatabaseDialog.fxml"));

        try {
            parent = loader.<DialogPane>load();
        } catch (IOException e) {
            logger.error("OpenDbDialog error: " + e);
        }

        controller = loader.getController();

        this.setDialogPane((DialogPane) parent);
        ((DialogPane) parent).getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        this.setTitle("Setup connection...");

        setResultConverter((dialogButton) -> {
            ButtonBar.ButtonData data = dialogButton == null ? null : dialogButton.getButtonData();
            return data == ButtonBar.ButtonData.OK_DONE ? controller.getSettings() : null;
        });

        Stage stage = (Stage) getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image(Resources.LOGO_PATH));
    }
}
