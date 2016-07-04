package by.post.ui;

import by.post.control.ui.RecoveryDialogController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * @author Dmitriy V.Yefremov
 */
public class RecoveryDialog{

    private RecoveryDialogController controller;
    private Parent parent;
    private Stage stage;

    public RecoveryDialog() throws IOException {
        init();
    }

    public void show() {
        stage.show();
    }

    public void showAndWait(){
        stage.showAndWait();
    }

    public void close() {
        stage.close();
    }

    private void init() throws IOException {

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(AboutDialog.class.getResource("RecoveryDialog.fxml"));

        parent = loader.load();
        controller = loader.getController();
        controller.setRecoveryDialog(this);

        stage = new Stage();
        stage.setScene(new Scene(parent));
        stage.setTitle("H2dbAdmin recovery tool");
        stage.setResizable(false);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.getIcons().add(new Image(Resources.LOGO_PATH));
    }
}
