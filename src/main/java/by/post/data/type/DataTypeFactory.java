package by.post.data.type;

/**
 * @author Dmitriy V.Yefremov
 */
public class DataTypeFactory {

    private static final DataTypeFactory INSTANCE = new DataTypeFactory();

    public static DataTypeFactory getInstance() {
        return INSTANCE;
    }

    private DataTypeFactory() {

    }

    public ColumnDataType getColumnDataType(Dbms dbms) {

        if (dbms.equals(Dbms.H2)) {
            return new H2Type();
        }

        return new DefaultType();
    }
}
