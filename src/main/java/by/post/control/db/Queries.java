package by.post.control.db;

/**
 * @author Dmitriy V.Yefremov
 */
public class Queries {

    public static String createTable(String name) {
        return "CREATE TABLE " + name.toUpperCase();
    }

    public static String deleteTable(String name){
        return "DROP TABLE " + name.toUpperCase();
    }

}
