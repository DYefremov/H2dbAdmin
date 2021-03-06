package by.post.control.db;

import by.post.control.ui.dialogs.OpenFileDialogProvider;
import by.post.data.Cell;
import by.post.data.Column;
import by.post.data.Row;
import by.post.data.type.DefaultColumnDataType;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
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
    public boolean unload(int rowIndex, Column column, TableView table) {

        if (column == null || table == null) {
            logger.error("LobDataManager error[unload]: Invalid arguments!");
            return false;
        }

        String query = getQuery(rowIndex, column, table, Commands.UNLOAD);

        if (query == null) {
            logger.error("LobDataManager error[unload]: query = null");
            return false;
        }

        File file = new OpenFileDialogProvider().getSaveFileDialog("Set file name");

        if (file == null) {
            return false;
        }

        Connection connection = DbController.getInstance().getCurrentConnection();

        if (connection != null) {
            try {
                return saveData(column, query, file, connection);
            } catch (IOException e) {
                logger.error("LobDataManager error[unload]: " + e);
            } catch (SQLException e) {
                logger.error("LobDataManager error[unload]: " + e);
            }
        }

        return false;
    }

    /**
     * @param rowIndex
     * @param column
     * @param table
     */
    public boolean download(int rowIndex, Column column, TableView<Row> table) {

        if (column == null || table == null) {
            logger.error("LobDataManager error[download]: Invalid arguments!");
            return false;
        }

        File file = new OpenFileDialogProvider().getFileDialog("Select a file", false, false);

        if (file == null) {
            return false;
        }

        String query = getQuery(rowIndex, column, table, Commands.DOWNLOAD);

        Connection connection = DbController.getInstance().getCurrentConnection();

        if (query == null || connection == null) {
            logger.error("LobDataManager error[download] : query = " + query + " connection = " + connection);
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
            logger.error("LobDataManager error[download (SQL)]: " + e);
        } catch (FileNotFoundException e) {
            logger.error("LobDataManager error[download (FileNotFound)]: " + e);
        }

        return false;
    }

    /**
     * Deleting data from cell
     *
     * @param rowIndex
     * @param column
     * @param table
     * @return
     */
    public boolean delete(int rowIndex, Column column, TableView<Row> table) {

        String query = getQuery(rowIndex, column, table, Commands.DELETE);
        DbControl dbControl = DbController.getInstance();

        if (query == null) {
            logger.error("LobDataManager error[delete] : query = " + query);
            return false;
        }

        try {
            dbControl.execute(query);
            logger.info("Deleting data from the cell is completed!");
            return true;
        } catch (SQLException e) {
            logger.error("LobDataManager error[delete] : " + e);
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
    private String getQuery(int rowIndex, Column column, TableView<Row> table, Commands command) {

        TableColumn keyColumn = table.getColumns().stream().filter(c -> {
            Column cl = (Column) c.getUserData();
            return cl.isPrimaryKey() || cl.isAutoIncrement();
        }).findAny().orElse(null);

        if (keyColumn == null) {
            new Alert(Alert.AlertType.INFORMATION, "Implemented only for tables with primary key!").showAndWait();
            return null;
        }

        int keyIndex = table.getColumns().indexOf(keyColumn);
        Row row = table.getItems().get(rowIndex);
        Cell keyCell = row.getCells().get(keyIndex);
        String keyColumnName = ((Column)keyColumn.getUserData()).getColumnName();

        String query = null;

        if (command.equals(Commands.DOWNLOAD)) {
            query = "UPDATE " + table.getId() + " SET " + column.getColumnName() + "=(?) " +
                    " WHERE " + keyColumnName + "=" + keyCell.getValue();
        } else if (command.equals(Commands.UNLOAD)) {
            query = "SELECT " + column.getColumnName() + " FROM " +
                    table.getId() + " WHERE " + keyColumnName + "=" + keyCell.getValue();
        } else if (command.equals(Commands.DELETE)) {
            query = "UPDATE " + table.getId() + " SET " + column.getColumnName() + "=NULL " +
                    " WHERE " + keyColumnName + "=" + keyCell.getValue();
        }

        return query;
    }

}
