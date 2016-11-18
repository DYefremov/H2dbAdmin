package by.post.search;

import by.post.control.PropertiesController;
import by.post.control.db.DbControl;
import by.post.control.db.DbController;
import by.post.control.db.Queries;
import by.post.control.db.TableType;
import by.post.data.Table;
import org.h2.fulltext.FullText;
import org.h2.fulltext.FullTextLucene;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * @author Dmitriy V.Yefremov
 */
public class SearchProvider {

    public static void main(String[] args) {
        long start = System.currentTimeMillis();

        List<Table> tables = new SearchProvider().getSearchResult("dddd");
        System.out.println("Found " + tables);

        long end = System.currentTimeMillis();

        System.out.println("Consumed " + (end - start) / 1000   + "s");
    }

    /**
     * @param searchValue
     */
    private List<Table> getSearchResult(String searchValue) {

        List<Table> tables = new ArrayList<>();

        Properties ps = PropertiesController.getProperties();

        DbControl dc = DbController.getInstance();
        dc.connect(ps.getProperty("url"), ps.getProperty("user"), ps.getProperty("password"));

        List<String> tablesList = dc.getTablesList(TableType.TABLE.name());

        tablesList.stream().forEach(t -> {

            List<String> colNames = new ArrayList<String>();

            try (Statement namesStatement = dc.execute(getTableColumnNames(t));
                 ResultSet colNamesRs = namesStatement.getResultSet()) {
                while (colNamesRs.next()) {
                    colNames.add(colNamesRs.getNString(1));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

            if (!colNames.isEmpty()) {
                String query = getQuery(t, colNames, searchValue);

                try (Statement st = dc.execute(query); ResultSet rs = st.getResultSet()) {
                    if (rs != null && rs.next()) {
                        tables.add(new Table(t));
                    }
                } catch (SQLException e) {

                }
            }
        });

        return tables;
    }

    /**
     * @param tableName
     * @param columnNames
     * @param text
     * @return
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
     * @return
     */
    public static String getTableColumnNames(String tableName) {
        return "SELECT COLUMN_NAME FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_NAME='"+ tableName + "';";
    }

}
