package by.post.data.type;

import org.h2.value.DataType;

import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

/**
 * @author Dmitriy V.Yefremov
 */
public class H2Type  implements ColumnDataType {

    public static final String INTEGER = "INTEGER";
    public static final String BOOLEAN = "BOOLEAN";
    public static final String TINYINT = "TINYINT";
    public static final String SMALLINT = "SMALLINT";
    public static final String BIGINT = "BIGINT";
    public static final String IDENTITY = "IDENTITY";
    public static final String DECIMAL = "DECIMAL";
    public static final String DOUBLE = "DOUBLE";
    public static final String REAL = "REAL";
    public static final String TIME = "TIME";
    public static final String DATE = "DATE";
    public static final String TIMESTAMP = "TIMESTAMP";
    public static final String TIMESTAMP_WITH_TIMEZONE = "TIMESTAMP WITH TIMEZONE";
    public static final String BINARY = "BINARY";
    public static final String OTHER = "OTHER";
    public static final String VARCHAR = "VARCHAR";
    public static final String VARCHAR_IGNORECASE = "VARCHAR_IGNORECASE";
    public static final String CHAR = "CHAR";
    public static final String BLOB = "BLOB";
    public static final String CLOB = "CLOB";
    public static final String UUID = "UUID";
    public static final String ARRAY = "ARRAY";
    public static final String GEOMETRY = "GEOMETRY";

    @Override
    public Collection<String> getValues() {
        return Arrays.asList(this.getClass().getFields()).stream().map(f -> {
            Object value = null;
            try {
                value = f.get(this);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            return String.valueOf(value);
        }).collect(Collectors.toList());
    }

    @Override
    public int getNumType(String name) {
        return DataType.getTypeByName(name).type;
    }

    @Override
    public String typeName(int type) {
        return DataType.getDataType(type).name;
    }

    @Override
    public boolean isLargeObject(int type) {
        return DataType.isLargeObject(type);
    }

    @Override
    public boolean isStringType(int type) {
        return  DataType.isStringType(type);
    }

    @Override
    public boolean isNumericType(int type) {
        return DataType.supportsAdd(type);
    }
}
