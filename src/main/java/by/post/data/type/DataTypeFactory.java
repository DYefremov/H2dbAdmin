package by.post.data.type;

/**
 * @author Dmitriy V.Yefremov
 */
public class DataTypeFactory {

    public DataTypeFactory() {

    }

    public ColumnDataType getColumnDataType(Dbms dbms) {

        if (dbms.equals(Dbms.H2)) {
            return new H2Type();
        }

        return new DefaultType();
    }
}
