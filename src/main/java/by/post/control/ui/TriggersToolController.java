package by.post.control.ui;

import by.post.control.db.DbControl;
import by.post.control.db.DbController;
import by.post.control.db.TableType;
import by.post.data.Cell;
import by.post.data.Table;
import by.post.data.Trigger;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.TextFieldTableCell;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author Dmitriy V.Yefremov
 */
public class TriggersToolController {

    @FXML
    TableView<Trigger> tableView;
    @FXML
    TableColumn<Trigger, Boolean> typeInsertColumn;
    @FXML
    TableColumn<Trigger, Boolean> typeUpdateColumn;
    @FXML
    TableColumn<Trigger, Boolean> typeDeleteColumn;
    @FXML
    TableColumn<Trigger, Boolean> typeSelectColumn;
    @FXML
    TableColumn<Trigger, Boolean> beforeColumn;
    @FXML
    TableColumn<Trigger, Boolean> noWaitColumn;
    @FXML
    TableColumn<Trigger, Integer> queueSizeColumn;

    private DbControl dbControl;

    public TriggersToolController() {

    }

    /**
     * Actions for buttons
     */
    @FXML
    public void onAdd() {

        new Alert(Alert.AlertType.INFORMATION, "Not implemented yet!").showAndWait();
        /*
        Trigger trigger = new Trigger();
        tableView.getItems().add(trigger);
        tableView.getSelectionModel().select(trigger);
        */
    }

    @FXML
    public void onDelete() {

        new Alert(Alert.AlertType.INFORMATION, "Not implemented yet!").showAndWait();

        /*
        Optional<ButtonType> result = new ConfirmationDialog().showAndWait();

        if (result.get() == ButtonType.OK) {
            Trigger trigger = tableView.getSelectionModel().getSelectedItem();
            tableView.getItems().remove(trigger);
            tableView.refresh();
        }
        */
    }

    @FXML
    public void initialize() {

        init();
        initData();
    }

    /**
     * Initialize ui elements
     */
    private void init() {

        typeInsertColumn.setCellFactory(CheckBoxTableCell.forTableColumn(typeInsertColumn));
        typeUpdateColumn.setCellFactory(CheckBoxTableCell.forTableColumn(typeUpdateColumn));
        typeDeleteColumn.setCellFactory(CheckBoxTableCell.forTableColumn(typeDeleteColumn));
        typeSelectColumn.setCellFactory(CheckBoxTableCell.forTableColumn(typeSelectColumn));
        beforeColumn.setCellFactory(CheckBoxTableCell.forTableColumn(beforeColumn));
        noWaitColumn.setCellFactory(CheckBoxTableCell.forTableColumn(noWaitColumn));
        queueSizeColumn.setCellFactory(TextFieldTableCell.forTableColumn(new CheckedIntegerStringConverter()));
    }

    /**
     * Initialize data
     */
    private void initData() {

        dbControl = DbController.getInstance();
        Table data = dbControl.getTable("Triggers", TableType.SYSTEM_TABLE);
        tableView.setItems(FXCollections.observableArrayList(getTriggers(data)));
    }

    /**
     * @param data
     * @return collection of triggers received from the system table
     */
    private Collection<Trigger> getTriggers(Table data) {

        List<Trigger> triggers = new ArrayList<>();

        if (data == null || data.getRows() == null) {
            return triggers;
        }

        data.getRows().forEach(r -> {
            Trigger trigger = new Trigger();
            List<Cell> cells = r.getCells();

            if (cells != null && !cells.isEmpty() && cells.size() == 14) {
                trigger.setName(cells.get(2).getValue());
                String types = cells.get(3).getValue();

                if (types != null && types.length() > 1) {
                    trigger.setTypeInsert(types.contains("INSERT"));
                    trigger.setTypeUpdate(types.contains("UPDATE"));
                    trigger.setTypeDelete(types.contains("DELETE"));
                    trigger.setTypeSelect(types.contains("SELECT"));
                }

                trigger.setTableName(cells.get(6).getValue());
                trigger.setBefore(Boolean.valueOf(cells.get(7).getValue()));
                trigger.setJavaClass(cells.get(8).getValue());
                trigger.setQueueSize(Integer.valueOf(cells.get(9).getValue()));
                trigger.setNoWait(Boolean.valueOf(cells.get(10).getValue()));
                trigger.setRemarks(cells.get(11).getValue());
                trigger.setId(Integer.valueOf(cells.get(13).getValue()));

                triggers.add(trigger);
            }
        });

        return triggers;
    }
}
