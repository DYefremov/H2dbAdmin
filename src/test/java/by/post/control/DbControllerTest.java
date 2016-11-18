package by.post.control;


import by.post.control.db.DbControl;
import by.post.control.db.DbController;
import by.post.control.db.TableType;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import java.util.List;
import java.util.Properties;

/**
 * @author Dmitriy V.Yefremov
 */
public class DbControllerTest {
    @Ignore
    @Test
    public void connect() throws Exception {

        Properties properties = PropertiesController.getProperties();
        String user = properties.getProperty("user");
        String password = properties.getProperty("password");
        String path = properties.getProperty("path");
        String db = properties.getProperty("db");

        DbControl control = DbController.getInstance();
        control.connect(path, user, password);

        // Проверяем наличие таблиц
        Assert.assertFalse(control.getTablesList(TableType.TABLE.name()).isEmpty());

        List<String> tabNames = control.getTablesList(TableType.TABLE.name());
        System.out.println(tabNames);
    }
}