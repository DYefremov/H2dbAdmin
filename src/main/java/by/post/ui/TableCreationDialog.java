package by.post.ui;

import by.post.control.Context;
import by.post.control.Settings;
import by.post.control.ui.dialogs.TableCreationDialogController;
import by.post.data.Table;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * @author Dmitriy V.Yefremov
 */
public class TableCreationDialog extends Dialog<Table> {

    private TableCreationDialogController controller;

    public TableCreationDialog() throws IOException {
        init();
    }

    private void init() throws IOException {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("dialogs/TableCreationDialogPane.fxml"));
        loader.setResources(ResourceBundle.getBundle("bundles.Lang", Context.getLocale()));
        this.setDialogPane(loader.load());
        controller = loader.getController();
        Stage stage = (Stage)this.getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image(Resources.LOGO_PATH));
        initElements();
    }

    private void initElements() {
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
        //Setting translation
        boolean defLang = Context.getLocale().getLanguage().equals(Settings.DEFAULT_LANG);
        okButton.setText(defLang ? ButtonType.OK.getText() : "Создать");
    }
}
