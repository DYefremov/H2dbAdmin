package by.post.control.ui.dialogs;

import by.post.control.Context;
import by.post.control.db.DbControl;
import by.post.control.db.DbController;
import by.post.control.db.Queries;
import by.post.control.ui.ConditionTableViewController;
import by.post.control.ui.TypedTreeItem;
import by.post.data.Column;
import by.post.data.ConditionRow;
import by.post.data.Table;
import by.post.data.View;
import by.post.ui.MainUiForm;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Dmitriy V.Yefremov
 */
public class ViewCreationDialogController {

    @FXML
    private TextField viewName;
    @FXML
    private ListView tablesListView;
    @FXML
    private HBox viewsHBox;

    private View view;

    private DbControl dbControl;

    private static final Logger logger = LogManager.getLogger(ViewCreationDialogController.class);

    public ViewCreationDialogController() {

    }

    @FXML
    public void onAdd() {
        addTable();
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

    /**
     * @return view
     */
    public View getView() {

        ObservableList tableViews = viewsHBox.getChildren();
        List<Table> tables = new ArrayList<>();
        tableViews.forEach(v -> tables.add(getTable((TableView) v)));

        return new View(viewName.getText(), tables);
    }

    /**
     * @param view
     * @return table
     */
    private Table getTable(TableView view) {

        String tableName = view.getId();
        Table table = new Table(tableName);
        List<ConditionRow> rows = view.getItems();
        List<Column> columns = new ArrayList<>();

        rows.forEach(row -> {
            if (row.isSelected()) {
                Column column = new Column(tableName, row.getName(), null);
                column.setCondition(row.getCondition());
                columns.add(column);
            }
        });

        table.setColumns(columns);

        return table;
    }

    @FXML
    private void initialize() {
        setTablesList();
        dbControl = DbController.getInstance();
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

        Object item = tablesListView.getSelectionModel().getSelectedItem();

        if (item == null) {
            new Alert(Alert.AlertType.ERROR, "Please, select table from list!").showAndWait();
            return;
        }

        String tableName = String.valueOf(item);

        try {
            TableView<ConditionRow> view = getTableView(tableName);

            if (checkNotExist(tableName)) {
                viewsHBox.getChildren().add(view);
                setData(view, tableName);
            } else {
                new Alert(Alert.AlertType.ERROR, "This table has been added!").showAndWait();
            }
        } catch (IOException e) {
            logger.error("ViewCreationDialogController error[addTable]: " + e);
        }
    }

    /**
     * @param tableName
     * @return true if not exist
     */
    private boolean checkNotExist(String tableName) {
        ObservableList<Node> views = viewsHBox.getChildren();

        return views.stream().noneMatch(v -> v.getId().equals(tableName));
    }

    /**
     * @param view
     */
    private void setData(TableView view, String tableName) {

        List<String> columns = getColumnsNames(tableName);
        ObservableList<ConditionRow> data = FXCollections.observableArrayList();
        columns.forEach(c -> data.add(new ConditionRow(c, "", false)));
        view.setItems(data);
    }

    /**
     * @param tableName
     * @return
     * @throws IOException
     */
    private TableView<ConditionRow> getTableView(String tableName) throws IOException {

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(MainUiForm.class.getResource("ConditionTableView.fxml"));
        TableView<ConditionRow> tableView =  loader.load();
        tableView.setId(tableName);

        ConditionTableViewController controller = loader.getController();
        controller.setMainColumnText(tableName);
        controller.setParent(viewsHBox);

        return tableView;
    }

    /**
     * @param tableName
     * @return
     */
    private List<String> getColumnsNames(String tableName) {

        List<String> colNames = new ArrayList<String>();

        try (Statement namesStatement = dbControl.execute(Queries.getTableColumnNames(tableName));
             ResultSet colNamesRs = namesStatement.getResultSet()) {
            while (colNamesRs.next()) {
                colNames.add(colNamesRs.getNString(1));
            }
        } catch (SQLException e) {
            logger.error("ViewCreationDialogController error[getColumnsNames]: " + e);
        }

        return colNames;
    }
}
