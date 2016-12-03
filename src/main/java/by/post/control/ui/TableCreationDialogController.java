package by.post.control.ui;

import by.post.data.Table;
import javafx.fxml.FXML;
import javafx.scene.control.*;


/**
 * @author Dmitriy V.Yefremov
 */
public class TableCreationDialogController {

    @FXML
    private TableView tableView;
    @FXML
    private TableColumn nameColumn;
    @FXML
    private TableColumn typeColumn;
    @FXML
    private TableColumn lengthColumn;
    @FXML
    private TableColumn keyColumn;
    @FXML
    private TableColumn notNullColumn;
    @FXML
    private TableColumn defaultValueColumn;
    @FXML
    private TextField tableName;
    @FXML
    private DialogPane dialogPane;

    private Table table;

    public TableCreationDialogController() {

    }

    @FXML
    public void onAddButton() {
        new Alert(Alert.AlertType.INFORMATION, "Not implemented yet!").showAndWait();
    }

    @FXML
    public void onDeleteButton() {
        new Alert(Alert.AlertType.INFORMATION, "Not implemented yet!").showAndWait();
    }

    /**
     * @return
     */
    public Table getTable() {
        return table == null ? new Table(tableName.getText()) : table;
    }

    @FXML
    private void initialize() {

    }


}

