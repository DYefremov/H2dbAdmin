package by.post.data.type;

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
}
