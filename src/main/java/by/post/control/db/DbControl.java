package by.post.control.db;

import by.post.data.Column;
import by.post.data.Row;
import by.post.data.Table;

import java.util.List;

/**
 * @author Dmitriy V.Yefremov
 */
public interface DbControl {

  /**
   * @param path
   * @param user
   * @param password
   */
  void connect(String path, String user, String password);

  /**
   * @return table names list
   */
  List<String> getTablesList();

  /**
   * @param name
   * @return table by name
   */
  Table getTable(String name);

  /**
   * @param table
   */
  void update(Table table);
}
