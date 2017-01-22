package by.post.control.db;

import by.post.control.ui.OpenFileDialogProvider;
import by.post.data.*;
import by.post.data.type.DefaultColumnDataType;
import javafx.scene.control.Alert;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * @author Dmitriy V.Yefremov
 */
public class LobDataManager {

    private static final Logger logger = LogManager.getLogger(LobDataManager.class);

    public LobDataManager() {

    }

    /**
     * @param rowIndex
     * @param column
     * @param table
     */
    public void save(int rowIndex, Column column, Table table) {

        if (column == null || table == null) {
            logger.error("LobDataManager error[save]: Invalid arguments!");
            return;
        }

        Cell keyCell = null;
        String query = null;
        String keyColumnName = null;
        Row row = table.getRows().get(rowIndex);

        for (Column pKeyColumn : table.getColumns()) {
            if (pKeyColumn.isPrimaryKey() || pKeyColumn.isAutoIncrement()) {
                int keyIndex = table.getColumns().indexOf(pKeyColumn);
                keyCell = row.getCells().get(keyIndex);
                keyColumnName = pKeyColumn.getColumnName();
                break;
            }
        }

        if (keyCell != null) {
            query = "SELECT " + column.getColumnName() + " FROM " +
                    table.getName() + " WHERE " + keyColumnName + "=" + keyCell.getValue();
        } else {
            new Alert(Alert.AlertType.INFORMATION, "Implemented only for tables with primary key!").showAndWait();
            return;
        }

        File file = new OpenFileDialogProvider().getSaveFileDialog("Set file name");

        Connection connection = DbController.getInstance().getCurrentConnection();

        if (connection != null) {
            saveData(column, query, file, connection);
        }
    }

    /**
     * @param column
     * @param query
     * @param file
     * @param connection
     */
    private void saveData(Column column, String query, File file, Connection connection) {

        try (Statement statement = connection.createStatement()) {
            statement.execute(query);
            try (ResultSet resultSet = statement.getResultSet()){
                if (resultSet != null) {
                    if (file != null) {
                        while (resultSet.next()) {
                            String columnName = column.getColumnName();
                            DefaultColumnDataType type = DefaultColumnDataType.valueOf(column.getType());
                            boolean isBlob = type.equals(DefaultColumnDataType.BLOB);
                            saveData(file, resultSet, columnName, isBlob);
                        }
                    }
                }
            }
        } catch (SQLException e) {
            logger.error("LobDataManager error[saveData]: " + e);
        }
    }

    /**
     * @param file
     * @param resultSet
     * @param columnName
     * @param isBlob
     */
    private void saveData(File file, ResultSet resultSet, String columnName, boolean isBlob) {

        try (OutputStream os = new BufferedOutputStream(new FileOutputStream(file)); InputStream is = isBlob ?
                resultSet.getBinaryStream(columnName) : resultSet.getAsciiStream(columnName) ){
            int b;
            while ((b = is.read()) != -1) {
                os.write(b);
            }
            os.flush();
        } catch (SQLException e) {
            logger.error("LobDataManager error[saveBlobData (SQL)]: " + e);
        } catch (FileNotFoundException e) {
            logger.error("LobDataManager error[saveBlobData (FileNotFound)]: " + e);
        } catch (IOException e) {
            logger.error("LobDataManager error[saveBlobData (IO)]: " + e);
        }
    }
}
