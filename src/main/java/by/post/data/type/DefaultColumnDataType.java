package by.post.data.type;

import java.sql.Types;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Dmitry V.Yefremov
 */
public class DefaultColumnDataType implements  ColumnDataType {

    public static final String BIT = "BIT";
    public static final String TINYINT = "TINYINT";
    public static final String SMALLINT = "SMALLINT";
    public static final String INTEGER = "INTEGER";
    public static final String BIGINT = "BIGINT";
    public static final String FLOAT = "FLOAT";
    public static final String REAL = "REAL";
    public static final String DOUBLE = "BIT";
    public static final String NUMERIC = "NUMERIC";
    public static final String DECIMAL = "DECIMAL";
    public static final String CHAR = "CHAR";
    public static final String VARCHAR = "VARCHAR";
    public static final String LONGVARCHAR = "LONGVARCHAR";
    public static final String DATE = "DATE";
    public static final String TIME = "TIME";
    public static final String TIMESTAMP = "TIMESTAMP";
    public static final String TIME_WITH_TIMEZONE = "TIME WITH TIMEZONE";
    public static final String TIMESTAMP_WITH_TIMEZONE = "TIMESTAMP WITH TIMEZONE";
    public static final String BINARY = "BINARY";
    public static final String VARBINARY = "VARBINARY";
    public static final String LONGVARBINARY = "LONGVARBINARY";
    public static final String NULL = "NULL";
    public static final String OTHER = "OTHER";
    public static final String JAVA_OBJECT = "JAVA_OBJECT";
    public static final String DISTINCT = "DISTINCT";
    public static final String STRUCT = "STRUCT";
    public static final String ARRAY = "ARRAY";
    public static final String BLOB = "BLOB";
    public static final String CLOB = "CLOB";
    public static final String REF = "REF";
    public static final String DATALINK = "DATALINK";
    public static final String BOOLEAN = "BOOLEAN";
    public static final String ROWID = "ROWID";
    public static final String NCHAR = "NCHAR";
    public static final String NVARCHAR = "NVARCHAR";
    public static final String LONGNVARCHAR = "LONGNVARCHAR";
    public static final String NCLOB = "NCLOB";
    public static final String SQLXML = "SQLXML";
    public static final String REF_CURSOR = "REF_CURSOR";

    private static Map<Integer, String> types;

    static {

        types = new HashMap<>();

        types.put(Types.BIT, BIT);
        types.put(Types.TINYINT, TINYINT);
        types.put(Types.SMALLINT, SMALLINT);
        types.put(Types.INTEGER, INTEGER);
        types.put(Types.BIGINT, BIGINT);
        types.put(Types.FLOAT, FLOAT);
        types.put(Types.REAL, REAL);
        types.put(Types.DOUBLE, DOUBLE);
        types.put(Types.NUMERIC, NUMERIC);
        types.put(Types.DECIMAL, DECIMAL);
        types.put(Types.CHAR, CHAR);
        types.put(Types.VARCHAR, VARCHAR);
        types.put(Types.LONGVARCHAR, LONGVARCHAR);
        types.put(Types.DATE, DATE);
        types.put(Types.TIME, TIME);
        types.put(Types.TIMESTAMP, TIMESTAMP);
        types.put(Types.BINARY, BINARY);
        types.put(Types.VARBINARY, VARBINARY);
        types.put(Types.LONGVARBINARY, LONGVARBINARY);
        types.put(Types.NULL, NULL);
        types.put(Types.OTHER, OTHER);
        types.put(Types.JAVA_OBJECT, JAVA_OBJECT);
        types.put(Types.DISTINCT, DISTINCT);
        types.put(Types.STRUCT, STRUCT);
        types.put(Types.ARRAY, ARRAY);
        types.put(Types.BLOB, BLOB);
        types.put(Types.CLOB, CLOB);
        types.put(Types.REF, REF);
        types.put(Types.DATALINK, DATALINK);
        types.put(Types.BOOLEAN, BOOLEAN);
        types.put(Types.ROWID, ROWID);
        types.put(Types.NCHAR, NCHAR);
        types.put(Types.NVARCHAR, NVARCHAR);
        types.put(Types.LONGNVARCHAR, LONGNVARCHAR);
        types.put(Types.NCLOB, NCLOB);
        types.put(Types.SQLXML, SQLXML);
        types.put(Types.REF_CURSOR, REF_CURSOR);
        types.put(Types.TIME_WITH_TIMEZONE, TIME_WITH_TIMEZONE);
        types.put(Types.TIMESTAMP_WITH_TIMEZONE, TIMESTAMP_WITH_TIMEZONE);
    }

    @Override
    public Collection<String> getValues() {
        return types.values();
    }

    @Override
    public int getNumType(String name) {
        return 0;
    }

    @Override
    public String typeName(int type) {
        return types.get(type);
    }

    @Override
    public boolean isLargeObject(int type) {
        return false;
    }

    @Override
    public boolean isStringType(int type) {
        return false;
    }

    @Override
    public boolean isNumericType(int type) {
        return false;
    }
}