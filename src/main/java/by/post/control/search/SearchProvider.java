package by.post.control.search;

import by.post.control.db.DbControl;
import by.post.control.db.DbController;
import by.post.control.db.Queries;
import by.post.control.db.TableType;
import by.post.data.Table;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Very simple implementation text search
 * without  database indexing and  procedures creation.
 * It will slow on large volumes of data.
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
    public List<Table> getSearchResult(String searchValue) {

        List<Table> tables = new ArrayList<>();
        DbControl dbControl = DbController.getInstance();
        List<String> tablesList = dbControl.getTablesList(TableType.TABLE.name());

        for (String t : tablesList) {
            if (terminate) {
                break;
            }

            List<String> colNames = new ArrayList<>();

            try (Statement namesStatement = dbControl.execute(Queries.getNotLobColumnNames(t));
                 ResultSet colNamesRs = namesStatement.getResultSet()) {
                while (colNamesRs.next()) {
                    colNames.add(colNamesRs.getNString(1));
                }
            } catch (SQLException e) {
                logger.error("SearchProvider error: " + e);
            }

            if (!colNames.isEmpty()) {
                Table table = dbControl.getTableFromQuery(getQuery(t, colNames, searchValue));
                if (table.getRows() != null && !table.getRows().isEmpty()) {
                    tables.add(table);
                }
            }
        }

        logger.info(terminate ? "SearchProvider: search is canceled!" : "SearchProvider: search is done!");

        return tables;
    }

    /**
     * @param tableName
     * @param columnNames
     * @param text
     * @return query string
     */
    private String getQuery(String tableName, List<String> columnNames, String text) {

        StringBuilder sb = new StringBuilder("SELECT * FROM " + tableName + " WHERE ");

        int lastIndex = columnNames.size() - 1;

        for (String name : columnNames) {
            boolean last = columnNames.indexOf(name) == lastIndex;
            sb.append("UPPER(" + name + ") LIKE " + "UPPER('%" + text + "%')" + (last ? ";" : " OR "));
        }

        return sb.toString();
    }
}