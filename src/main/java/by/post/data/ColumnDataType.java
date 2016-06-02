package by.post.data;

import java.sql.Types;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Dmitry V.Yefremov
 */
public final class ColumnDataType {

    private static Map<Integer, String> types;

    private ColumnDataType() {
        types = new HashMap<>();
        types.put(Types.ARRAY, "ARRAY");
        types.put(Types.VARCHAR, "VARCHAR");
        types.put(Types.INTEGER, "INTEGER");
        types.put(Types.DECIMAL, "DECIMAL");
    }

    /**
     * Получение типа данных столбца
     *
     * @param colType
     * @return data type of column
     */
    public static String getColumnDataType(int colType) {
        return types.containsKey(colType) ? types.get(colType) : "UNDEFINED";
    }
}
