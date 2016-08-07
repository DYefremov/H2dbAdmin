package by.post.ui;

import by.post.control.ui.ChoiceColumnTypeDialogController;
import by.post.data.Column;
import by.post.data.ColumnDataType;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

/**
 * @author Dmitriy V.Yefremov
 */
public class ChoiceColumnTypeDialog extends Dialog<Column> {

    private FXMLLoader loader;
    private DialogPane parent;
    private ChoiceColumnTypeDialogController controller;
    private Column column;

    private static final Logger logger = LogManager.getLogger(ChoiceColumnTypeDialog.class);

    public ChoiceColumnTypeDialog() {
        column = new Column("New column", ColumnDataType.getNumType(ColumnDataType.VARCHAR.name()));
        init();
    }

    public ChoiceColumnTypeDialog(Column column) {
        this.column = column;
        init();
    }

    private void init() {
        try {
            loader = new FXMLLoader(AddColumnDialog.class.getResource("ChoiceColumnTypeDialog.fxml"));
            parent = loader.<DialogPane>load();
            controller = loader.getController();
            controller.setColumn(column);
        } catch (IOException e) {
            logger.error("AddColumnDialog create error: " + e);
        }

        parent.setHeaderText("Please, set type of column!");
        parent.getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        setDialogPane(parent);
        setTitle(Resources.TITLE);

        setResultConverter((dialogButton) -> {

            ButtonBar.ButtonData data = dialogButton == null ? null : dialogButton.getButtonData();

            if (data == ButtonBar.ButtonData.OK_DONE) {
                column.setType(controller.getType());
                column.setPrimaryKey(controller.isKey());
                column.setNotNull(controller.isNotNull());
                return column;
            }

            return null;
        });

        Stage stage = (Stage) getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image(Resources.LOGO_PATH));
    }
}
