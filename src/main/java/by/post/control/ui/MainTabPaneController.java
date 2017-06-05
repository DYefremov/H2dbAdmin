package by.post.control.ui;

import by.post.control.db.TableType;
import by.post.data.Table;
import by.post.ui.MainUiForm;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
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

    /**
     * @param table
     */
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
        //Searching for whether the tab for this table is already been opened.
        Tab tab = tabPane.getTabs().isEmpty() ? null : tabPane.getTabs().stream()
                        .filter(t -> t.getText().equals(table.getName()))
                        .findFirst().orElse(null);

        if (tab == null) {
            tab = getTab(table.getName(), node);
            tabPane.getTabs().add(tab);
        }

        tabPane.getSelectionModel().select(tab);
    }

    /**
     * @param name
     * @param node
     * @return
     */
    private Tab getTab(String name, Node node) {

        Tab tab = new Tab(name, node);
        tab.setOnClosed(event -> {
            if (tabPane.getTabs().isEmpty()) {
                Platform.runLater(() -> mainController.showTabPane(false));
            }
        });

        MenuItem item = new MenuItem("Close all");
        item.setOnAction(event ->
            Platform.runLater(() -> {
                tabPane.getTabs().clear();
                Platform.runLater(() -> mainController.showTabPane(false));
        }));

        tab.setContextMenu(new ContextMenu(item));

        return tab;
    }

}
