package by.post.control.ui;

import by.post.control.Context;
import by.post.control.db.TableType;
import by.post.data.Table;
import by.post.ui.MainUiForm;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;

import java.io.IOException;
import java.util.ResourceBundle;

/**
 * @author Dmitriy V.Yefremov
 */
public class MainTabController {

    @FXML
    TabPane tabPane;

    private MainUiController mainController;

    public MainTabController() {

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
     * @param table
     */
    public void selectTable(Table table) throws IOException {

        if (table == null) {
            throw new IllegalArgumentException("MainTabController error[selectTable]: Argument can not be null!");
        }

        selectTable(table.getName(), table.getType());
        mainController.showTabPane(true);
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
        Tab tab = loader.load();
        TableTabController controller = loader.getController();
        controller.setMainUiController(mainController);
        controller.setTabPane(tabPane);
        controller.selectTable(tableName, tableType);
        tab.setText(tableName);

        return tab;
    }

    @FXML
    private void initialize() {
        Context.setMainTabController(this);
    }

}
