package by.post.control.db;

import by.post.control.Context;
import by.post.data.Column;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import javafx.util.Callback;

import java.util.ResourceBundle;

/**
 * Custom implementation for working with LOB from table cell
 *
 * @author Dmitriy V.Yefremov
 */
class LargeObjectCell extends TableCell {

    private ChoiceBox choiceBox;
    private ImageView imageView;

    public LargeObjectCell() {

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
            choiceBox = getChoiceBox();
        }

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
    public static Callback<TableColumn, TableCell> forTableColumn() {
        return list -> new LargeObjectCell();
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

    /**
     * @return
     */
    private ChoiceBox getChoiceBox() {
//        FXMLLoader loader = new FXMLLoader(MainUiForm.class.getResource("LobCellBox.fxml"));
//        loader.setResources(ResourceBundle.getBundle("bundles.Lang", Context.getLocale()));
//        choiceBox = loader.load();
        ResourceBundle bundle = ResourceBundle.getBundle("bundles.Lang", Context.getLocale());
        ChoiceBox choiceBox = new ChoiceBox(FXCollections.observableArrayList(
                bundle.getString("Unload"), bundle.getString("Download"), new Separator(), bundle.getString("Delete")));
        choiceBox.setOnAction(event -> {

            int index = choiceBox.getSelectionModel().getSelectedIndex();
            int rowIndex = this.getIndex();

            Column column = (Column) this.getTableColumn().getUserData();
            LobDataManager dataManager = LobDataManager.getInstance();
            TableView table = this.getTableView();

            if (index == 0) {
                boolean downloaded = dataManager.unload(rowIndex, column, table);
                setCellBackground(downloaded);
                updateItem(downloaded);
            } else if (index == 1) {
                boolean uploaded = dataManager.download(rowIndex, column, table);
                setCellBackground(uploaded);
                updateItem(uploaded);
            } else {
                setCellBackground(dataManager.delete(rowIndex, column, table));
                setItem(null);
            }
        });

        return choiceBox;
    }

}
