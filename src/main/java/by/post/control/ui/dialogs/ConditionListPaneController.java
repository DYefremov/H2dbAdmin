package by.post.control.ui.dialogs;

import by.post.data.Column;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;

import java.util.Collection;
import java.util.List;

/**
 * Used with ConditionListPane as helper
 * for DataSelectionDialogPane
 *
 * @author Dmitriy V.Yefremov
 */
public class ConditionListPaneController {

    @FXML
    private TableView<Column> tableView;
    @FXML
    private TableColumn<Column, String> namesColumn;
    @FXML
    private TableColumn<Column, String> conditionColumn;
    @FXML
    private TextArea console;

    private int fullColumnsSize;
    private List<Column> columns;

    public ConditionListPaneController() {

    }

    public void setColumns(List<Column> columns, int fullColumnsSize) {

        this.columns = columns;
        this.fullColumnsSize = fullColumnsSize;
        updateColumns();
    }

    @FXML
    public void onEditCommit(TableColumn.CellEditEvent<Column, String> event) {

        updateConsole();
    }

    @FXML
    private void initialize() {

        conditionColumn.setCellFactory(ConditionCell.forTableColumn());
    }

    /**
     * Update columns after set new list
     */
    private void updateColumns() {

        tableView.getItems().clear();
        tableView.setItems(FXCollections.observableArrayList(columns));

        int columnsSize = columns.size();
        double cellSize = tableView.getFixedCellSize();
        updateConsole();
        tableView.setMinHeight(columnsSize > 0 ? ++columnsSize * cellSize : cellSize * 2);
    }

    /**
     * Updating output for console
     */
    private void updateConsole() {

        if (columns.isEmpty()) {
            console.clear();
            return;
        }

        StringBuilder sb = new StringBuilder("SELECT ");

        int columnsSize = columns.size();

        sb.append(columnsSize == fullColumnsSize ? "* " : getColumnNamesList(columns));
        sb.append(" FROM " + columns.get(0).getTableName());
        sb.append(getConditionsForQuery());
        sb.append(";");

        console.setText(sb.toString());
    }

    /**
     * @return String with constructed condition for columns
     */
    private String getConditionsForQuery() {

        tableView.getItems().forEach(c -> System.out.println("Name = " + c.getColumnName() + " Condition = " + c.getCondition()));

        return "";
    }

    /**
     * @return String from column names separated with ','
     */
    private String getColumnNamesList(Collection<Column> collection) {

        StringBuilder sb = new StringBuilder();

        int lastColumnIndex = collection.size() - 1;

        for (Column column : collection) {
            int index = columns.indexOf(column);
            sb.append(index == lastColumnIndex ? column.getColumnName() : column.getColumnName() + ",");
        }

        return sb.toString();
    }

}
