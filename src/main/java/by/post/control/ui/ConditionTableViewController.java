package by.post.control.ui;

import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.layout.Pane;

/**
 * @author Dmitriy V.Yefremov
 */
public class ConditionTableViewController  {

    @FXML
    TableView tableView;
    @FXML
    TableColumn mainColumn;
    @FXML
    TableColumn selColumn;

    private Pane parent;

    public ConditionTableViewController() {

    }

    public void setParent(Pane parent) {
        this.parent = parent;
    }

    public void setMainColumnText(String text) {
        mainColumn.setText(text);
    }

    @FXML
    public void onContextMenuDelete() {
        parent.getChildren().remove(tableView);
    }

    @FXML
    private void initialize() {
        selColumn.setCellFactory(CheckBoxTableCell.forTableColumn(selColumn));
    }
}
