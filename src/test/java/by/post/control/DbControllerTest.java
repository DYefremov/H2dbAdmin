package by.post.control;

import by.post.control.db.DbControl;
import by.post.control.db.DbController;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

/**
 * @author Dmitriy V.Yefremov
 */
public class DbControllerTest {
    @Test
    public void connect() throws Exception {
      DbControl control = new DbController();
      control.connect("~/test", "sa", "");

      // Проверяем наличие таблиц
      Assert.assertFalse(control.getTablesList().isEmpty());

      List<String> tabNames = control.getTablesList();
      System.out.println(tabNames);
    }

}