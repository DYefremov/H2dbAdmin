package by.post.ui;

import by.post.control.db.DbControl;
import by.post.control.db.DbController;
import by.post.control.ui.MainUiController;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.Optional;

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
        loader.setLocation(getClass().getResource("MainUiForm.fxml"));

        mainPane = loader.load();
        controller = loader.getController();
        controller.setMainUiForm(this);
        mainScene = new Scene(mainPane, 700, 500);
        mainStage.setScene(mainScene);
        mainStage.setMinHeight(500);
        mainStage.setMinWidth(700);
        //Override closing program
        mainStage.setOnCloseRequest(event -> {
            Optional<ButtonType> result = new ConfirmationDialog().showAndWait();

            if (result.get() == ButtonType.OK) {
                closeConnection();
                Platform.exit();
                System.exit(0);
            }
            event.consume();
        });

        mainStage.show();
    }

    public Stage getMainStage() {
        return mainStage;
    }

    private void closeConnection() {
        DbControl dbControl = DbController.getInstance();
        dbControl.closeConnection();
    }

}
