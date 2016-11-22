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

    private static SimpleProgressIndicator instance = new SimpleProgressIndicator();

    public static SimpleProgressIndicator getInstance() {
        return instance;
    }

    private SimpleProgressIndicator() {

        FXMLLoader loader = new FXMLLoader(SimpleProgressIndicator.class.getResource("SimpleProgressIndicator.fxml"));

        try {
            parent = loader.load();
            stage = this;
            stage.setScene(new Scene(parent));
            stage.setResizable(false);
            stage.initStyle(StageStyle.UNDECORATED);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.centerOnScreen();

            parent.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    stage.close();
                }
            });

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
