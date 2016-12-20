package by.post.control.ui;

import by.post.ui.MainUiForm;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionModel;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

/**
 * @author Dmitriy V.Yefremov
 */
public class SettingsDialogController {

    @FXML
    private Pane content;
    @FXML
    private ListView<String> settingsList;

    private Node dbSettings;
    private Node uiSettings;

    private static final Logger logger = LogManager.getLogger(SettingsDialogController.class);

    public SettingsDialogController() {

    }

    /**
     * Navigation in settings list by mouse
     */
    @FXML
    public void onSettingsMouseClicked() {
        selectSettingsItem();
    }

    /**
     * Navigation in settings list by keyboard
     */
    public void onSettingsKeyReleased() {
        selectSettingsItem();
    }

    @FXML
    private void initialize() {
        try {
            dbSettings = (Node) FXMLLoader.load(MainUiForm.class.getResource("DbSettings.fxml"));
            uiSettings = (Node) FXMLLoader.load(MainUiForm.class.getResource("UiSettings.fxml"));

            if (!settingsList.getItems().isEmpty()) {
                setSettings(settingsList.getItems().get(0));
            }
        } catch (IOException e) {
            logger.error("SettingsDialogController error[initialize]: " + e);
        }
    }

    /**
     * Selecting value from items list
     */
    private void selectSettingsItem() {

        SelectionModel model = settingsList.getSelectionModel();

        if (model.getSelectedItem() == null) {
            model.select(0);
        }

        setSettings(model.getSelectedItem().toString());
    }

    /**
     * @param name
     */
    private void setSettings(String name) {

        if (name.equals(settingsList.getItems().get(0))) {
            setContent(dbSettings);
        } else  if (name.equals(settingsList.getItems().get(1))) {
            setContent(uiSettings);
        }
    }

    /**
     * @param node
     */
    private void setContent(Node node) {

        Platform.runLater(() -> {
            if (!content.getChildren().isEmpty()) {
                content.getChildren().clear();
            }
            content.getChildren().add(node);
        });
    }

}
