package by.post.control.ui;

import by.post.control.db.TableType;
import by.post.data.Table;
import by.post.ui.MainUiForm;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;

import java.io.IOException;

/**
 * @author Dmitriy V.Yefremov
 */
public class MainTabPaneController {

    @FXML
    TabPane tabPane;

    private MainUiController mainController;

    public MainTabPaneController() {

    }

    public TabPane getTabPane() {
        return tabPane;
    }

    public void setMainController(MainUiController mainController) {
        this.mainController = mainController;
    }

    /**
     * @param tableType
     */
    public void setTableType(TableType tableType) {

    }

    /**
     * Clear all tabs
     */
    public void clearTabs() {

    }

    public void selectTable(Table table) {

        FXMLLoader loader = new FXMLLoader(MainUiForm.class.getResource("TableTab.fxml"));
        TableTabController controller;

        Node node = null;
        try {
            node = loader.load();
            controller = loader.getController();
            controller.setTable(table);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Tab tab = new Tab(table.getName(), node);
        tab.setOnClosed(event -> {
            if (tabPane.getTabs().isEmpty()) {
                Platform.runLater(() -> mainController.showTabPane(false));
            }
        });

        Platform.runLater(() -> {
            if (tabPane.getTabs().isEmpty()) {
                tabPane.getTabs().add(tab);
            } else {
                tabPane.getTabs().set(0, tab);
            }
//            tabPane.getSelectionModel().select(tab);
        });
    }
}
