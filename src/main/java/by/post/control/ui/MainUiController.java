package by.post.control.ui;

import by.post.control.db.DbControl;
import by.post.control.db.DbController;
import by.post.data.Table;
import by.post.ui.AboutDialog;
import by.post.ui.MainUiForm;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

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
  @FXML
  private TreeView tableTree;

  private MainUiForm mainUiForm;

  private static final Logger logger = LogManager.getLogger(MainUiController.class);

  public MainUiController() {

  }

  public void setMainUiForm(MainUiForm mainUiForm) {
    this.mainUiForm = mainUiForm;
  }

  /**
   * add action at the start
   */
  @FXML
  private void initialize() {
    System.out.println("Starting application...");

    DbControl dbControl = new DbController();
    dbControl.connect("~/test", "sa", "");

    List<TreeItem> tables = new ArrayList<>();

    dbControl.getTablesList().stream().forEach(t -> {
      tables.add(new TreeItem(t));
    });

    //Корневой элемент
    TreeItem root = new TreeItem("test");

    ObservableList<TreeItem> list = FXCollections.observableList(tables);
    //Сортируем по алфавиту
//    FXCollections.sort(list);

    root.getChildren().addAll(list);
    tableTree.setRoot(root);

    tableTree.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {
      @Override
      public void changed(ObservableValue observable, Object oldValue, Object newValue) {
        TreeItem<String> item = (TreeItem<String>) newValue;
        selectTable(dbControl.getTable(item.getValue()));
      }
    });
  }
  /**
   * Action for "close" menu item
   */
  @FXML
  public void onItemClose(ActionEvent event) {
    try {
      mainUiForm.getMainStage().close();
    } catch (Exception e) {
      logger.error("MainUiController error: " + e);
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
      logger.error("MainUiController error: " + e);
    }
  }


  /**
   * Select and display the selected table
   * @param table
   */
  private void selectTable(Table table) {
    System.out.println(table.getName());
   table.getRows().stream().forEach(r -> System.out.println(r.toString()));
  }

}
