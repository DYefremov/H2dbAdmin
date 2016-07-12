package by.post.control.ui;

import by.post.ui.ConfirmationDialog;
import javafx.fxml.FXML;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextArea;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Optional;

/**
 * @author Dmitriy V.Yefremov
 */
public class SqlConsoleController {

    @FXML
    private TextArea console;
    @FXML
    private TextArea consoleOut;

    private static final String SEP = "\n------------------- End query ------------------------------";

    private static final Logger logger = LogManager.getLogger(SqlConsoleController.class);

    public SqlConsoleController() {

    }

    //TODO add icons for buttons and small query validation !!!
    @FXML
    public void onExecuteAction() {

        Optional<ButtonType> result = new ConfirmationDialog().showAndWait();

        if (result.get() == ButtonType.OK) {
            String query = console.getText();
            if (query.isEmpty()) {
                logger.info("The query is empty. Please, repeat.");
                return;
            }
            consoleOut.appendText(console.getText() + SEP);
            logger.info("Execute query: \n" + query + SEP);
            console.clear();
        }
    }

    @FXML
    public void onClearAction() {

        Optional<ButtonType> result = new ConfirmationDialog().showAndWait();

        if (result.get() == ButtonType.OK) {
            console.clear();
        }
    }

    @FXML
    private void initialize() {

    }

}
