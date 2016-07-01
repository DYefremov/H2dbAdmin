package by.post.ui;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * Shows info about application
 *
 * @author Dmitriy V.Yefremov
 */
public class AboutDialog extends Application {
//    private AboutDialogController controller;
    private Parent parent;

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(AboutDialog.class.getResource("AboutDialog.fxml"));

        parent = loader.load();
        // Close dialog by mouse click
        parent.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                stage.close();
            }
        });

        stage.setScene(new Scene(parent));
        stage.setTitle("H2dbAdmin");
        stage.setResizable(false);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initStyle(StageStyle.UNIFIED);
        stage.getIcons().add(new Image(Resources.ICON_PATH));
        stage.showAndWait();
    }

    public void start() throws Exception{
        Stage stage = new Stage();
        start(stage);
    }
}
