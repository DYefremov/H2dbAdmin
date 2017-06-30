package by.post.control.ui.tools;

import by.post.control.db.DbControl;
import by.post.control.db.DbController;
import by.post.control.db.Queries;
import by.post.control.db.TableType;
import by.post.data.*;
import by.post.ui.ConfirmationDialog;
import by.post.ui.UsersDialog;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.CheckBoxTableCell;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

/**
 * @author Dmitriy V.Yefremov
 */
public class UsersToolController {

    @FXML
    private TableView<User> tableView;
    @FXML
    private TableColumn<User, Boolean> adminColumn;

    private DbControl dbControl;
    private static final Logger logger = LogManager.getLogger(UsersToolController.class);

    public UsersToolController() {

    }

    @FXML
    public void onUserAdd() throws IOException {

        Optional<User> result = new UsersDialog().showAndWait();

        if (result.isPresent()) {
            try {
                dbControl.update(Queries.createUser(result.get()));
                refreshUsers();
            } catch (SQLException e) {
                logger.error("UsersToolController error [onUserAdd]: " + e);
                new Alert(Alert.AlertType.ERROR, "Failure creating user.\n" + e.getMessage()).showAndWait();
            }
        }

    }

    @FXML
    private void onUserDelete() {

        User user = tableView.getSelectionModel().getSelectedItem();

        if (user == null) {
            new Alert(Alert.AlertType.ERROR, "No user is selected!").showAndWait();
            return;
        }

        Optional<ButtonType> result = new ConfirmationDialog().showAndWait();

        if (result.get() == ButtonType.OK) {
            try {
                dbControl.update(Queries.dropUser(user));
                refreshUsers();
            } catch (SQLException e) {
                logger.error("UsersToolController error [onUserDelete]: " + e);
                new Alert(Alert.AlertType.ERROR, "Failure to delete the user.\n" + e.getMessage()).showAndWait();
            }
        }
    }

    @FXML
    private void initialize() {

        adminColumn.setCellFactory(CheckBoxTableCell.forTableColumn(adminColumn));
        dbControl = DbController.getInstance();
        tableView.getItems().addAll(getUsers());
    }

    /**
     * @return retrieved collection of users
     */
    private Collection<User> getUsers() {

        String tableName = "Users";

        Table table = dbControl.getTable(tableName, TableType.SYSTEM_TABLE);

        List<User> userList = new ArrayList<>();

        if (table.getColumns() == null || table.getColumns().isEmpty()) {
            logger.error("UsersToolController error[getUsers]: Error retrieving data.");
            return userList;
        }

        int nameIndex = -1;
        int adminIndex = -1;
        int idIndex = -1;

        List<Column> columns = table.getColumns();

        for (Column column : columns) {
            String name = column.getColumnName();
            nameIndex = name.equals("NAME") ? columns.indexOf(column) : nameIndex;
            adminIndex = name.equals("ADMIN") ? columns.indexOf(column) : adminIndex;
            idIndex = name.equals("ID") ? columns.indexOf(column) : idIndex;
        }

        if (nameIndex == -1 || adminIndex == -1 || idIndex == -1) {
            logger.error("UsersToolController error[getUsers]: Error retrieving indexes.");
            return userList;
        }

        Collection<Row> data = (Collection<Row>) dbControl.getTableData(tableName, TableType.SYSTEM_TABLE, 0, Integer.MAX_VALUE);

        if (data != null && !data.isEmpty()) {
            for (Row row : data) {
                List<Cell> cells = row.getCells();
                userList.add(new User(cells.get(nameIndex).getValue(), null, cells.get(idIndex).getValue(),
                        Boolean.valueOf(cells.get(adminIndex).getValue())));
            }
        }

        return userList;
    }

    /**
     * Refresh users list
     */
    private void refreshUsers() {

        Platform.runLater(() -> {
            tableView.getItems().clear();
            tableView.getItems().addAll(getUsers());
            tableView.refresh();
        });
    }

}
