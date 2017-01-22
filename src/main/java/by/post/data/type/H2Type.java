package by.post.data.type;

import org.h2.value.DataType;

import java.util.Collection;
import java.util.stream.Collectors;

/**
 * @author Dmitriy V.Yefremov
 */
public class H2Type implements ColumnDataType {

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
}
