package by.post.control.ui;

import by.post.data.Table;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

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

    private Table table;

    public TableTabController() {

    }

    /**
     * @param table
     */
    public void setTable(Table table) {

        this.table = table;
        tableNameLabel.setText(table.getName());
        typeLabel.setText(table.getType() != null ? table.getType().name() : "");
        mainTableController.setTable(table);
    }

    @FXML
    private void initialize() {

    }

}
