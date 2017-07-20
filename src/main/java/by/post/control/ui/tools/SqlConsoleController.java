package by.post.control.ui.tools;

import by.post.control.db.DbControl;
import by.post.control.db.DbController;
import by.post.control.db.TableDataResolver;
import by.post.data.Row;
import by.post.data.Table;
import by.post.ui.ConfirmationDialog;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author Dmitriy V.Yefremov
 */
public class SqlConsoleController {

    @FXML
    private TextArea console;
    @FXML
    private TableView tableView;
    @FXML
    private Label viewLabel;

    private DbControl dbControl;
    private List<String> queriesHistory;
    private int historyPosition;

    private static final int HISTORY_SIZE = 10;
    private static final String DONE_MESSAGE = "\n---- Done! ----\n\n";
    private static final String ERROR_MESSAGE = "No data or error.See \"More info.\" for details!";

    private static final Logger logger = LogManager.getLogger(SqlConsoleController.class);

    public SqlConsoleController() {

    }

    /**
     * Actions for buttons
     */
    @FXML
    public void onExecuteAction() {

        Optional<ButtonType> result = new ConfirmationDialog().showAndWait();

        String query = console.getText();

        if (result.get() == ButtonType.OK) {
            logger.info("Execute query: \n" + query);
            setData(executeQuery(query));
            console.clear();
        }
    }

    /**
     * @param table
     */
    private void setData(Table table) {

        List<Row> rows = table.getRows();

        boolean hasData = rows != null && !rows.isEmpty();
        setOutputMessage(hasData ? " " : ERROR_MESSAGE);

        if (!hasData) {
            return;
        }

        TableDataResolver resolver = new TableDataResolver(table);

        Platform.runLater(() -> {
            tableView.getColumns().addAll(resolver.getTableColumns());
            tableView.setItems(resolver.getItems());
        });
    }

    @FXML
    public void onClearAction() {

        Optional<ButtonType> result = new ConfirmationDialog().showAndWait();

        if (result.get() == ButtonType.OK) {
            console.clear();
            clearOutput();
        }
    }

    /**
     * Actions for keyboard
     */
    @FXML
    public void onEnterKey(KeyEvent e) {

        KeyCode keyCode = e.getCode();

        switch (keyCode) {
            case ENTER:
                String query = console.getText();
                // (query need ends with ";" to execute)
                if (query.endsWith(";")) {
                    onExecuteAction();
                    console.clear();
                    historyPosition = 0;
                    //Without this wrapping next code not work properly!!!
                    Platform.runLater(() -> {
                        //return cursor on start position
                        console.selectPositionCaret(0);
                    });
                }
                break;

            case UP:
                if (historyPosition < queriesHistory.size()) {
                    console.setText(queriesHistory.get(queriesHistory.size() - ++historyPosition));
                }
                break;

            case DOWN:
                if (historyPosition > 1) {
                    console.setText(queriesHistory.get(queriesHistory.size() - --historyPosition));
                }
                break;

            default:
                break;
        }
    }

    @FXML
    private void initialize() {

        dbControl = DbController.getInstance();
        queriesHistory = new ArrayList<>(HISTORY_SIZE + 1);
    }

    /**
     * @param query
     * @return result as string
     * @throws SQLException
     */
    private Table executeQuery(String query) {
        // Store queries
        queriesHistory.add(query);

        if (queriesHistory.size() > HISTORY_SIZE) {
            queriesHistory.remove(0);
        }

        clearOutput();
        Table table = dbControl.getTableFromQuery(query);

        if (table.isUpdate()) {
            String message = table.isUpdated() ? DONE_MESSAGE : ERROR_MESSAGE;
            setOutputMessage(message);
            return table;
        }

        if (table.getRows() == null || table.getRows().isEmpty()) {
            setOutputMessage(ERROR_MESSAGE);
        }

        return table;
    }

    /**
     * @param message
     */
    private void setOutputMessage(String message) {

        Platform.runLater(() -> {
            viewLabel.setText(message);
            tableView.setVisible(true);
        });
    }

    /**
     *Clear table view
     */
    private void clearOutput() {

        Platform.runLater(() -> {
            tableView.getColumns().clear();
            tableView.getItems().clear();
            tableView.refresh();
            tableView.setVisible(false);
        });
    }

}

