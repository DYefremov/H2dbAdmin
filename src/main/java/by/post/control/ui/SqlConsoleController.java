package by.post.control.ui;

import by.post.control.db.DbControl;
import by.post.control.db.DbController;
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
            statement = dbControl.execute(query);
            resultSet = statement.getResultSet();

            if (statement == null || (!statement.getMoreResults() && statement.getUpdateCount() == -1)) {
                return "No data! Please, check your request!";
            }

            result = getFormattedOut(resultSet);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (statement != null) {
                    statement.close();
                }

                if (resultSet != null) {
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

        StringBuilder stringBuilder = new StringBuilder();
        String format = "%20s|";
        ResultSetMetaData metaData = resultSet.getMetaData();

        int columnsCounter = metaData.getColumnCount();
        //TODO think about using for resolve data with TableBuilder class
        for (int i = 1; i < columnsCounter; i++) {
            stringBuilder.append(String.format(format, metaData.getColumnName(i)));
        }

        int len = stringBuilder.toString().length();

        if (len > 0) {
            // Add  separator for the header
            stringBuilder.append("\n" + String.format("%" + len + "s", " ").replace(' ', '-') + "\n");
        }

        while (resultSet.next()) {
            for (int i = 1; i <= columnsCounter; i++) {
                stringBuilder.append(String.format(format, resultSet.getString(i)));
            }
            stringBuilder.append("\n");
        }

        return stringBuilder.toString();
    }

}
