package by.post.control.ui;

import by.post.control.db.DbControl;
import by.post.control.db.DbController;
import by.post.control.db.TableType;
import by.post.data.Row;
import by.post.data.Table;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;

import java.util.Collection;

/**
 * @author Dmitriy V.Yefremov
 */
public class TableTabController {

    @FXML
    private Label tableNameLabel;
    @FXML
    private Label typeLabel;
    @FXML
    private MainTableController mainTableController;

    private int offset = 0;
    private int rowsLimit = 1;
    private Table table;
    private DbControl dbControl;

    public TableTabController() {

    }

    public void onSearch() {
        new Alert(Alert.AlertType.INFORMATION, "Not implemented yet").showAndWait();
    }

    @FXML
    public void onNext() {
        offset++;
        setData();
    }

    @FXML
    public void onPrevious() {
        offset--;
        setData();
    }

    /**
     * @param tableName
     * @param tableType
     */
    public void selectTable(String tableName, TableType tableType) {

        tableNameLabel.setText(tableName);
        typeLabel.setText(tableType != null ? tableType.name() : "");
        table = dbControl.getTable(tableName, tableType);
        mainTableController.setTable(table);
    }

    @FXML
    private void initialize() {
        dbControl = DbController.getInstance();
    }

    private void setData() {

        Collection<Row> data = (Collection<Row>) dbControl.getTableData(table.getName(), table.getType(), rowsLimit, offset);

        if (!data.isEmpty()) {
            mainTableController.setData(data);
        }
    }

}
