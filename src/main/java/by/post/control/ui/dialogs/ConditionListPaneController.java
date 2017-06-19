package by.post.control.ui.dialogs;

import by.post.data.Column;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

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

    List<Column> columns;

    public ConditionListPaneController() {

    }

    public void setColumns(List<Column> columns) {

        this.columns = columns;
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
        tableView.setMinHeight(columnsSize > 0 ? ++columnsSize * cellSize : cellSize * 2);
    }
}
