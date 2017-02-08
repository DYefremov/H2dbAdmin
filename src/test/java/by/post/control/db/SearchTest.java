package by.post.control.db;

import by.post.control.PropertiesController;
import by.post.data.type.ColumnDataType;
import by.post.data.type.H2Type;
import org.junit.Ignore;
import org.junit.Test;

import java.sql.*;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import static org.junit.Assert.assertTrue;

/**
 *@author Dmitriy V.Yefremov
 */
public class SearchTest {

    @Ignore
    @Test
    public void test() {

        final String searchValue = "сиг".toUpperCase();

        long start = System.currentTimeMillis();

        DbControl control = getDbControl();
        Connection connection = control.getCurrentConnection();

        List<String> tables = control.getTablesList(TableType.TABLE.name());
        Set<String> set  = new HashSet<>();

        ColumnDataType columnDataType = new H2Type();

        tables.parallelStream().forEach(name -> {
           try (Statement st = connection.createStatement()) {
               st.executeQuery(Queries.getTable(name));
               try (ResultSet rs = st.getResultSet()) {
                   while (rs.next()) {
                       ResultSetMetaData metaData = rs.getMetaData();

                       int count = metaData.getColumnCount();

                       for (int i = 1; i <= count; i++) {
                           int type = columnDataType.getNumType(metaData.getColumnTypeName(i));
                           if (!columnDataType.isLargeObject(type)) {
                               String data = rs.getNString(i);
                               if (data != null && data.toUpperCase().contains(searchValue)) {
                                   set.add(name);
                               }

                           }
                       }
                   }
               }
           } catch (SQLException e) {

           }
        });

        assertTrue(!set.isEmpty());

        System.out.println(set);

        long stop = System.currentTimeMillis();
        System.out.println("Consumed " + (stop - start) + "ms");
    }

    /**
     * @return DbControl with current settings
     */
    private DbControl getDbControl() {

        Properties properties = PropertiesController.getProperties();
        String user = properties.getProperty("user");
        String password = properties.getProperty("password");
        String url = properties.getProperty("url");

        DbControl control = DbController.getInstance();
        control.connect(url, user, password);

        return control;
    }

}
