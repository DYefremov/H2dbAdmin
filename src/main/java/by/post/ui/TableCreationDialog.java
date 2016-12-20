package by.post.ui;

import by.post.control.ui.TableCreationDialogController;
import by.post.data.Table;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.Optional;

/**
 * @author Dmitriy V.Yefremov
 */
public class TableCreationDialog extends Dialog<Table> {

    private TableCreationDialogController controller;

    private static final Logger logger = LogManager.getLogger(TableCreationDialog.class);

    public TableCreationDialog() {
        init();
    }

    private void init() {
        try {
            FXMLLoader loader = new FXMLLoader(TableCreationDialog.class.getResource("TableCreationDialogPane.fxml"));
            this.setDialogPane(loader.load());
            controller = loader.getController();
            Stage stage = (Stage)this.getDialogPane().getScene().getWindow();
            stage.getIcons().add(new Image(Resources.LOGO_PATH));
        } catch (IOException e) {
            logger.error("TableCreationDialog error[init]: " + e);
        }
        //Consume ok button event if canceled in confirmation dialog
        final Button okButton = (Button) getDialogPane().lookupButton(ButtonType.OK);
        okButton.addEventFilter(ActionEvent.ACTION, event -> {
            Optional<ButtonType> result = new ConfirmationDialog().showAndWait();
            if (result.get() != ButtonType.OK) {
                event.consume();
            }
        });

        setResultConverter((dialogButton) -> {
            ButtonBar.ButtonData data = dialogButton == null ? null : dialogButton.getButtonData();
            return data == ButtonBar.ButtonData.OK_DONE ? controller.getTable() : null;
        });
    }
}
