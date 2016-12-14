package by.post.control;

import by.post.control.ui.TypedTreeItem;
import javafx.scene.control.ListView;
import javafx.scene.control.TableView;
import javafx.scene.control.TreeView;

import java.util.List;

/**
 * Class for storing the application context
 *
 * @author Dmitriy V.Yefremov
 */
public class Context {

    private static TypedTreeItem tablesTreeItem;
    private static TableView mainTableView;
    private static TreeView mainTableTree;

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

    private Context() {

    }
}
