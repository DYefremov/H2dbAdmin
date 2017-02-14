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
 *
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

        if (item != null) {
            this.setGraphic(imageView);
            this.setStyle("-fx-alignment: CENTER;");
        }
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
                setCellBackground(dataManager.download(rowIndex, column, table));
            } else {
                setCellBackground(dataManager.upload(rowIndex, column, table));
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
        setGraphic(imageView);
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

}
