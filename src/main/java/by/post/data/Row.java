package by.post.data;

import java.util.List;

/**
 * @author Dmitriy V.Yefremov
 */
public class Row implements Data {
    private int num;
    private List<Cell> cells;

    public Row() {
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public List<Cell> getCells() {
        return cells;
    }

    public void setCells(List<Cell> cells) {
        this.cells = cells;
    }

    @Override
    public String toString() {
        return "Row{" +
                "num=" + num +
                ", cells=" + cells +
                '}';
    }
}
