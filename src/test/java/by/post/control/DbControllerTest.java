package by.post.control;


import by.post.control.db.DbControl;
import by.post.control.db.DbController;
import org.junit.Assert;
import org.junit.Test;


import java.util.List;
import java.util.Properties;

/**
 * @author Dmitriy V.Yefremov
 */
public class DbControllerTest {
    @Test
    public void connect() throws Exception {
        Properties properties = PropertiesController.getProperties();
        String user = properties.getProperty("user");
        String password = properties.getProperty("password");
        String path = properties.getProperty("path");
        String db = properties.getProperty("db");

        DbControl control = new DbController();
        control.connect(path, db, user, password);

        // Проверяем наличие таблиц
        Assert.assertFalse(control.getTablesList().isEmpty());

        List<String> tabNames = control.getTablesList();
        System.out.println(tabNames);
    }

}