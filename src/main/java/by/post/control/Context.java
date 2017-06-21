package by.post.control;

import by.post.control.ui.TypedTreeItem;
import by.post.data.type.ColumnDataType;
import by.post.data.type.DataTypeFactory;
import by.post.data.type.Dbms;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.control.TableView;
import javafx.scene.control.TreeView;

import java.util.Locale;

/**
 * Class for storing and init the application context
 *
 * @author Dmitriy V.Yefremov
 */
public class Context {

    private static TypedTreeItem tablesTreeItem;
    private static TableView mainTableView;
    private static TreeView mainTableTree;
    private static Locale locale;
    //Data types for database columns
    private static ColumnDataType currentDataType;
    private static Dbms CURRENT_DBMS = Dbms.DEFAULT;
    //Indicate if running load data in table process
    private static boolean isLoadData;
    //For bind with {isLoadData} (in status bar)
    private static SimpleBooleanProperty isLoadDataProperty = new SimpleBooleanProperty(isLoadData());

    public static TypedTreeItem getTablesTreeItem() {
        return tablesTreeItem;
    }

    public static synchronized void setTablesTreeItem(TypedTreeItem tablesTreeItem) {
        Context.tablesTreeItem = tablesTreeItem;
    }

    public static TableView getMainTableView() {
        return mainTableView;
    }

    public static synchronized void setMainTableView(TableView mainTableView) {
        Context.mainTableView = mainTableView;
    }

    public static TreeView getMainTableTree() {
        return mainTableTree;
    }

    public static synchronized void setMainTableTree(TreeView mainTableTree) {
        Context.mainTableTree = mainTableTree;
    }

    public static Locale getLocale() {
        return locale == null ? new Locale(Settings.DEFAULT_LANG) : locale;
    }

    public static synchronized void setLocale(Locale locale) {
        Context.locale = locale;
    }

    public static Dbms getCurrentDbms() {
        return CURRENT_DBMS;
    }

    public static synchronized void setCurrentDbms(Dbms currentDbms) {
        CURRENT_DBMS = currentDbms;
        setCurrentDataType(new DataTypeFactory().getColumnDataType(currentDbms));
    }

    public static synchronized void setCurrentDataType(ColumnDataType currentDataType) {
        Context.currentDataType = currentDataType;
    }

    public static ColumnDataType getCurrentDataType() {

        if (currentDataType == null) {
            currentDataType = new DataTypeFactory().getColumnDataType(getCurrentDbms());
        }

        return currentDataType;
    }

    public static boolean isLoadData() {
        return isLoadData;
    }

    public static  synchronized void setLoadData(boolean loadData) {
        isLoadData = loadData;
        isLoadDataProperty.setValue(loadData);
    }

    public static SimpleBooleanProperty getIsLoadDataProperty() {
        return isLoadDataProperty;
    }

    private Context() {

    }

}
