package by.post.control.ui;

import by.post.control.Context;
import by.post.data.View;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

/**
 * @author Dmitriy V.Yefremov
 */
public class ViewCreationDialogController {

    @FXML
    private TextField viewName;
    @FXML
    private ListView tablesListView;
    @FXML
    private TableView tableView;

    private View view;

    public ViewCreationDialogController() {

    }

    @FXML
    public void onAddButton() {
        new Alert(Alert.AlertType.INFORMATION, "Not implemented yet!").showAndWait();
    }

    @FXML
    public void onDeleteButton() {
        new Alert(Alert.AlertType.INFORMATION, "Not implemented yet!").showAndWait();
    }

    @FXML
    public void onTableListMouseClicked(MouseEvent event) {

        if (event.getClickCount() == 2) {
            addTable();
        }
    }

    public View getView() {
        return view != null ? view : new View();
    }

    @FXML
    private void initialize() {
        setTablesList();
    }

    /**
     *
     */
    private void setTablesList() {

        ObservableList<TypedTreeItem> list = Context.getTablesTreeItem().getChildren();

        if (list != null && !list.isEmpty()) {
            ObservableList<String> tablesList = FXCollections.observableArrayList();
            list.forEach(item -> tablesList.add(String.valueOf(item.getValue())));
            tablesListView.setItems(tablesList);
        }
    }

    /**
     *
     */
    private void addTable() {
        
    }
}
