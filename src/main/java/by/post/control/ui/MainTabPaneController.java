package by.post.control.ui;

import by.post.control.Context;
import by.post.control.db.TableType;
import by.post.ui.ConfirmationDialog;
import by.post.ui.MainUiForm;
import javafx.application.Platform;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.*;

import java.io.IOException;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

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
     * Clear all tabs
     */
    public void clearTabs() {

    }

    /**
     * @param tableName
     * @param tableType
     */
    public void selectTable(String tableName, TableType tableType) throws IOException {

        //Searching for whether the tab for this table is already been opened.
        Tab tab = tabPane.getTabs().isEmpty() ? null : tabPane.getTabs().stream()
                .filter(t -> t.getText().equals(tableName))
                .findFirst().orElse(null);

        if (tab != null) {
            tabPane.getSelectionModel().select(tab);
            return;
        }

        tab = getTab(tableName, tableType);
        tabPane.getTabs().add(tab);
        tabPane.getSelectionModel().select(tab);
    }

    /**
     * @param tableName
     * @param tableType
     * @return new tab
     */
    private Tab getTab(String tableName, TableType tableType) throws IOException {

        FXMLLoader loader = new FXMLLoader(MainUiForm.class.getResource("TableTab.fxml"));
        loader.setResources(ResourceBundle.getBundle("bundles.Lang", Context.getLocale()));
        Node node = loader.load();
        TableTabController controller = loader.getController();
        controller.selectTable(tableName, tableType);

        Tab tab = new Tab(tableName, node);

        tab.setOnCloseRequest(event -> {
            if (controller.hasNotSavedData()) {
                Optional<ButtonType> result = new ConfirmationDialog("You have tabs with unsaved data. Close everyone?").showAndWait();
                if (result.get() != ButtonType.OK) {
                    event.consume();
                }
            }
        });

        tab.setOnClosed(event -> {
            if (tabPane.getTabs().isEmpty()) {
                Platform.runLater(() -> mainController.showTabPane(false));
            }
        });

        MenuItem item = new MenuItem("Close all");
        item.setOnAction(event ->
                Platform.runLater(() -> {
                    tabPane.getTabs().removeAll(tabPane.getTabs().stream()
                            .filter(t -> {
                                Event closeEvent = new Event(Tab.TAB_CLOSE_REQUEST_EVENT);
                                t.getOnCloseRequest().handle(closeEvent);
                                return !closeEvent.isConsumed();
                            }).collect(Collectors.toList()));

                    if (tabPane.getTabs().isEmpty()) {
                        Platform.runLater(() -> mainController.showTabPane(false));
                    }
                }));

        tab.setContextMenu(new ContextMenu(item));

        return tab;
    }

}
