package by.post.control.ui.dialogs;

import javafx.fxml.FXML;

import java.util.List;

/**
 * Used with ConditionListPane as helper
 * for DataSelectionDialogPane
 *
 * @author Dmitriy V.Yefremov
 */
public class ConditionListPaneController {

    List<String> columnNames;

    public ConditionListPaneController() {

    }

    public void setColumnNames(List<String> columnNames) {

        this.columnNames = columnNames;
    }

    @FXML
    private void initialize() {


    }
}
