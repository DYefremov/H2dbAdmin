package by.post.control.ui;

import by.post.control.db.DbControl;
import by.post.control.db.DbController;
import by.post.control.db.TableBuilder;
import by.post.control.db.UpdateCommands;
import by.post.data.Cell;
import by.post.data.Column;
import by.post.ui.ConfirmationDialog;
import javafx.fxml.FXML;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextArea;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

/**
 * @author Dmitriy V.Yefremov
 */
public class SqlConsoleController {

    @FXML
    private TextArea console;
    @FXML
    private TextArea consoleOut;

    private DbControl dbControl;

    private static final String SEP = "\n------------------- End query ------------------------------\n";

    private static final Logger logger = LogManager.getLogger(SqlConsoleController.class);

    public SqlConsoleController() {

    }

    //TODO add icons for buttons and small query validation !!!
    /**
     * Actions for buttons
     */
    @FXML
    public void onExecuteAction() {

        Optional<ButtonType> result = new ConfirmationDialog().showAndWait();

        String query = console.getText();

        if (result.get() == ButtonType.OK) {
            if (query.isEmpty()) {
                String info = "The query is empty. Please, repeat.\n";
                logger.info(info);
                consoleOut.appendText(info);
                return;
            }

            consoleOut.appendText(executeQuery(query) + SEP);

            logger.info("Execute query: \n" + query + SEP);
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

    @FXML
    private void initialize() {
        dbControl = DbController.getInstance();
    }

    /**
     * @param query
     * @return result as string
     * @throws SQLException
     */
    private String executeQuery(String query) {

        String result = null;
        Statement statement = null;
        ResultSet resultSet = null;

        try {
            if (isUpdateQuery(query)) {
                return "Update query. Not implemented!!!";
            }

            statement = dbControl.execute(query);

            if (statement == null || statement.getResultSet() == null) {
                return "No data! Please, check your request!";
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

        return  result != null ? result : "No data or error!";
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
                String name = column.getName();
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
            Cell max  = column.parallelStream().max(Comparator.comparing(item -> item.getValue().toString().length())).get();
            lengths.add(String.valueOf(max.getValue()).length());
        });

        return lengths;
    }

    /**
     * @return prepared output string
     */
    private String resolveDataOutput(List<List<Cell>> columnsData) {

        List<Integer> maxLengths = getMaxCellValuesLengths(columnsData);

        StringBuilder stringBuilder = new StringBuilder();
//        String format = "%20s|";

        List<String> cellsFormats = new ArrayList<>();
        maxLengths.forEach(value -> cellsFormats.add("%" + value + "s|"));


        for (List<Cell> cells : columnsData) {
            int rowIndex = columnsData.indexOf(cells);
            System.out.println(rowIndex + "= row " + cells.size() + " cells" );
            System.out.println(cells);
//            stringBuilder.append(String.format(cellsFormats.get(columnIndex), cells.get(columnIndex).getValue()));
//            stringBuilder.append("\n");
        }

        int len = stringBuilder.toString().length();

        if (len > 0) {
            // Add  separator for the header
            stringBuilder.append("\n" + String.format("%" + len + "s", " ").replace(' ', '-') + "\n");
        }

//        stringBuilder.append(columnIndex  %  headerSize != 0 ? val : val +"\n");
        return stringBuilder.toString();
    }
}
