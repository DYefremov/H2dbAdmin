package by.post.ui;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.Image;
import javafx.stage.Stage;

/**
 * @author Dmitriy V.Yefremov
 */
public class InputDialog extends TextInputDialog {

    private final String TITLE = "H2dbAdmin";

    public InputDialog(){
        init();
    }

    /**
     * @param headerText
     * @param inputText
     * @param filtered
     */
    public InputDialog(String headerText, String inputText, boolean filtered) {
        super(inputText);
        init();
        setHeaderText(headerText);

        if (filtered) {
            setFiltered();
        }
    }

    private void init() {
        setTitle(TITLE);
        Stage stage =  (Stage) getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image(Resources.LOGO_PATH));
    }

    /**
     * Set filter for text input (only digits). Button "OK" will not send action.
     */
    public void setFiltered() {
        Button okButton = (Button) getDialogPane().lookupButton(ButtonType.OK);
        okButton.addEventFilter(ActionEvent.ACTION, ae -> {
            if (!getEditor().getCharacters().toString().matches("\\d+")) {
                getEditor().setText("Please, set valid value!");
                ae.consume();
            }
        });
    }
}
