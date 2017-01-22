package by.post.data.type;

import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Collection;

/**
 * @author Dmitriy V.Yefremov
 */
public interface ColumnDataType {

    /**
     * @return types names
     */
    Collection<String> getValues();

    /**
     * @return int value by name
     */
    int getNumType(String name);

    /**
     * @param type
     * @return type name by int value
     */
    String typeName(int type);

    /**
     * @param meta
     * @param columnIndex
     * @return
     */
    int getValueTypeFromResultSet(ResultSetMetaData meta, int columnIndex) throws SQLException;

    /**
     * @param type
     * @return
     */
    boolean isLargeObject(int type);
}
