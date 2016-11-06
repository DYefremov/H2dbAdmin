package by.post.control.db;

/**
 * @author Dmitriy V.Yefremov
 */
public enum TableType {

    TABLE, VIEW;

    public String preparedName() {
        return name().replaceAll("_", " ");
    }
}
