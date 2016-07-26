package by.post.control.ui;

import by.post.control.db.DbControl;
import by.post.control.db.DbController;
import by.post.control.db.Queries;
import by.post.data.Column;
import by.post.ui.ChoiceColumnTypeDialog;
import by.post.ui.ConfirmationDialog;
import by.post.ui.InputDialog;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * @author Dmitriy V.Yefremov
 */
public class TableEditor {

    private DbControl dbControl = null;
    private TableView mainTable;

    private static TableEditor instance = new TableEditor();

    private static final Logger logger = LogManager.getLogger(TableEditor.class);

    private TableEditor() {
    }

    public static TableEditor getInstance() {
        return instance;
    }

    public void setTable(TableView mainTable) {
        this.mainTable = mainTable;
    }

    /**
     * Add new row to the table
     */
    public void addRow() throws IOException {

        int size = mainTable.getColumns().size();

        if (size > 0) {
            int selectedIndex = mainTable.getSelectionModel().getSelectedIndex();
            mainTable.getItems().add(++selectedIndex, FXCollections.observableArrayList(Collections.nCopies(size, "New value.")));
            mainTable.getSelectionModel().select(selectedIndex, null);
        } else {
            Optional<String> result = new InputDialog("\tPlease, specify\n the number of columns!", "1", true).showAndWait();

            if (result.isPresent()) {
                Integer num = Integer.valueOf(result.get());
                List<Column> columns = new ArrayList<>(num);

                for (int i = 0; i < num; i++) {
                    columns.add(new Column("NEW", 0));
                }

                mainTable.getColumns().addAll(new TableDataResolver().getColumns(columns));
            }
        }
        mainTable.refresh();
    }

    /**
     * Remove  selected row from the table
     */
    public void removeRow() {
        mainTable.getItems().remove(mainTable.getSelectionModel().getSelectedItem());
    }

    /**
     * Save changes after table editing
     *
     * @param name
     */
    public void save(String name) {

        Optional<ButtonType> result = new ConfirmationDialog("Save changes for table: " + name).showAndWait();

        if (result.get() == ButtonType.OK) {
            logger.log(Level.INFO, "Save changes for  table: " + name);
        }
    }

    /**
     * Add table in the tree
     *
     * @param tableTree
     */
    public void addTable(TreeView tableTree) {

        Optional<String> result = new InputDialog("Please, write table name!", "New table", false).showAndWait();

        if (result.isPresent()) {
            String name = result.get();

            dbControl = DbController.getInstance();
            dbControl.update(Queries.createTable(name));

            tableTree.getRoot().getChildren().add(new TreeItem<>(name));
            tableTree.refresh();

            logger.log(Level.INFO, "Added new  table: " + name);
        }
    }

    /**
     * Delete table from the tree
     *
     * @param tableTree
     */
    public void deleteTable(TreeView tableTree) {

        Optional<ButtonType> result = new ConfirmationDialog().showAndWait();

        if (result.get() == ButtonType.OK) {
            TreeItem item = (TreeItem) tableTree.getSelectionModel().getSelectedItem();
            String name = item.getValue().toString();

            dbControl = DbController.getInstance();
            dbControl.update(Queries.deleteTable(name));

            tableTree.getRoot().getChildren().remove(item);
            tableTree.refresh();

            logger.log(Level.INFO, "Deleted table: " + name);
        }
    }

    /**
     * Change column name in the table
     *
     * @param id
     */
    public void changeColumnName(String id) {

        TableColumn column = (TableColumn) mainTable.getColumns().get(Integer.valueOf(id));
        Optional<String> result = new InputDialog("Set new name of column", "New", false).showAndWait();

        if (result.isPresent()) {
            column.setText(result.get());
        }
    }

    /**
     * Change column type in the table
     *
     * @param id
     */
    public void changeColumnType(String id) {

        Optional<String> result = new ChoiceColumnTypeDialog().showAndWait();

        if (result.isPresent()) {
            System.out.println(result.get());
        }
    }

    /**
     * Delete column from the table
     *
     * @param id
     */
    public void deleteColumn(String id) {
        Optional<ButtonType> result = new ConfirmationDialog().showAndWait();

        if (result.get() == ButtonType.OK) {
            logger.info("Delete column with id = " + id);
        }
    }

    /**
     * Add new column in the table
     */
    public void addColumn() {

        Optional<String> result = new InputDialog("Please, write column name!", "NEW", false).showAndWait();

        if (result.isPresent()) {
            int index = 0;
            ObservableList<ObservableList> items = mainTable.getItems();
            if (items != null && !items.isEmpty()) {
                ObservableList row = items.get(0);
                index = row.size();
                // Fill in the data.
                items.parallelStream().forEach(item -> item.add("New value."));
                mainTable.setItems(items);
            }
            // Create column
            TableColumn column = new TableDataResolver().getColumn(result.get(), index, false);
            mainTable.getColumns().add(column);

            logger.info("Add column in table.");
        }
    }

}
