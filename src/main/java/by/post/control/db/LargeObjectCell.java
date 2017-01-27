package by.post.control.db;

import by.post.data.Column;
import by.post.data.Table;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.util.Callback;

/**
 *
 */
class LargeObjectCell extends TableCell {

    private ChoiceBox choiceBox;
    private Table table;

    private static final String TEXT =  "Object";

    public LargeObjectCell(Table table) {
        this.table = table;
        this.setText(TEXT);
    }

    /**
     *
     */
    @Override
    public void startEdit() {

        if (! isEditable() || ! getTableView().isEditable() || ! getTableColumn().isEditable()) {
            return;
        }

        if (choiceBox == null) {
            choiceBox = createChoiceBox(FXCollections.observableArrayList("Download", "Upload"));
        }

        choiceBox.setOnAction(event -> {
            int index = choiceBox.getSelectionModel().getSelectedIndex();
            int rowIndex = this.getIndex();
            System.out.println(rowIndex + " " + this.getTableRow().getIndex());

            Column column = (Column) this.getTableColumn().getUserData();
            LobDataManager dataManager = LobDataManager.getInstance();

            if (index == 0) {
                dataManager.download(rowIndex, column, table);
                cancelEdit();
            } else {
                dataManager.upload(rowIndex, column, table);
                cancelEdit();
            }
        });

        setGraphic(choiceBox);
        super.startEdit();
    }

    /**
     *
     */
    @Override
    public void cancelEdit() {
        super.cancelEdit();
        setGraphic(null);
        choiceBox = null;
    }

    /**
     * @param items
     * @return
     */
    static ChoiceBox createChoiceBox(final ObservableList items) {

        ChoiceBox choiceBox = new ChoiceBox(items);

        return choiceBox;
    }

    /**
     * @return
     */
    public static  Callback<TableColumn, TableCell> forTableColumn(final Table table) {
        return list -> new LargeObjectCell(table);
    }

}
