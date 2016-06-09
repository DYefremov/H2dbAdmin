package by.post.ui;

import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

/**
 * Implementation of small progress indicator
 *
 * @author Dmitriy V.Yefremov
 */
public class SimpleProgressIndicator extends Stage {
    private ProgressIndicator parent;
    private Stage stage;

    public SimpleProgressIndicator() {
        FXMLLoader loader = new FXMLLoader(SimpleProgressIndicator.class.getResource("SimpleProgressIndicator.fxml"));
        try {
            stage = this;
            parent = loader.load();

            parent.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    stage.close();
                }
            });

            stage.setScene(new Scene(parent));
            stage.setResizable(false);
            stage.initStyle(StageStyle.UNDECORATED);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.centerOnScreen();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
