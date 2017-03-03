package by.post.ui;

import by.post.control.Context;
import by.post.control.Settings;
import by.post.control.ui.SearchToolDialogController;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.ResourceBundle;

/**
 * @author Dmitriy V.Yefremov
 */
public class SearchToolDialog extends Dialog {

    private SearchToolDialogController controller;

    private static final Logger logger = LogManager.getLogger(SearchToolDialog.class);

    public SearchToolDialog() {
        init();
    }

    private void init() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(SearchToolDialog.class.getResource("SearchToolDialog.fxml"));
            loader.setResources(ResourceBundle.getBundle("bundles.Lang", Context.getLocale()));
            this.setDialogPane(loader.load());
            controller = loader.getController();

            Stage stage = (Stage) this.getDialogPane().getScene().getWindow();
            stage.getIcons().add(new Image(Resources.LOGO_PATH));

            this.initModality(Modality.NONE);
            this.setOnCloseRequest(event -> controller.onCloseRequest());

            final Button cancelButton = (Button) this.getDialogPane().lookupButton(ButtonType.CANCEL);
            cancelButton.addEventFilter(ActionEvent.ACTION, event -> {
                controller.onCloseRequest();
                event.consume();
            });
            //Setting translation for Cancel button
            boolean defLang = Context.getLocale().getLanguage().equals(Settings.DEFAULT_LANG);
            cancelButton.setText(defLang ? ButtonType.CANCEL.getText() : "Отмена");
            //Sets not resizable after click on details
            this.getDialogPane().expandedProperty().addListener((observable, oldValue, newValue) -> this.setResizable(false));
        } catch (IOException e) {
            logger.error("RecoveryToolDialog error[init]: " + e);
        }
    }
}