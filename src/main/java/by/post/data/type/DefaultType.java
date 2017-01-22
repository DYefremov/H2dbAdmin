package by.post.data.type;

import java.util.Collection;

/**
 * @author Dmitriy V.Yefremov
 */
public class DefaultType implements ColumnDataType {

    @Override
    public Collection<String> getValues() {
        return DefaultColumnDataType.getTypes().values();
    }

    @Override
    public int getNumType(String name) {
        return DefaultColumnDataType.getNumType(name);
    }

    @Override
    public String typeName(int type) {
        return null;
    }
}
