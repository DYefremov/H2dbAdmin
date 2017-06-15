package by.post.control.ui;

import by.post.control.db.DbControl;
import by.post.control.db.DbController;
import by.post.control.db.TableType;
import by.post.data.Row;
import by.post.data.Table;
import by.post.ui.DataSelectionDialog;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.io.IOException;
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
    private TextField maxRowsTextField;
    @FXML
    private Button prevButton;
    @FXML
    private Button nextButton;
    @FXML
    private MainTableController mainTableController;

    private int offset;
    private int rowsLimit;
    private int dataSize;
    private Table table;
    private DbControl dbControl;

    public TableTabController() {

    }

    public void onSearch() {
        new DataSelectionDialog().showAndWait();
    }

    @FXML
    public void onNext() {
        updateData(true);
    }

    @FXML
    public void onPrevious() {
        updateData(false);
    }

    @FXML
    public void onMaxRowsChanged() {

        String value = maxRowsTextField.getText();

        if (value == "" || !value.matches("\\d+")) {
            maxRowsTextField.setStyle("-fx-border-color: red; -fx-border-width: 2px; -fx-border-radius: 3px");
            return;
        }

        maxRowsTextField.setStyle(null);
        rowsLimit = Integer.valueOf(value);
    }

    @FXML
    public void onRefresh() {
        new Alert(Alert.AlertType.INFORMATION, "Not implemented yet").showAndWait();
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
        dataSize = table.getData() != null ? table.getData().size() : 0;
        updateNavigationButtons();
    }

    @FXML
    private void initialize() {

        dbControl = DbController.getInstance();
        rowsLimit = Integer.valueOf(maxRowsTextField.getText());
    }

    /**
     * Receiving and updating table data while navigating
     *
     * @param next
     */
    private void updateData(boolean next) {

        if (next) {
            offset += rowsLimit;
        } else {
            offset = offset < 0 ? 0 : offset - rowsLimit;
        }

        Collection<Row> data = (Collection<Row>) dbControl.getTableData(table.getName(), table.getType(), rowsLimit, offset);

        dataSize = data.size();
        updateNavigationButtons();

        if (!data.isEmpty()) {
            mainTableController.setData(data);
        }
    }

    /**
     * Updating disable property for navigation buttons
     */
    private void updateNavigationButtons() {

        prevButton.setDisable(offset == 0);
        nextButton.setDisable(dataSize < rowsLimit);
    }

}
