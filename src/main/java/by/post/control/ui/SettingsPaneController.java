package by.post.control.ui;

import by.post.ui.MainUiForm;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionModel;
import javafx.scene.layout.HBox;

import java.io.IOException;

/**
 * @author Dmitriy V.Yefremov
 */
public class SettingsPaneController {

    @FXML
    private HBox parent;
    @FXML
    private ListView<String> settingsList;

    public SettingsPaneController() {

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

        if (!settingsList.getItems().isEmpty()) {
            setSettings(settingsList.getItems().get(0));
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

        if (parent.getChildren().size() > 1) {
            parent.getChildren().remove(1);
        }

        if (name.equals("Database")) {
            try {
                Node node = (Node) FXMLLoader.load(MainUiForm.class.getResource("DbSettings.fxml"));
                parent.getChildren().add(node);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
