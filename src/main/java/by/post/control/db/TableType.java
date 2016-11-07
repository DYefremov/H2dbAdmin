package by.post.control.db;

/**
 * @author Dmitriy V.Yefremov
 */
public enum TableType {

    TABLE, VIEW, SYSTEM_TABLE;

    public String preparedName() {
        return name().replaceAll("_", " ");
    }
}
