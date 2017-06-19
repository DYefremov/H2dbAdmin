package by.post.control.db;

import by.post.data.Column;
import by.post.data.Table;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import javafx.util.Callback;

/**
 * Custom implementation for working with LOB from table cell
 *
 *@author Dmitriy V.Yefremov
 */
class LargeObjectCell extends TableCell {

    private ChoiceBox choiceBox;
    private Table table;
    private ImageView imageView;

    public LargeObjectCell(Table table) {

        this.table = table;
        imageView = new ImageView(new Image("img/file.png"));
        imageView.setFitHeight(16);
        imageView.setFitWidth(16);
    }

    /**
     * @param item
     * @param empty
     */
    @Override
    public void updateItem(Object item, boolean empty) {

        super.updateItem(item, empty);
        this.setGraphic(item != null ? imageView : null);
        this.setStyle(item != null ? "-fx-alignment: CENTER;" : null);
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
            choiceBox = new ChoiceBox(FXCollections.observableArrayList("Download", "Upload", "Delete"));
        }

        choiceBox.setOnAction(event -> {

            int index = choiceBox.getSelectionModel().getSelectedIndex();
            int rowIndex = this.getIndex();

            Column column = (Column) this.getTableColumn().getUserData();
            LobDataManager dataManager = LobDataManager.getInstance();

            if (index == 0) {
                boolean downloaded = dataManager.download(rowIndex, column, table);
                setCellBackground(downloaded);
                updateItem(downloaded);
            } else if (index == 1) {
                boolean uploaded = dataManager.upload(rowIndex, column, table);
                setCellBackground(uploaded);
                updateItem(uploaded);
            } else {
                setCellBackground(dataManager.delete(rowIndex, column, table));
                setItem(null);
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
        this.setBackground(Background.EMPTY);
        setGraphic(getItem() != null ? imageView : null);
        choiceBox = null;
    }

    /**
     * @return
     */
    public static Callback<TableColumn, TableCell> forTableColumn(final Table table) {
        return list -> new LargeObjectCell(table);
    }

    /**
     * Sets background for cell
     */
    private void setCellBackground(boolean isError) {
        this.setBackground(new Background(new BackgroundFill(isError ? Color.LIME : Color.RED,
                CornerRadii.EMPTY, Insets.EMPTY)));
    }


    /**
     * Update item if needed
     *
     * @param update
     */
    private void updateItem(boolean update) {

        if (update) {
            updateItem(getItem() != null ? getItem() : "", false);
        }
    }

}
