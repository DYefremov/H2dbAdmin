package by.post.ui;

import by.post.control.Context;
import by.post.control.ui.dialogs.OpenDbDialogController;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * @author Dmitriy V.Yefremov
 */
public class OpenDbDialog extends Dialog<Map<String, String>> {

    private FXMLLoader loader;
    private Parent parent;
    private OpenDbDialogController controller;

    public OpenDbDialog() throws IOException {
        init();
    }

    private void init() throws IOException {

        loader = new FXMLLoader(getClass().getResource("dialogs/OpenDbDialog.fxml"));
        loader.setResources(ResourceBundle.getBundle("bundles.Lang", Context.getLocale()));
        parent = loader.<DialogPane>load();
        controller = loader.getController();

        setDialogPane((DialogPane) parent);
        setTitle("Setup connection...");

        Stage stage = (Stage) getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image(Resources.LOGO_PATH));

        initButtons();
    }

    private void initButtons() {
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
            return data == ButtonBar.ButtonData.OK_DONE ? controller.getSettings() : null;
        });
    }

}
