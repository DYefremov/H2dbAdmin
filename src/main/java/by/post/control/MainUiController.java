package by.post.control;

import by.post.ui.AboutDialog;
import by.post.ui.MainUiForm;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.stage.Stage;

/**
 * Controller class for main ui form
 *
 * @author Dmitriy V.Yefremov
 */
public class MainUiController {

    @FXML
    private MenuItem itemAbout;
    @FXML
    private MenuItem itemClose;
    @FXML
    private Menu menuSettings;

    private MainUiForm mainUiForm;

    public MainUiController() {

    }

    public void setMainUiForm(MainUiForm mainUiForm) {
        this.mainUiForm = mainUiForm;
    }

    //Можно добавить получение-наполнение данными при старте (чтение настроек)
    @FXML
    private void initialize() {
        System.out.println("Starting application...");
        //TODO добавить сюда инициализацию бд, настроек и т.п.
//        DbControl dbControl = new DbController();
//        dbControl.connect("~/test","sa","");
    }

    /**
     * Action for "close" menu item
     */
    @FXML
    public void onItemClose(ActionEvent event) {
        try {
            mainUiForm.getMainStage().close();
        } catch (Exception e) {
            //TODO добавить logger
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
    }

    /**
     * Action for "About" menu item
     */
    @FXML
    public void onItemAbout(ActionEvent event) {
        try {
            new AboutDialog().start(new Stage());
        } catch (Exception e) {
            System.out.println(e.getMessage());
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
    }

}
