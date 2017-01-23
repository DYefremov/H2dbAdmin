package by.post.data.type;

/**
 * @author Dmitriy V.Yefremov
 */
public class DataTypeFactory {

    public ColumnDataType getColumnDataType(Dbms dbms) {
        return dbms.equals(Dbms.H2) ? new H2Type() : new DefaultColumnDataType();
    }
}
