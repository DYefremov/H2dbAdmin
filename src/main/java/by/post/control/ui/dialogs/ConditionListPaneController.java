package by.post.control.ui.dialogs;

import by.post.data.Column;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;

import java.util.List;

/**
 * Used with ConditionListPane as helper
 * for DataSelectionDialogPane
 *
 * @author Dmitriy V.Yefremov
 */
public class ConditionListPaneController {

    @FXML
    private TableView tableView;
    @FXML
    private TableColumn<Column, String> namesColumn;
    @FXML
    private TableColumn<Column, String> conditionColumn;
    @FXML
    private TextArea console;

    private int fullColumnsSize;
    List<Column> columns;

    public ConditionListPaneController() {

    }

    public void setColumns(List<Column> columns, int fullColumnsSize) {

        this.columns = columns;
        this.fullColumnsSize = fullColumnsSize;
        updateColumns();
    }

    @FXML
    private void initialize() {

        conditionColumn.setCellFactory(ConditionCell.forTableColumn());
    }

    private void updateColumns() {

        tableView.getItems().clear();
        tableView.setItems(FXCollections.observableArrayList(columns));

        int columnsSize = columns.size();
        double cellSize = tableView.getFixedCellSize();
        updateConsole();
        tableView.setMinHeight(columnsSize > 0 ? ++columnsSize * cellSize : cellSize * 2);
    }

    private void updateConsole() {

        if (columns.isEmpty()) {
            console.clear();
            return;
        }

        StringBuilder sb = new StringBuilder("SELECT ");

        int columnsSize = columns.size();

        if (columnsSize == fullColumnsSize) {
            sb.append("* ");
        } else {
            int lastColumnIndex = columns.size() - 1;

            for (Column column : columns) {
                int index = columns.indexOf(column);
                sb.append(index == lastColumnIndex ? column.getColumnName() : column.getColumnName() + ",");
            }
        }

        sb.append(" FROM " + columns.get(0).getTableName());
        sb.append(getConditionsForQuery());
        sb.append(";");

        console.setText(sb.toString());
    }

    private String getConditionsForQuery() {
        return "";
    }
}
