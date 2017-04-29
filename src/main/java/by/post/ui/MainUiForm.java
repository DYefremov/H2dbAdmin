package by.post.ui;

import by.post.control.Context;
import by.post.control.PropertiesController;
import by.post.control.Settings;
import by.post.control.db.DbControl;
import by.post.control.db.DbController;
import by.post.control.ui.MainUiController;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.Locale;
import java.util.Optional;
import java.util.Properties;
import java.util.ResourceBundle;

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
    private Properties properties;

    private static final Logger logger = LogManager.getLogger(MainUiForm.class);

    public static void main(String[] args) {
        // Checking that the application is already running
        if (new AppRunChecker().isRunning()) {
            Platform.exit();
            System.exit(0);
        }

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
            logger.error("MainUiForm error: " + e);
        }
    }

    /**
     * Initializing main ui elements
     */
    private void initApp() throws Exception {

        properties = PropertiesController.getProperties();

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("MainUiForm.fxml"));
        //Set language
        Locale locale = getLocale();
        loader.setResources(ResourceBundle.getBundle("bundles.Lang", locale));
        Context.setLocale(locale);

        mainPane = loader.load();
        controller = loader.getController();
        controller.setMainUiForm(this);
        mainScene = new Scene(mainPane, Resources.MIN_WIDTH, Resources.MIN_HEIGHT);
        mainStage.setScene(mainScene);
        mainStage.setMinHeight(Resources.MIN_HEIGHT);
        mainStage.setMinWidth(Resources.MIN_WIDTH);
        setCloseRequest();
        mainStage.show();
    }

    /**
     * Override closing program
     */
    private void setCloseRequest() {
        mainStage.setOnCloseRequest(event -> {
            Boolean showPrompt = Boolean.valueOf(properties.getProperty(Settings.SHOW_PROMPT_IF_EXIT));

            if (showPrompt) {
                Optional<ButtonType> result = new ConfirmationDialog().showAndWait();
                if (result.get() == ButtonType.OK) {
                    close();
                } else {
                    event.consume();
                }
            }  else {
                close();
            }
        });
    }

    /**
     * Actions for close program
     */
    private void close() {
        closeConnection();
        Platform.exit();
        System.exit(0);
    }

    /**
     * @return main stage
     */
    public Stage getMainStage() {
        return mainStage;
    }

    /**
     * Closing connection to database
     */
    private void closeConnection() {
        DbControl dbControl = DbController.getInstance();
        dbControl.closeConnection();
    }

    private Locale getLocale() {

        boolean defLang = true;

        if (properties != null && properties.getProperty(Settings.LANG) != null) {
           defLang = properties.getProperty(Settings.LANG).equals(Settings.DEFAULT_LANG);
        }

        return defLang ? new Locale(Settings.DEFAULT_LANG) : new Locale(Settings.SECOND_LANG);
    }
}
