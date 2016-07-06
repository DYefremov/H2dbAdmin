package by.post.data;

import java.sql.Types;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Dmitry V.Yefremov
 */
public class ColumnDataType {

    private static Map<Integer, String> types;
    private static final int UNDEFINED =  -1;
    private static final String UNDEFINED_STR =  "UNDEFINED";

    static {

        types = new HashMap<>();

        types.put(Types.BIT, "BIT");
        types.put(Types.TINYINT, "TINYINT");
        types.put(Types.SMALLINT, "SMALLINT");
        types.put(Types.INTEGER, "INTEGER");
        types.put(Types.BIGINT, "BIGINT");
        types.put(Types.FLOAT, "FLOAT");
        types.put(Types.REAL, "REAL");
        types.put(Types.DOUBLE, "DOUBLE");
        types.put(Types.NUMERIC, "NUMERIC");
        types.put(Types.DECIMAL, "DECIMAL");
        types.put(Types.CHAR, "CHAR");
        types.put(Types.VARCHAR, "VARCHAR");
        types.put(Types.LONGVARCHAR, "LONGVARCHAR");
        types.put(Types.DATE, "DATE");
        types.put(Types.TIME, "TIME");
        types.put(Types.TIMESTAMP, "TIMESTAMP");
        types.put(Types.BINARY, "BINARY");
        types.put(Types.VARBINARY, "VARBINARY");
        types.put(Types.LONGVARBINARY, "LONGVARBINARY");
        types.put(Types.NULL, "NULL");
        types.put(Types.OTHER, "OTHER");
        types.put(Types.JAVA_OBJECT, "JAVA_OBJECT");
        types.put(Types.DISTINCT, "DISTINCT");
        types.put(Types.STRUCT, "STRUCT");
        types.put(Types.ARRAY, "ARRAY");
        types.put(Types.BLOB, "BLOB");
        types.put(Types.CLOB, "CLOB");
        types.put(Types.REF, "REF");
        types.put(Types.DATALINK, "DATALINK");
        types.put(Types.BOOLEAN, "BOOLEAN");
        types.put(Types.ROWID, "ROWID");
        types.put(Types.NCHAR, "NCHAR");
        types.put(Types.NVARCHAR, "NVARCHAR");
        types.put(Types.LONGNVARCHAR, "LONGNVARCHAR");
        types.put(Types.NCLOB, "NCLOB");
        types.put(Types.SQLXML, "SQLXML");
        types.put(Types.REF_CURSOR, "REF_CURSOR");
        types.put(Types.TIME_WITH_TIMEZONE, "TIME_WITH_TIMEZONE");
        types.put(Types.TIMESTAMP_WITH_TIMEZONE, "TIMESTAMP_WITH_TIMEZONE");
    }

    /**
     * @param colType
     * @return data type of column
     */
    public static String getType(int colType) {
        return types.containsKey(colType) ? types.get(colType) : UNDEFINED_STR;
    }

    /**
     * @return numerical type value by name
     */
    public static int getNumType(String name) {

        int type = UNDEFINED;

        for (Map.Entry<Integer, String> entry : types.entrySet()) {
            if (entry.getValue().equals(name)) {
                type = entry.getKey();
            }
        }

        return type;
    }

}
