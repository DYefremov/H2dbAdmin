package by.post.control.ui;

import by.post.control.Context;
import by.post.control.db.DbControl;
import by.post.control.db.DbController;
import by.post.control.db.Queries;
import by.post.data.View;
import by.post.ui.MainUiForm;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
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
    private TableView<ConditionRow> tableView;

    private View view;

    private DbControl dbControl;

    private static final Logger logger = LogManager.getLogger(ViewCreationDialogController.class);

    public ViewCreationDialogController() {

    }

    @FXML
    public void onAddButton() {
        addTable();
//        new Alert(Alert.AlertType.INFORMATION, "Not implemented yet!").showAndWait();
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
        TableColumn column = null;

        try {
            column = getTableColumn(tableName);
            tableView.getColumns().add(column);
        } catch (IOException e) {
            new Alert(Alert.AlertType.ERROR, "Error creating column!").showAndWait();
            logger.error("ViewCreationDialogController error[addTable]: " + e);
        }

        if (column == null) {
            return;
        }
    }

    /**
     * @param tableName
     * @return
     * @throws IOException
     */
    private TableColumn getTableColumn(String tableName) throws IOException {

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(MainUiForm.class.getResource("ConditionColumn.fxml"));
        TableColumn column = null;
        column = (TableColumn) loader.load();
        column.setText(tableName);

        return column;
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

    /**
     *
     */
    class ConditionRow {

        private SimpleStringProperty name;
        private SimpleStringProperty condition;
        private SimpleBooleanProperty selected;

        public ConditionRow() {
            this.name = new SimpleStringProperty();
            this.condition = new SimpleStringProperty();
            this.selected = new SimpleBooleanProperty(false);
        }

        public ConditionRow(String name, String condition, boolean selected) {
            this.name = new SimpleStringProperty(name);
            this.condition = new SimpleStringProperty(condition);
            this.selected = new SimpleBooleanProperty(selected);
        }

        public String getName() {
            return name.get();
        }

        public SimpleStringProperty nameProperty() {
            return name;
        }

        public void setName(String name) {
            this.name.set(name);
        }

        public String getCondition() {
            return condition.get();
        }

        public SimpleStringProperty conditionProperty() {
            return condition;
        }

        public void setCondition(String condition) {
            this.condition.set(condition);
        }

        public boolean isSelected() {
            return selected.get();
        }

        public SimpleBooleanProperty selectedProperty() {
            return selected;
        }

        public void setSelected(boolean selected) {
            this.selected.set(selected);
        }

        @Override
        public String toString() {
            return "ConditionRow{" +
                    "name=" + name +
                    ", condition=" + condition +
                    ", selected=" + selected +
                    '}';
        }
    }

}
