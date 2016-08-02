package by.post.ui;

import by.post.control.ui.AddColumnDialogController;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.util.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

/**
 * @author Dmitriy V.Yefremov
 */
public class AddColumnDialog extends Dialog<Pair<String, String>> {

    private FXMLLoader loader;
    private DialogPane parent;
    private AddColumnDialogController controller;

    private static final Logger logger = LogManager.getLogger(AddColumnDialog.class);

    public AddColumnDialog() {
        init();
    }

    private void init() {

        try {
            loader = new FXMLLoader(AddColumnDialog.class.getResource("AddColumnDialog.fxml"));
            parent = loader.<DialogPane>load();
            controller = loader.getController();
        } catch (IOException e) {
            logger.error("AddColumnDialog create error: " + e);
        }

        parent.setHeaderText("Please, set name and type of column!");
        parent.getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        setDialogPane(parent);

        setResultConverter((dialogButton) -> {
            String name = controller.getName();
            String type = controller.getType();
            ButtonBar.ButtonData data = dialogButton == null ? null : dialogButton.getButtonData();
            return data == ButtonBar.ButtonData.OK_DONE ? new Pair(name, type) : null;
        });

        Stage stage = (Stage) getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image(Resources.LOGO_PATH));
    }

}
