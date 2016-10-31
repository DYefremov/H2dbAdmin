package by.post.control.ui;

import by.post.control.db.DbControl;
import by.post.control.db.DbController;
import by.post.control.db.TableBuilder;
import by.post.control.db.UpdateCommands;
import by.post.data.Cell;
import by.post.data.Column;
import by.post.ui.ConfirmationDialog;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

/**
 * @author Dmitriy V.Yefremov
 */
public class SqlConsoleController {

    @FXML
    private TextArea console;
    @FXML
    private TextArea consoleOut;

    private DbControl dbControl;
    private List<String> queriesHistory;
    private int historyPosition;

    private static final int HISTORY_SIZE = 10;
    private static final String DONE_MESSAGE = "\n---- Done! ----\n\n";
    private static final String ERROR_MESSAGE = "Error.See \"More info.\" for details!";

    private static final Logger logger = LogManager.getLogger(SqlConsoleController.class);

    public SqlConsoleController() {

    }

    /**
     * Actions for buttons
     */
    @FXML
    public void onExecuteAction() {

        Optional<ButtonType> result = new ConfirmationDialog().showAndWait();

        String query = console.getText().toUpperCase();

        if (result.get() == ButtonType.OK) {
            logger.info("Execute query: \n" + query);
            consoleOut.appendText(executeQuery(query));
            console.clear();
        }
    }

    @FXML
    public void onClearAction() {

        Optional<ButtonType> result = new ConfirmationDialog().showAndWait();

        if (result.get() == ButtonType.OK) {
            console.clear();
            consoleOut.clear();
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
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            //return cursor on start position
                            console.selectPositionCaret(0);
                        }
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
    private String executeQuery(String query) {
        // Store queries
        queriesHistory.add(query);

        if (queriesHistory.size() > HISTORY_SIZE) {
            queriesHistory.remove(0);
        }
        
        consoleOut.clear();

        String result = null;
        Statement statement = null;
        ResultSet resultSet = null;

        try {
            if (isUpdateQuery(query)) {
                statement = dbControl.update(query);
                return statement.getUpdateCount() != -1 ? DONE_MESSAGE : ERROR_MESSAGE;
            }

            statement = dbControl.execute(query);

            if (statement == null || statement.getResultSet() == null) {
                return ERROR_MESSAGE;
            }

            resultSet = statement.getResultSet();
            result = getFormattedOut(resultSet);
        } catch (SQLException e) {
            logger.error("SqlConsoleController error in executeQuery: " + e);
        } finally {
            try {
                if (statement != null && !statement.isClosed()) {
                    statement.close();
                }

                if (resultSet != null && !resultSet.isClosed()) {
                    resultSet.close();
                }
            } catch (SQLException e) {
                logger.error("SqlConsoleController error in executeQuery: " + e);
            }
        }

        return result != null ? result : "No data or error!";
    }

    /**
     * @param resultSet
     * @return formatted output
     */
    private String getFormattedOut(ResultSet resultSet) throws SQLException {

        TableBuilder tableBuilder = new TableBuilder();

        ResultSetMetaData metaData = resultSet.getMetaData();
        List<Column> columnsHeader = tableBuilder.getColumns(metaData);
        List<List<Cell>> columnsData = new ArrayList<>();

        if (columnsHeader != null || !columnsHeader.isEmpty()) {
            columnsHeader.forEach(column -> {
                List<Cell> headerCells = new ArrayList<>();
                String name = column.getColumnName();
                headerCells.add(new Cell(name, name, name));
                columnsData.add(headerCells);
            });
        }

        while (resultSet.next()) {
            List<Cell> cells = tableBuilder.getCells(resultSet);

            if (cells != null || cells.isEmpty()) {
                int headerSize = columnsHeader.size();
                cells.forEach(cell -> {
                    int columnIndex = cells.indexOf(cell);

                    if (!columnsData.isEmpty() && columnsData.size() == headerSize) {
                        columnsData.get(columnIndex).add(cell);
                    } else {
                        List<Cell> column = new ArrayList<Cell>();
                        column.add(cell);
                        columnsData.add(columnIndex, column);
                    }
                });
            }
        }

        return resolveDataOutput(columnsData);
    }

    /**
     * @param query
     * @return true if query has update command
     */
    private boolean isUpdateQuery(String query) {

        UpdateCommands[] commands = UpdateCommands.values();

        for (int i = 0; i < commands.length; i++) {
            if (query.contains(commands[i].name())) {
                return true;
            }
        }
        return false;
    }

    /**
     * @param values
     * @return max length of value for cell
     */
    private List<Integer> getMaxCellValuesLengths(List<List<Cell>> values) {

        List<Integer> lengths = new ArrayList<>();

        values.forEach(column -> {
            Cell max = column.parallelStream().max(Comparator.comparing(item -> item.getValue() != null ? item.getValue().toString().length() : 0)).get();
            lengths.add(String.valueOf(max.getValue()).length());
        });

        return lengths;
    }

    /**
     * @return prepared output string
     */
    private String resolveDataOutput(List<List<Cell>> columnsData) {

        if (columnsData == null || columnsData.isEmpty()) {
            return "No data for output!";
        }

        StringBuilder stringBuilder = new StringBuilder();

        List<Integer> maxLengths = getMaxCellValuesLengths(columnsData);
        List<String> cellsFormats = new ArrayList<>();
        maxLengths.forEach(value -> cellsFormats.add("%" + ++value + "s|"));

        List<Cell> cells = columnsData.get(0);
        int headerSize = columnsData.size();

        String separator = "";
        int maxRows = cells.size() - 1;

        for (Cell cell : cells) {
            int rowIndex = cells.indexOf(cell);

            for (List<Cell> row : columnsData) {
                int columnIndex = columnsData.indexOf(row);
                String val = String.format(cellsFormats.get(columnIndex), String.valueOf(row.get(rowIndex).getValue()));
                stringBuilder.append(++columnIndex != headerSize ? val : val + "\n");
            }
            // Add  separator for the header
            if (rowIndex == 0) {
                int sepLength = stringBuilder.length();

                if (sepLength > 0) {
                    // Build separator for the header and bottom
                    separator = String.format("%" + --sepLength + "s", " ").replace(' ', '-') + "\n";
                    stringBuilder.append(separator);
                }
            } else if (rowIndex == maxRows) {
                stringBuilder.append(separator);
            }
        }

        return stringBuilder.toString();
    }
}
