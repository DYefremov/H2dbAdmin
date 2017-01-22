package by.post.data.type;

import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;

/**
 * @author Dmitriy V.Yefremov
 */
public class DefaultType implements ColumnDataType {
    @Override
    public Collection<String> getValues() {
        return new ArrayList<>();
    }

    @Override
    public int getNumType(String name) {
        return 0;
    }

    @Override
    public String typeName(int type) {
        return "";
    }

    @Override
    public int getValueTypeFromResultSet(ResultSetMetaData meta, int columnIndex) throws SQLException {
        return 0;
    }

    @Override
    public boolean isLargeObject(int type) {
        return false;
    }
}
