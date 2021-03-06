package by.post.control.ui;

import by.post.control.Context;
import by.post.control.events.RootEventTarget;
import by.post.control.events.TableSelectionHandler;
import by.post.data.Table;
import by.post.ui.MainUiForm;
import javafx.application.Platform;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.input.MouseEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

/**
 * @author Dmitriy V.Yefremov
 */
public class MainTabController implements TableSelectionHandler{

    @FXML
    TabPane tabPane;

    private MainUiController mainController;

    private static final Logger logger = LogManager.getLogger(MainUiController.class);

    public MainTabController() {

    }

    public TabPane getTabPane() {
        return tabPane;
    }

    public void setMainController(MainUiController mainController) {
        this.mainController = mainController;
    }

    /**
     * Closing tab by table name
     *
     * @param tableName
     */
    void closeTab(String tableName) {

        Tab tab = getTabByName(tableName);

        if (tab != null) {
            closeTab(tab);
        }
    }

    /**
     *Closing tab
     *
     * @param tab
     */
    void closeTab(Tab tab) {

        Platform.runLater(() -> {
            if (tryCloseTab(tab)) {
                tabPane.getTabs().remove(tab);
                onClosedTab();
            }
        });
    }

    /**
     * Closing all tabs
     */
    void closeAllTabs() {

        Platform.runLater(() -> {
            tabPane.getTabs().removeAll(tabPane.getTabs().stream().filter(t -> tryCloseTab(t)).collect(Collectors.toList()));
            onClosedTab();
        });
    }

    void onClosedTab() {

        if (tabPane.getTabs().isEmpty()) {
            Platform.runLater(() -> mainController.showTabPane(false));
        }
    }

    /**
     * @param table
     */
    public void selectTable(Table table) {

        if (table == null) {
            throw new IllegalArgumentException("MainTabController error[selectTable]: Argument can not be null!");
        }

        //Searching for whether the tab for this table is already been opened.
        Tab tab = tabPane.getTabs().isEmpty() ? null : tabPane.getTabs().stream()
                .filter(t -> t.getText().equals(table.getName()))
                .findFirst().orElse(null);

        if (tab != null && table.isForceDataLoad()) {
            tabPane.getSelectionModel().select(tab);
            return;
        }

        //Only for first tab!!!
        if (tabPane.getTabs().isEmpty()) {
            mainController.showTabPane(true);
        }

        Platform.runLater(() -> {
            Tab newTab = getTab(table);
            tabPane.getTabs().add(newTab);
            tabPane.getSelectionModel().select(newTab);
        });
    }

    /**
     * @param table
     * @return new tab
     */
    private Tab getTab(Table table) {

        FXMLLoader loader = new FXMLLoader(MainUiForm.class.getResource("TableTab.fxml"));
        loader.setResources(ResourceBundle.getBundle("bundles.Lang", Context.getLocale()));
        Tab tab = null;
        try {
            tab = loader.load();
        } catch (IOException e) {
            logger.error("MainTabController error [getTab]: " + e);
            return new Tab("Error", new Label("Tab creation error. See \"More info\""));
        }
        
        TableTabController controller = loader.getController();
        controller.setMainTabController(this);
        controller.selectTable(table);
        tab.setText(table.getName());

        return tab;
    }

    @FXML
    private void initialize() {
        RootEventTarget.addTableSelectionHandler(this);
        //Consuming events during data load
        tabPane.addEventFilter(MouseEvent.MOUSE_PRESSED, event -> {
            if (Context.isLoadData()) {
                event.consume();
            }
        });
    }

    /**
     * @param tab
     * @return true if closed
     */
    private boolean tryCloseTab(Tab tab) {

        Event closeEvent = new Event(Tab.TAB_CLOSE_REQUEST_EVENT);
        tab.getOnCloseRequest().handle(closeEvent);

        return !closeEvent.isConsumed();
    }

    /**
     * @param tableName
     * @return found tab or null if not found
     */
    private Tab getTabByName(String tableName) {
        return tabPane.getTabs().stream().filter(t -> t.getText().equals(tableName)).findAny().orElse(null);
    }
}
