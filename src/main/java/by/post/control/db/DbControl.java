package by.post.control.db;

import by.post.data.Table;

import java.util.List;

/**
 * @author Dmitriy V.Yefremov
 */
public interface DbControl {

    /**
     * @param path
     * @param db
     * @param user
     * @param password
     */
    void connect(String path, String db, String user, String password);

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
