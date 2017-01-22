package by.post.ui;

import by.post.control.ui.ColumnDialogController;
import by.post.data.Column;
import by.post.data.type.DefaultColumnDataType;
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
import java.util.Optional;

/**
 * Column dialog for add and edit
 *
 * @author Dmitriy V.Yefremov
 */
public class ColumnDialog extends Dialog<Column> {

    private FXMLLoader loader;
    private DialogPane parent;
    private ColumnDialogController controller;
    private Column column;

    private static final Logger logger = LogManager.getLogger(ColumnDialog.class);

    public ColumnDialog() {
        column = new Column("","New", DefaultColumnDataType.VARCHAR.name());
        init();
    }

    public ColumnDialog(Column column) {
        this.column = column;
        init();
    }

    private void init() {

        try {
            loader = new FXMLLoader(ColumnDialog.class.getResource("ColumnDialog.fxml"));
            parent = loader.<DialogPane>load();
            controller = loader.getController();
            controller.setColumn(column);
        } catch (IOException e) {
            logger.error("ColumnDialog create error: " + e);
        }

        parent.setHeaderText("Please, set properties of column!");
        parent.getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        setDialogPane(parent);
        setTitle(Resources.TITLE);

        setResultConverter((dialogButton) -> {

            ButtonBar.ButtonData data = dialogButton == null ? null : dialogButton.getButtonData();

            if (data == ButtonBar.ButtonData.OK_DONE) {
                Optional<ButtonType> result = new ConfirmationDialog().showAndWait();

                if (result.get() == ButtonType.OK) {
                    column.setColumnName(controller.getName());
                    column.setType(controller.getType());
                    column.setPrimaryKey(controller.isKey());
                    column.setNotNull(controller.isNotNull());
                }
            }

            return data == ButtonBar.ButtonData.OK_DONE ? column : null;
        });

        Stage stage = (Stage) getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image(Resources.LOGO_PATH));
    }

}
