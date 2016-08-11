package by.post.ui;

import by.post.control.ui.AddColumnDialogController;
import by.post.data.Column;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

/**
 * @author Dmitriy V.Yefremov
 */
public class AddColumnDialog extends Dialog<Column> {

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
        setTitle(Resources.TITLE);

        setResultConverter((dialogButton) -> {
            String name = controller.getName();
            String type = controller.getType();
            ButtonBar.ButtonData data = dialogButton == null ? null : dialogButton.getButtonData();
            Column column = new Column(name, type);
            column.setPrimaryKey(controller.getPrimaryKey());
            column.setNotNull(controller.getNotNull());

            return data == ButtonBar.ButtonData.OK_DONE ? column : null;
        });

        Stage stage = (Stage) getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image(Resources.LOGO_PATH));
    }

}
