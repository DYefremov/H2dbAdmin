package by.post.control.ui;

import by.post.control.db.DbControl;
import by.post.control.db.DbController;
import by.post.control.db.TableBuilder;
import by.post.data.ColumnDataType;
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
import java.util.LinkedList;
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

            try {
                consoleOut.appendText(executeQuery(query) + SEP);
            } catch (SQLException e) {
                logger.error("SqlConsoleController error in onExecuteAction:" + e);
            }

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
    private String executeQuery(String query) throws SQLException {

        Statement statement = dbControl.execute(query);
        ResultSet resultSet = statement.getResultSet();

        String result = getFormattedOut(resultSet);

        resultSet.close();
        statement.close();

        return result;
    }

    /**
     * @param resultSet
     * @return formatted output
     */
    private String getFormattedOut(ResultSet resultSet) throws SQLException {

        StringBuilder stringBuilder = new StringBuilder();
        String format = "%20s|";

        if (resultSet == null) {
            return stringBuilder.append("No data! Please, check your request!").toString();
        }

        ResultSetMetaData metaData = resultSet.getMetaData();

        int columnsCounter = metaData.getColumnCount();
        //TODO think about using for resolve data with TableBuilder class
        for (int i = 1; i < columnsCounter; i++) {
            stringBuilder.append(String.format(format, metaData.getColumnName(i)));
        }

        int len = stringBuilder.toString().length();
        // Add  separator for the header
        stringBuilder.append("\n" + String.format("%" + len + "s", " ").replace(' ', '-') + "\n");

        while (resultSet.next()) {
            for (int i = 1; i < columnsCounter; i++) {
                stringBuilder.append(String.format(format, resultSet.getString(i)));
            }
            stringBuilder.append("\n");
        }

        return stringBuilder.toString();
    }

}
