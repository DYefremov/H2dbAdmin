package by.post.control.ui;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.file.DirectoryStream;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * @author Dmitriy V.Yefremov
 */
public class RecoveryPaneController {

    @FXML
    private TreeView<Path> databasePath;
    @FXML
    private TreeView<Path> savePath;

    public RecoveryPaneController() {

    }

    @FXML
    public void onRun() {

    }

    @FXML
    private void initialize() {

        Iterable<Path> rootDirectories = FileSystems.getDefault().getRootDirectories();
        TreeItem<Path> rootNode = null;
        try {
            // Set name for root element by pc host name
            rootNode = new TreeItem(InetAddress.getLocalHost().getHostName());
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

        for (Path name : rootDirectories) {
            TreeItem treeNode = new TreeItem(name);
            getTree(treeNode);
            rootNode.getChildren().add(treeNode);
            rootNode.setExpanded(true);
        }

        databasePath.setRoot(rootNode);
//        savePath.setRoot(rootNode);
//        setListener(databasePath);
//        setListener(savePath);
    }

    /**
     * @param view
     */
    private void setListener(TreeView view) {

        view.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {

                TreeItem<Path> item = (TreeItem<Path>) newValue;

                if (Files.isDirectory(item.getValue())) {
                    getTree(item);
                    item.setExpanded(true);
                } else {

                }
            }
        });

    }

    /**
     * @param item
     * @throws IOException
     */
    private void getTree(TreeItem<Path> item) {

        try {
            if (Files.isReadable(item.getValue())) {
                DirectoryStream<Path> directoryStream = Files.newDirectoryStream(item.getValue());

                directoryStream.forEach(path -> {

                    TreeItem<Path> newItem = new TreeItem<>(path);
                    item.getChildren().add(newItem);

                    if (Files.isDirectory(path)) {
                        item.setGraphic(getFolderImage());
                        item.addEventHandler(TreeItem.branchExpandedEvent(),new EventHandler() {
                            @Override
                            public void handle(Event event) {
                                Platform.runLater(new Runnable() {
                                    @Override
                                    public void run() {
                                        TreeItem it = (TreeItem) event.getSource();
                                        if (it.isLeaf()) {
//                                            newItem.setGraphic(getFolderImage());
//                                            getTree(newItem);
                                        }

                                    }
                                });
                            }
                        });
                    }
                });

            }

        } catch (IOException e) {

        }


    }

    /**
     * @return view with folder image
     */
    private ImageView getFolderImage() {
        return new ImageView(new Image("/img/folder_test.png", 25, 25, false, false));
    }

}
