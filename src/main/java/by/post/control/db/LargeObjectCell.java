package by.post.control.db;

import by.post.data.Column;
import by.post.data.Table;
import javafx.collections.FXCollections;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.ChoiceBoxTableCell;
import javafx.util.Callback;

/**
 *
 */
class LargeObjectCell extends ChoiceBoxTableCell {

    private ChoiceBox choiceBox;
    private Table table;

    public LargeObjectCell(Table table) {
        this.table = table;
    }

    /**
     *
     */
    @Override
    public void startEdit() {

        if (!isEditable() || !getTableView().isEditable() || !getTableColumn().isEditable()) {
            return;
        }

        if (choiceBox == null) {
            choiceBox = new ChoiceBox(FXCollections.observableArrayList("Download", "Upload"));
        }

        choiceBox.setOnAction(event -> {

            int index = choiceBox.getSelectionModel().getSelectedIndex();
            int rowIndex = this.getIndex();

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

        super.startEdit();
        setText(null);
        setGraphic(choiceBox);
    }

    /**
     *
     */
    @Override
    public void cancelEdit() {
        super.cancelEdit();
        setText(String.valueOf(getItem()));
        setGraphic(null);
        choiceBox = null;
    }

    /**
     * @return
     */
    public static Callback<TableColumn, TableCell> forTableColumn(final Table table) {
        return list -> new LargeObjectCell(table);
    }

}
