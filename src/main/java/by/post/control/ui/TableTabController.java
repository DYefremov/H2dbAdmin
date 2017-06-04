package by.post.control.ui;

import by.post.data.Column;
import by.post.data.Table;
import javafx.fxml.FXML;
import javafx.scene.control.TableView;

/**
 * @author Dmitriy V.Yefremov
 */
public class TableTabController {

    @FXML
    private TableView<Column> columnsTable;


    public TableTabController() {

    }

    public void setTable(Table table) {
        columnsTable.getItems().addAll(table.getColumns());

    }

    @FXML
    private void initialize() {



    }
}
