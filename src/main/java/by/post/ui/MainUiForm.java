package by.post.ui;

import by.post.control.ui.MainUiController;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

/**
 * Main class for Ui
 *
 * @author Dmitriy V.Yefremov
 */
public class MainUiForm extends Application {

    private MainUiController controller;
    private Stage mainStage;
    private Scene mainScene;
    private BorderPane mainPane;

    private static final Logger logger = LogManager.getLogger(MainUiForm.class);

    public static void main(String[] args) {
        launch(args);
    }

    /**
     * @param stage
     * @throws IOException
     */
    @Override
    public void start(Stage stage) {

        this.mainStage = stage;
        this.mainStage.setTitle("H2dbAdmin");
        this.mainStage.getIcons().add(new Image(Resources.LOGO_PATH));

        try {
            initApp();
        } catch (Exception e) {
            logger.error(e);
        }
    }

    /**
     * Initialize main ui elements
     */
    private void initApp() throws Exception {

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(MainUiForm.class.getResource("MainUiForm.fxml"));

        mainPane = loader.load();
        controller = loader.getController();
        controller.setMainUiForm(this);
        mainScene = new Scene(mainPane, 700, 500);
        mainStage.setScene(mainScene);
        //Override closing program
        mainStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                Platform.exit();
                System.exit(0);
            }
        });

        mainStage.show();
    }

    public Stage getMainStage() {
        return mainStage;
    }

}
