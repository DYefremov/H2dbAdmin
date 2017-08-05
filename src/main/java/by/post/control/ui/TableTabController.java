package by.post.control.ui;

import by.post.control.db.DbControl;
import by.post.control.db.DbController;
import by.post.control.db.TableEditor;
import by.post.control.db.TableType;
import by.post.data.Row;
import by.post.data.Table;
import by.post.ui.ConfirmationDialog;
import by.post.ui.DataSelectionDialog;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.io.IOException;
import java.util.Collection;
import java.util.Optional;

/**
 * @author Dmitriy V.Yefremov
 */
public class TableTabController {

    @FXML
    private Tab tab;
    @FXML
    private Label tableNameLabel;
    @FXML
    private Label typeLabel;
    @FXML
    private Label dataSizeLabel;
    @FXML
    private TextField maxRowsTextField;
    @FXML
    private Button prevButton;
    @FXML
    private Button nextButton;
    @FXML
    private Button dataSelectionButton;
    @FXML
    private MainTableController mainTableController;
    @FXML
    private Node mainTable;

    private int offset;
    private int rowsLimit;
    private int dataSize;
    private Table table;
    private DbControl dbControl;
    private TableEditor tableEditor;
    private SimpleStringProperty sizeProperty;
    private DataLoadService loadService;
    private MainTabController mainTabController;

    public TableTabController() {

    }

    public void setMainTabController(MainTabController mainUiController) {
        this.mainTabController = mainUiController;
    }

    @FXML
    public void onSearch() throws IOException {

        Optional<String> result =  new DataSelectionDialog(table).showAndWait();

        if (result.isPresent()) {
            String query = result.get().replace(";", "") + " LIMIT " + maxRowsTextField.getText();
            new DataSelectionService(query, table.getType()).start();
        }
    }

    @FXML
    public void onNext() {

        offset += rowsLimit;
        updateData();
    }

    @FXML
    public void onPrevious() {

        offset = offset < 0 ? 0 : offset - rowsLimit;
        updateData();
    }

    @FXML
    public void onMaxRowsChanged(KeyEvent event) {

        String value = maxRowsTextField.getText();

        if (value == "" || !value.matches("\\d+")) {
            maxRowsTextField.setStyle("-fx-border-color: red; -fx-border-radius: 3px");
            return;
        }

        maxRowsTextField.setStyle(null);
        rowsLimit = Integer.valueOf(value);

        if (event.getCode().equals(KeyCode.ENTER)) {
            updateData();
        }
    }

    @FXML
    public void onRefresh() {
       updateData();
    }

    @FXML
    private void onCloseRequest(Event event) {

        if (hasNotSavedData()) {
            Optional<ButtonType> result = new ConfirmationDialog("You have tabs with unsaved data. Close everyone?").showAndWait();
            if (result.get() != ButtonType.OK) {
                event.consume();
            }
        }
    }

    @FXML
    private void onClosed() {
        mainTabController.onClosedTab();
    }

    @FXML
    private void onCloseMenuItem() {
       mainTabController.closeTab(tab);
    }

    @FXML
    private void onCloseAllMenuItem() {
        mainTabController.closeAllTabs();
    }

    private boolean hasNotSavedData() {
        return tableEditor.hasNotSavedData();
    }

    /**
     * @param table
     */
    public void selectTable(Table table) {

        this.table = table;
        final TableType tableType = table.getType();

        tableNameLabel.setText(table.getName());
        dataSelectionButton.setDisable(!tableType.equals(TableType.TABLE));
        typeLabel.setText(tableType != null ? tableType.name() : "");
        mainTableController.setTable(table);

        if (table.isForceDataLoad()) {
            updateData();
        }

        updateNavigationButtons();
    }

    @FXML
    private void initialize() {

        dbControl = DbController.getInstance();
        rowsLimit = Integer.valueOf(maxRowsTextField.getText());
        tableEditor = mainTableController.getTableEditor();
        sizeProperty = new SimpleStringProperty(String.valueOf(dataSize));
        dataSizeLabel.textProperty().bind(sizeProperty);
        loadService = new DataLoadService();
    }

    /**
     * Receiving and updating table data while navigating
     */
    private void updateData() {
        loadService.restart();
    }

    /**
     * Updating disable property for navigation buttons
     */
    private void updateNavigationButtons() {

        Platform.runLater(() -> {
            prevButton.setDisable(offset == 0);
            nextButton.setDisable(dataSize < rowsLimit);
        });
    }

    /**
     * Service for updating table data
     */
    private class DataLoadService extends Service<Void> {

        @Override
        protected Task<Void> createTask() {

            Task<Void> task = new Task<Void>() {
                @Override
                protected Void call() throws Exception {
                    mainTable.setDisable(true);
                    Collection<Row> data = (Collection<Row>) dbControl.getTableData(table.getName(), table.getType(), rowsLimit, offset);
                    dataSize = data.size();
                    Platform.runLater(() -> mainTableController.setData(data));

                    return null;
                }
            };

            task.setOnSucceeded(event -> updateUi());

            return task;
        }
    }

    /**
     * Service for data load by custom query
     */
    private class DataSelectionService extends Service<Void> {

        private final String query;
        private final TableType tableType;

        public DataSelectionService(String query, TableType tableType) {
            this.query = query;
            this.tableType = tableType;
            this.setOnSucceeded(event -> updateUi());
        }

        @Override
        protected Task<Void> createTask() {

            Task<Void> task = new Task<Void>() {
                @Override
                protected Void call() throws Exception {
                    mainTable.setDisable(true);
                    Table table = dbControl.getTableFromQuery(query);
                    table.setType(tableType);
                    dataSize = table.getRows() == null ? 0 : table.getRows().size();
                    Platform.runLater(() -> mainTableController.setTable(table));

                    return null;
                }
            };
            return task;
        }
    }

    /**
     * Update ui elements after data changes
     */
    private void updateUi() {

        Platform.runLater(() -> {
            updateNavigationButtons();
            sizeProperty.setValue(String.valueOf(dataSize));
            mainTable.setDisable(false);
        });
    }

}
