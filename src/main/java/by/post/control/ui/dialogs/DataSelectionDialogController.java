package by.post.control.ui.dialogs;

import by.post.data.Table;
import com.sun.javafx.scene.control.skin.ComboBoxListViewSkin;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;

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
    private BorderPane conditionPane;
    @FXML
    private DialogPane dialogPane;
    @FXML
    private ListView columnsListView;

    private Table table;

    public DataSelectionDialogController() {

    }

    public void setTable(Table table) {

        this.table = table;
        updateBoxes(table);
    }

    @FXML
    public void columnsBoxOnHidden() {

        List<CheckBox> filtered = (List<CheckBox>) columnsListView.getItems().stream()
                .filter(c -> ((CheckBox) c).isSelected()).collect(Collectors.toList());

        if (table.getColumns().size() == filtered.size()) {
            columnsBox.setValue("*");
            return;
        }

        String value = "";

        int lastIndex = filtered.size() - 1;

        for (CheckBox checkBox : filtered) {
            value += checkBox.getText() + (filtered.indexOf(checkBox) == lastIndex ? "": ",");
        }

        columnsBox.setValue(value);
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

            dialogPane.setContent(String.valueOf(observable.getValue()).equals("WHERE") ? conditionPane : null);
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
}
