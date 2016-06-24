package by.post.control.ui;

import by.post.ui.ConfirmationDialog;
import javafx.collections.FXCollections;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableView;

import java.util.Collections;
import java.util.Optional;

/**
 * @author Dmitriy V.Yefremov
 */
public class TableEditor {

    /**
     * Add new row to the table
     *
     * @param table
     */
    public static void addRow(TableView table) {
        int size = table.getColumns().size();
        int selectedIndex = table.getSelectionModel().getSelectedIndex();
        table.getItems().add(++selectedIndex, FXCollections.observableArrayList(Collections.nCopies(size, "New value.")));
        table.getSelectionModel().select(selectedIndex, null);
        table.refresh();
    }

    /**
     * Remove  selected row from the table
     *
     * @param table
     */
    public static void removeRow(TableView table) {
        table.getItems().remove(table.getSelectionModel().getSelectedItem());
    }

    /**
     * Save changes after table editing
     *
     * @param table
     */
    public static void save(TableView table) {

        Optional<ButtonType> result = new ConfirmationDialog().showAndWait();

        if (result.get() == ButtonType.OK){
            System.out.println("ok");
        }
    }

}
