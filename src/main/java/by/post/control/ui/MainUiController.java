package by.post.control.ui;

import by.post.control.Context;
import by.post.control.PropertiesController;
import by.post.control.Settings;
import by.post.control.db.DatabaseManager;
import by.post.control.ui.tools.UsersToolController;
import by.post.data.Table;
import by.post.data.View;
import by.post.data.type.Dbms;
import by.post.ui.*;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.ButtonType;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Map;
import java.util.Optional;

/**
 * Controller class for main ui form
 *
 * @author Dmitriy V.Yefremov
 */
public class MainUiController {

    @FXML
    private TextArea console;
    @FXML
    private SplitPane mainSplitPane;
    @FXML
    private SplitPane explorerSplitPane;
    @FXML
    private MainTableTreeController mainTableTreeController;
    @FXML
    private MainTabController mainTabPaneController;

    private TabPane tabPane;
    private MainUiForm mainUiForm;
    private DatabaseManager databaseManager;

    private static final Logger logger = LogManager.getLogger(MainUiController.class);

    public MainUiController() {
    }

    public void setMainUiForm(MainUiForm mainUiForm) {
        this.mainUiForm = mainUiForm;
    }

    /**
     * Actions for menu and tool bar items
     */
    @FXML
    public void onNewDbAdd() throws IOException {

        Optional<Map<String, String>> result = new DatabaseDialog().showAndWait();

        if (result.isPresent()) {
            new Thread(() -> {
                databaseManager.addDatabase(result.get());
                Platform.runLater(() -> {
                    clearTabs();
                    mainTableTreeController.init();
                });
            }).start();
        }
    }

    @FXML
    public void onAddNewTable() throws IOException {
        addNewTable();
    }

    @FXML
    public void onAddNewView() throws IOException {
        addNewView();
    }

    @FXML
    public void onDbDelete() {
        databaseDelete(false);
    }

    @FXML
    public void onDbDrop() {
        databaseDelete(true);
    }

    @FXML
    public void onOpenDb() throws IOException {
        openNewDatabase();
    }

    @FXML
    public void onExit() {
        closeProgram();
    }

    @FXML
    public void onAbout() throws IOException {
        new AboutDialog(mainUiForm.getHostServices()).showAndWait();
    }

    @FXML
    public void onExplorer() {
        setCenter(explorerSplitPane);
    }

    @FXML
    public void onTrigger() throws IOException {
        setCenter(FXMLLoader.load(MainUiForm.class.getResource("tools/TriggersToolPane.fxml")));
    }

    @FXML
    public void onSqlConsole() throws IOException {
        setCenter(FXMLLoader.load(MainUiForm.class.getResource("tools/SqlConsole.fxml")));
    }

    @FXML
    public void onUsersTool() throws IOException {
        showUsersTool(false);
    }

    @FXML
    public void onAddUser() throws IOException {
       showUsersTool(true);
    }

    @FXML
    public void onSearchTool() throws IOException {
        onExplorer();
        showSearchTool();
    }

    @FXML
    public void onRecoveryTool() throws IOException {
        new RecoveryToolDialog().showAndWait();
    }

    @FXML
    public void onSettings() throws IOException {
        new SettingsDialog().showAndWait();
    }

    /**
     * Show/Hide tab pane
     * @param show
     */
    public void showTabPane(boolean show) {

        Platform.runLater(() -> {
            if (show && !explorerSplitPane.getItems().contains(tabPane)) {
                explorerSplitPane.getItems().add(tabPane);
                explorerSplitPane.setDividerPosition(0, 0.3);
            } else if (!show) {
                explorerSplitPane.getItems().remove(tabPane);
            }
        });
    }

    /**
     * Clear main tab pane
     */
    public void clearTabs() {
        mainTabPaneController.closeAllTabs();
    }

    /**
     * Closing tab by table name
     *
     * @param tableName
     */
    public void deleteTab(String tableName) {
        mainTabPaneController.closeTab(tableName);
    }

    @FXML
    private void initialize() {

        mainTableTreeController.setMainController(this);
        mainTabPaneController.setMainController(this);
        tabPane = mainTabPaneController.getTabPane();
        showTabPane(false);
        // Set log messages output to the text area
        LogArea.setArea(console);
        logger.info("Starting application...");
        initDbmsType();
        init();
    }

    /**
     * Init on startup
     */
    private void init() {
        databaseManager = DatabaseManager.getInstance();
    }

    /**
     * Init DBMS type
     */
    private void initDbmsType() {

        String driver = PropertiesController.getProperties().getProperty(Settings.DRIVER);

        if (driver.equals(Settings.DEFAULT_DRIVER)) {
            Context.setCurrentDbms(Dbms.H2);
        }
    }

    /**
     * Open new database
     */
    private void openNewDatabase() throws IOException {

        Optional<Map<String, String>> result = new OpenDbDialog().showAndWait();

        if (result.isPresent()) {
            PropertiesController.setProperties(result.get());
            clearTabs();
            mainTableTreeController.init();
        }
    }

    /**
     * Add new table
     */
    private void addNewTable() throws IOException {

        Optional<Table> result = new TableCreationDialog().showAndWait();

        if (result.isPresent()) {
            mainTableTreeController.addNewTable(result.get());
        }
    }

    /**
     * Add new view
     */
    private void addNewView() throws IOException {

        Optional<View> result = new ViewCreationDialog().showAndWait();

        if (result.isPresent()) {
            mainTableTreeController.addNewView(result.get());
        }
    }

    /**
     * Set center node in main split pane
     *
     * @param node
     */
    private void setCenter(Node node) {
        mainSplitPane.getItems().set(0, node);
    }

    /**
     * Close and exit the program.
     */
    private void closeProgram() {
        Stage mainStage = mainUiForm.getMainStage();
        mainStage.fireEvent(new WindowEvent(mainStage, WindowEvent.WINDOW_CLOSE_REQUEST));
    }

    /**
     * Show and work with search tool
     */
    private void showSearchTool() throws IOException {
        new SearchToolDialog().show();
    }

    /**
     * Deleting database
     * If not dropOnly the database files will be removed!
     */
    private void databaseDelete(boolean dropOnly) {

        Optional<ButtonType> result = new ConfirmationDialog().showAndWait();

        if (result.get() == ButtonType.OK) {
            new Thread(() -> {
                try {
                    databaseManager.deleteDatabase(dropOnly);
                    Platform.runLater(() -> {
                        clearTabs();
                        mainTableTreeController.initData();
                    });
                } catch (SQLException e) {
                    logger.error("MainUiController error [databaseDelete]: " + e);
                }
            }).start();
        }
    }

    /**
     * Shows tool for working with users
     *
     * @param withAddDialog
     * @throws IOException
     */
    private void showUsersTool(boolean withAddDialog) throws IOException {

        FXMLLoader loader = new FXMLLoader(MainUiForm.class.getResource("tools/UsersToolPane.fxml"));
        setCenter(loader.load());
        //Shows dialog for user add if necessary
        if (withAddDialog) {
            UsersToolController controller = loader.getController();
            controller.onUserAdd();
        }
    }

}
