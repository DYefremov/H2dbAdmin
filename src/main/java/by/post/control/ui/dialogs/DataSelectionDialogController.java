package by.post.control.ui.dialogs;

import by.post.data.Column;
import by.post.data.Table;
import com.sun.javafx.scene.control.skin.ComboBoxListViewSkin;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Dmitriy V.Yefremov
 */
public class DataSelectionDialogController {

    @FXML
    private ChoiceBox choiceBox;
    @FXML
    private ComboBox columnsBox;
    @FXML
    private ChoiceBox fromToBox;
    @FXML
    private ComboBox tablesListBox;
    @FXML
    private ChoiceBox whereBox;
    @FXML
    private Node conditionListPane;
    @FXML
    private DialogPane dialogPane;
    @FXML
    private ListView columnsListView;
    @FXML
    ConditionListPaneController conditionListPaneController;

    private Table table;

    public DataSelectionDialogController() {

    }

    public void setTable(Table table) {

        this.table = table;
        updateBoxes(table);
    }

    @FXML
    public void columnsBoxOnHidden() {

        List<String> filtered = getSelectedColumnNames();

        if (table.getColumns().size() == filtered.size()) {
            columnsBox.setValue("*");
            updateConditionList();
            return;
        }

        String value = "";

        int lastIndex = filtered.size() - 1;

        for (String name : filtered) {
            value += name + (filtered.indexOf(name) == lastIndex ? "": ",");
        }

        columnsBox.setValue(value);
        updateConditionList();
    }

    @FXML
    private void initialize() {

        dialogPane.setContent(null);

        //Disable hiding popup on selection !!!
        columnsBox.setSkin(new ComboBoxListViewSkin<Object>(columnsBox) {
            @Override
            protected boolean isHideOnClickEnabled() {
                return false;
            }
        } );

        whereBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            dialogPane.setContent(String.valueOf(observable.getValue()).equals("WHERE") ? conditionListPane : null);
            updateConditionList();
            dialogPane.getScene().getWindow().sizeToScene();
        });
    }

    /**
     * @param table
     */
    private void updateBoxes(Table table) {

        tablesListBox.getItems().add(table.getName());
        tablesListBox.setValue(table.getName());

        table.getColumns().forEach(c -> {
            CheckBox checkBox = new CheckBox(c.getColumnName());
            checkBox.setSelected(true);
            columnsListView.getItems().add(checkBox);
        });
    }

    private void updateConditionList() {

        List<Column> filtered = getSelectedColumns();
        conditionListPaneController.setColumns(filtered);
        dialogPane.getScene().getWindow().sizeToScene();
    }

    /**
     * @return selected columns
     */
    private List<Column> getSelectedColumns() {

        List<String> selected = getSelectedColumnNames();

        return table.getColumns().stream().filter(c -> selected.contains(c.getColumnName())).collect(Collectors.toList());
    }

    private List<String> getSelectedColumnNames() {

        return (List<String>) columnsListView.getItems().stream()
                .filter(c -> ((CheckBox) c).isSelected()).map(c -> ((CheckBox) c).getText())
                .collect(Collectors.toList());
    }
}
