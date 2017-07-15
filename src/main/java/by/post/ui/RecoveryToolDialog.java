package by.post.ui;

import by.post.control.Context;
import by.post.control.ui.dialogs.RecoveryPaneController;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ResourceBundle;

/**
 * @author Dmitriy V.Yefremov
 */
public class RecoveryToolDialog extends Dialog {

    private RecoveryPaneController controller;

    public RecoveryToolDialog() throws IOException {
        init();
    }

    private void init() throws IOException {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("dialogs/RecoveryDialogPane.fxml"));
        loader.setResources(ResourceBundle.getBundle("bundles.Lang", Context.getLocale()));
        setDialogPane(loader.load());
        controller = loader.getController();
        Stage stage = (Stage)this.getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image(Resources.LOGO_PATH));
        setResizable(false);

        getDialogPane().getScene().getWindow().setOnCloseRequest(event -> {
            if (!closeDialog()) {
                event.consume();
            }
        });

        setOnCloseRequest(event -> {
            event.consume();
            closeDialog();
        });
        //Sets not resizable after click on details
        getDialogPane().expandedProperty().addListener((observable, oldValue, newValue) ->
                Platform.runLater(() -> this.setResizable(false)));
    }

    private boolean closeDialog() {

        if (new ConfirmationDialog().showAndWait().get() != ButtonType.OK) {
            return false;
        }

        controller.onCancel();

        return true;
    }

}
