package by.post.control;

import by.post.control.ui.TypedTreeItem;
import by.post.data.type.ColumnDataType;
import by.post.data.type.DataTypeFactory;
import by.post.data.type.Dbms;
import javafx.collections.ObservableList;
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
    private static ObservableList<ObservableList> currentData;
    private static Locale locale;
    //Data types for database columns
    private static ColumnDataType currentDataType;
    private static Dbms CURRENT_DBMS = Dbms.DEFAULT;

    public static TypedTreeItem getTablesTreeItem() {
        return tablesTreeItem;
    }

    public static void setTablesTreeItem(TypedTreeItem tablesTreeItem) {
        Context.tablesTreeItem = tablesTreeItem;
    }

    public static TableView getMainTableView() {
        return mainTableView;
    }

    public static void setMainTableView(TableView mainTableView) {
        Context.mainTableView = mainTableView;
    }

    public static TreeView getMainTableTree() {
        return mainTableTree;
    }

    public static void setMainTableTree(TreeView mainTableTree) {
        Context.mainTableTree = mainTableTree;
    }

    public static ObservableList<ObservableList> getCurrentData() {
        return currentData;
    }

    public static void setCurrentData(ObservableList<ObservableList> currentData) {
        Context.currentData = currentData;
    }

    public static Locale getLocale() {
        return locale == null ? new Locale(Settings.DEFAULT_LANG) : locale;
    }

    public static void setLocale(Locale locale) {
        Context.locale = locale;
    }

    public static Dbms getCurrentDbms() {
        return CURRENT_DBMS;
    }

    public static void setCurrentDbms(Dbms currentDbms) {
        CURRENT_DBMS = currentDbms;
    }

    public static void setCurrentDataType(ColumnDataType currentDataType) {
        Context.currentDataType = currentDataType;
    }

    public static ColumnDataType getCurrentDataType() {

        if (currentDataType == null) {
            currentDataType = DataTypeFactory.getInstance().getColumnDataType(CURRENT_DBMS);
        }

        return currentDataType;
    }

    private Context() {

    }

}
