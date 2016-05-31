package by.post.ui;

import by.post.control.ui.AboutDialogController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * Shows info about application
 *
 * @author Dmitriy V.Yefremov
 */
public class AboutDialog extends Application {
  private AboutDialogController controller;
  private Parent parent;

  @Override
  public void start(Stage stage) throws Exception{
    FXMLLoader loader = new FXMLLoader();
    loader.setLocation(AboutDialog.class.getResource("AboutDialog.fxml"));

    parent = loader.load();
    controller = loader.getController();
    controller.setAboutDialog(this);

    stage.setScene(new Scene(parent));
    stage.initModality(Modality.APPLICATION_MODAL);
    stage.showAndWait();
    }
}
