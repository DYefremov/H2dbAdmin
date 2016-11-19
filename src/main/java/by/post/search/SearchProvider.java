package by.post.search;

import by.post.control.PropertiesController;
import by.post.control.db.DbControl;
import by.post.control.db.DbController;
import by.post.control.db.TableType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * Very simple implementation text search
 * without  database indexing and  procedures creation.
 *It will slow on large volumes of data.
 * Maybe this implementation is temporary.
 *
 * @author Dmitriy V.Yefremov
 */
public class SearchProvider {

    private boolean terminate;

    private static final Logger logger = LogManager.getLogger(SearchProvider.class);

    public void setTerminate(boolean terminate) {
        this.terminate = terminate;
    }

    /**
     * @param searchValue
     */
    public List<String> getSearchResult(String searchValue) {

        List<String> tables = new ArrayList<>();

        Properties properties = PropertiesController.getProperties();

        DbControl dbControl = DbController.getInstance();
        dbControl.connect(properties.getProperty("url"), properties.getProperty("user"), properties.getProperty("password"));

        List<String> tablesList = dbControl.getTablesList(TableType.TABLE.name());

        tablesList.forEach(t -> {

            if (terminate) {
                return;
            }

            List<String> colNames = new ArrayList<String>();

            try (Statement namesStatement = dbControl.execute(getTableColumnNames(t));
                 ResultSet colNamesRs = namesStatement.getResultSet()) {
                while (colNamesRs.next()) {
                    colNames.add(colNamesRs.getNString(1));
                }
            } catch (SQLException e) {
                logger.error("SearchProvider error: " + e);
            }

            if (!colNames.isEmpty()) {
                String query = getQuery(t, colNames, searchValue);

                try (Statement st = dbControl.execute(query); ResultSet rs = st.getResultSet()) {
                    if (rs != null && rs.next()) {
                        tables.add(t);
                    }
                } catch (SQLException e) {
                    logger.error("SearchProvider error: " + e);
                }
            }
        });

        logger.info(terminate ? "SearchProvider: search is canceled!": "SearchProvider: search is done!");

        return tables;
    }

    /**
     * @param tableName
     * @param columnNames
     * @param text
     * @return query string
     */
    private String getQuery(String tableName, List<String> columnNames, String text) {

        String columns = columnNames.toString().replace('[', ' ');
        columns = columns.replace(']', ' ');

        StringBuilder sb = new StringBuilder("SELECT DISTINCT " + columns);
        sb.append(" FROM " + tableName + " WHERE ");

        for (String name : columnNames) {
            if (columnNames.indexOf(name) != columnNames.size() - 1) {
                sb.append("UPPER(" + name + ") LIKE " + "UPPER('%" + text + "%') OR ");
            } else {
                sb.append("UPPER(" + name + ") LIKE " + "UPPER('%" + text + "%');");
            }
        }

        return sb.toString();
    }


    /**
     * @param tableName
     * @return columns names
     */
    private  String getTableColumnNames(String tableName) {
        return "SELECT COLUMN_NAME FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_NAME='"+ tableName + "';";
    }

}
