package by.post.ui;

import by.post.control.Context;
import by.post.control.ui.dialogs.SearchToolDialogController;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Dialog;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ResourceBundle;

/**
 * @author Dmitriy V.Yefremov
 */
public class SearchToolDialog extends Dialog {

    private SearchToolDialogController controller;

    public SearchToolDialog() throws IOException {
        init();
    }

    private void init() throws IOException {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("dialogs/SearchToolDialog.fxml"));
        loader.setResources(ResourceBundle.getBundle("bundles.Lang", Context.getLocale()));
        setDialogPane(loader.load());
        controller = loader.getController();

        Stage stage = (Stage) getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image(Resources.LOGO_PATH));

        initModality(Modality.NONE);
        setOnCloseRequest(event -> controller.onCloseRequest());
        //Sets not resizable after click on details
        getDialogPane().expandedProperty().addListener((observable, oldValue, newValue) ->
                Platform.runLater(() -> this.setResizable(false)));
    }
}
