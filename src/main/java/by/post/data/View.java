package by.post.data;

import java.util.List;

/**
 * @author Dmitriy V.Yefremov
 */
public class View {

    private String name;
    private List<Table> tables;

    public View() {

    }

    public View(String name, List<Table> tables){
        this.name = name;
        this.tables = tables;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Table> getTables() {
        return tables;
    }

    public void setTables(List<Table> tables) {
        this.tables = tables;
    }

    @Override
    public String toString() {
        return "View{" +
                "name='" + name + '\'' +
                ", tables=" + tables +
                '}';
    }
}
