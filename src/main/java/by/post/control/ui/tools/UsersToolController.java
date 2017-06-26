package by.post.control.ui.tools;

import by.post.data.User;
import by.post.ui.UsersDialog;
import javafx.fxml.FXML;
import javafx.scene.control.TableView;

/**
 * @author Dmitriy V.Yefremov
 */
public class UsersToolController {

    @FXML
    private TableView<User> tableView;

    public UsersToolController() {

    }

    @FXML
    public void onUserAdd() {
        new UsersDialog().showAndWait();
    }

    @FXML
    public void onUserDelete() {

    }

    @FXML
    private void initialize() {
        tableView.getItems().add(new User("test", "0", true ));
    }
}
