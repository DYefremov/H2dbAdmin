package by.post.control;

import by.post.control.ui.TypedTreeItem;
import javafx.collections.ObservableList;
import javafx.scene.control.TableView;
import javafx.scene.control.TreeView;

import java.util.Locale;

/**
 * Class for storing the application context
 *
 * @author Dmitriy V.Yefremov
 */
public class Context {

    private static TypedTreeItem tablesTreeItem;
    private static TableView mainTableView;
    private static TreeView mainTableTree;
    private static ObservableList<ObservableList> currentData;
    private static Locale locale;

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

    public static synchronized ObservableList<ObservableList> getCurrentData() {
        return currentData;
    }

    public static synchronized void setCurrentData(ObservableList<ObservableList> currentData) {
        Context.currentData = currentData;
    }

    public static Locale getLocale() {
        return locale;
    }

    public static synchronized void setLocale(Locale locale) {
        Context.locale = locale;
    }

    private Context() {

    }
}
