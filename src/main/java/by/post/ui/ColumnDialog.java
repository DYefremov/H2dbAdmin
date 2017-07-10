package by.post.ui;

import by.post.control.Context;
import by.post.control.ui.dialogs.ColumnDialogController;
import by.post.data.Column;
import by.post.data.type.DefaultColumnDataType;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Optional;
import java.util.ResourceBundle;

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

    public ColumnDialog() throws IOException {
        column = new Column("", "New", DefaultColumnDataType.VARCHAR);
        init();
    }

    public ColumnDialog(Column column) throws IOException {
        this.column = column;
        init();
    }

    private void init() throws IOException {

        loader = new FXMLLoader(getClass().getResource("dialogs/ColumnDialog.fxml"));
        loader.setResources(ResourceBundle.getBundle("bundles.Lang", Context.getLocale()));
        parent = loader.load();
        controller = loader.getController();
        controller.setColumnProperties(column);

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
                    column.setDefaultValue(controller.getDefaultValue());
                }
            }

            return data == ButtonBar.ButtonData.OK_DONE ? column : null;
        });

        Stage stage = (Stage) getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image(Resources.LOGO_PATH));
    }

}
