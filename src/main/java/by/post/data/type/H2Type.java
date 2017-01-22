package by.post.data.type;

import org.h2.value.DataType;

import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Collection;
import java.util.stream.Collectors;

/**
 * @author Dmitriy V.Yefremov
 */
public class H2Type  implements ColumnDataType {

    public H2Type() {

    }

    @Override
    public Collection<String> getValues() {
        return DataType.getTypes().stream().map(t -> t.name).collect(Collectors.toList());
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
    public int getValueTypeFromResultSet(ResultSetMetaData meta, int columnIndex) throws SQLException {
        return DataType.getValueTypeFromResultSet(meta, columnIndex);
    }

    @Override
    public boolean isLargeObject(int type) {
        return DataType.isLargeObject(type);
    }
}
