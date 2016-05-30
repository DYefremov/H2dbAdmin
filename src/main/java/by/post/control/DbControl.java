package by.post.control;

/**
 * @author Dmitriy V.Yefremov
 */
public interface DbControl {

  /**
   * @param db
   * @param user
   * @param password
   */
  void connect(String db, String user, String password);
}
