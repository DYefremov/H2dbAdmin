package by.post.control.db;

import by.post.control.ui.OpenFileDialogProvider;
import by.post.data.Cell;
import by.post.data.Column;
import by.post.data.Row;
import by.post.data.Table;
import by.post.data.type.DefaultColumnDataType;
import javafx.scene.control.Alert;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.sql.*;

/**
 * @author Dmitriy V.Yefremov
 */
public class LobDataManager {

    private static final Logger logger = LogManager.getLogger(LobDataManager.class);

    private static final LobDataManager INSTANCE = new LobDataManager();

    private LobDataManager() {

    }

    public static LobDataManager getInstance() {
        return INSTANCE;
    }

    /**
     * @param rowIndex
     * @param column
     * @param table
     */
    public boolean download(int rowIndex, Column column, Table table) {

        if (column == null || table == null) {
            logger.error("LobDataManager error[download]: Invalid arguments!");
            return false;
        }

        String query = getQuery(rowIndex, column, table, false);

        if (query == null) {
            logger.error("LobDataManager error[download]: query = null");
            return false;
        }

        File file = new OpenFileDialogProvider().getSaveFileDialog("Set file name");

        Connection connection = DbController.getInstance().getCurrentConnection();

        if (connection != null) {
            try {
               return saveData(column, query, file, connection);
            } catch (IOException e) {
                logger.error("LobDataManager error[download]: " + e);
            } catch (SQLException e) {
                logger.error("LobDataManager error[download]: " + e);
            }
        }

        return false;
    }

    /**
     * @param rowIndex
     * @param column
     * @param table
     */
    public boolean upload(int rowIndex, Column column, Table table) {

        if (column == null || table == null) {
            logger.error("LobDataManager error[upload]: Invalid arguments!");
            return false;
        }

        File file = new OpenFileDialogProvider().getFileDialog("Select a file", false, false);

        if (file == null) {
            return false;
        }

        String query = getQuery(rowIndex, column, table, true);

        Connection connection = DbController.getInstance().getCurrentConnection();

        if (query == null || connection == null) {
            logger.error("LobDataManager error[upload] : query = " + query + " connection = " + connection);
            return false;
        }

        try (PreparedStatement ps = connection.prepareStatement(query)) {
            if (column.getType().equals(DefaultColumnDataType.BLOB)) {
                InputStream fis = new BufferedInputStream(new FileInputStream(file));
                ps.setBinaryStream(1, fis, file.length());
                ps.execute();
                connection.commit();
            } else if (column.getType().equals(DefaultColumnDataType.CLOB)) {
                Reader reader = new BufferedReader(new FileReader(file));
                ps.setCharacterStream(1, reader, file.length());
                ps.execute();
                connection.commit();
            }
            logger.info("File " + file + " was uploaded.");
            return true;
        } catch (SQLException e) {
            logger.error("LobDataManager error[upload (SQL)]: " + e);
        } catch (FileNotFoundException e) {
            logger.error("LobDataManager error[upload (FileNotFound)]: " + e);
        }

        return false;
    }

    /**
     * @param column
     * @param query
     * @param file
     * @param connection
     */
    private boolean saveData(Column column, String query, File file, Connection connection) throws IOException, SQLException {

        try (Statement statement = connection.createStatement()) {
            statement.execute(query);
            try (ResultSet resultSet = statement.getResultSet()) {
                if (resultSet != null) {
                    while (resultSet.next()) {
                        String columnName = column.getColumnName();
                        String type = column.getType();
                        boolean isBlob = type.equals(DefaultColumnDataType.BLOB);
                        return saveData(file, resultSet, columnName, isBlob);
                    }
                }
            }
        }
        return false;
    }

    /**
     * @param file
     * @param resultSet
     * @param columnName
     * @param isBlob
     */
    private boolean saveData(File file, ResultSet resultSet, String columnName, boolean isBlob) throws SQLException, IOException {

        if (resultSet.getBinaryStream(columnName) == null) {
            new Alert(Alert.AlertType.ERROR, "No data for save!").showAndWait();
            return false;
        }

        try (InputStream is = new PushbackInputStream(isBlob ? resultSet.getBinaryStream(columnName) :
                resultSet.getAsciiStream(columnName))) {

            try (OutputStream os = new BufferedOutputStream(new FileOutputStream(file))) {
                int b;
                while ((b = is.read()) != -1) {
                    os.write(b);
                }
                os.flush();
            }
        }
        return true;
    }

    /**
     * @return query string
     */
    private String getQuery(int rowIndex, Column column, Table table, boolean upload) {

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

        if (keyCell == null) {
            new Alert(Alert.AlertType.INFORMATION, "Implemented only for tables with primary key!").showAndWait();
            return null;
        }

        if (upload) {
            query = "UPDATE " + table.getName() + " SET " + column.getColumnName() + "=(?) " +
                    " WHERE " + keyColumnName + "=" + keyCell.getValue();
        } else {
            query = "SELECT " + column.getColumnName() + " FROM " +
                    table.getName() + " WHERE " + keyColumnName + "=" + keyCell.getValue();
        }

        return query;
    }
}
