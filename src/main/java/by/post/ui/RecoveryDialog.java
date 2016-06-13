package by.post.ui;

import by.post.control.ui.RecoveryDialogController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * @author Dmitriy V.Yefremov
 */
public class RecoveryDialog extends Application{

    private RecoveryDialogController controller;
    private Parent parent;
    private Stage stage;

    @Override
    public void start(Stage stage) throws Exception {
        this.stage = stage;
        this.stage.getIcons().add(new Image(Resources.ICON_PATH));
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(AboutDialog.class.getResource("RecoveryDialog.fxml"));

        parent = loader.load();
        controller = loader.getController();
        controller.setRecoveryDialog(this);

        stage.setScene(new Scene(parent));
        stage.setTitle("H2dbAdmin recovery tool");
        stage.setResizable(false);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.showAndWait();
    }

    /**
     * Start (show) window
     * @throws Exception
     */
    public void start() throws Exception{
        Stage stage = new Stage();
        start(stage);
    }

    /**
     * Close window
     */
    public void close() {
        stage.close();
    }

}
