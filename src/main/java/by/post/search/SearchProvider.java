package by.post.search;

import by.post.control.Context;
import by.post.control.db.DbControl;
import by.post.control.db.DbController;
import by.post.control.db.Queries;
import by.post.control.db.TableType;
import by.post.data.type.ColumnDataType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Very simple implementation text search
 * without  database indexing and  procedures creation.
 *It will slow on large volumes of data.
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
    public Collection<String> getSearchResult(String searchValue) {

        Set<String> tableNames = new HashSet<>();

        DbControl control = DbController.getInstance();
        Connection connection = control.getCurrentConnection();

        if (connection == null) {
            logger.error("SearchProvider error: connection is null" );
            return tableNames;
        }

        ColumnDataType dataType = Context.getCurrentDataType();

        List<String> tables = control.getTablesList(TableType.TABLE.name());

        tables.parallelStream().forEach(name -> {

            if (terminate) {
                return;
            }

            try (Statement st = connection.createStatement()) {
                st.executeQuery(Queries.getTable(name));
                try (ResultSet rs = st.getResultSet()) {
                    while (rs.next()) {
                        ResultSetMetaData metaData = rs.getMetaData();

                        int count = metaData.getColumnCount();

                        for (int i = 1; i <= count; i++) {
                            int type = dataType.getNumType(metaData.getColumnTypeName(i));

                            if (!dataType.isLargeObject(type)) {
                                String data = rs.getNString(i);
                                if (data != null && data.toUpperCase().contains(searchValue.toUpperCase())) {
                                    tableNames.add(name);
                                }
                            }
                        }
                    }
                }
            } catch (SQLException e) {
                logger.error("SearchProvider error: " + e);
            }
        });

        logger.info(terminate ? "SearchProvider: search is canceled!": "SearchProvider: search is done!");

        return tableNames;
    }
}
