package by.post.ui;

import by.post.control.ui.dialogs.AboutDialogController;
import by.post.ui.Resources;
import javafx.application.HostServices;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

/**
 * Shows info about application
 *
 * @author Dmitriy V.Yefremov
 */
public class AboutDialog {

    private Parent parent;
    private Stage stage;
    private AboutDialogController controller;
    private HostServices hostServices;

    public AboutDialog(HostServices hostServices) throws IOException {
        this.hostServices = hostServices;
        init();
    }

    public void show() {
        stage.show();
    }

    public void showAndWait(){
        stage.showAndWait();
    }

    private void init() throws IOException {

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("dialogs/AboutDialog.fxml"));
        parent = loader.load();
        // Close dialog by mouse click
        parent.setOnMouseClicked(event -> stage.close());

        controller = loader.getController();
        controller.setHostServices(hostServices);

        stage = new Stage();
        stage.setScene(new Scene(parent));
        stage.setTitle(Resources.TITLE);
        stage.setResizable(false);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initStyle(StageStyle.UNIFIED);
        stage.getIcons().add(new Image(Resources.LOGO_PATH));
    }

}
