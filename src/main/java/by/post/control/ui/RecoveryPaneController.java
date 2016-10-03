package by.post.control.ui;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.file.*;

/**
 * @author Dmitriy V.Yefremov
 */
public class RecoveryPaneController {

    @FXML
    private TreeView<Path> databasePath;
    @FXML
    private TreeView<Path> savePath;
    @FXML
    private CheckBox filter;

    private static final Logger logger = LogManager.getLogger(RecoveryPaneController.class);

    public RecoveryPaneController() {

    }

    @FXML
    public void onRun() {

        String file = null;
        String save = null;

        FileTreeItem baseItem = (FileTreeItem) databasePath.getSelectionModel().getSelectedItem();
        FileTreeItem saveItem = (FileTreeItem) savePath.getSelectionModel().getSelectedItem();

        if (baseItem != null) {
            file = String.valueOf(baseItem.getPath());
        }

        if (saveItem != null) {
           save = String.valueOf(saveItem.getPath());
        }

        logger.info("Selected: database file = " + file + ",  path for save = " + save);
    }

    @FXML
    private void initialize() {

        String hostName = null;

        try {
            hostName =  InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException e) {
            logger.error("RecoveryPaneController error [initialize]: " + e);
        }

        hostName = hostName == null ? "PC" : hostName;

        Iterable<Path> rootDirectories = FileSystems.getDefault().getRootDirectories();
        FileTreeItem databaseItem = new FileTreeItem(hostName);
        FileTreeItem saveItem = new FileTreeItem(hostName);

        rootDirectories.forEach(name -> {
            getRootNode(databaseItem, name);
            getRootNode(saveItem, name);
        });

        databasePath.setRoot(databaseItem);
        savePath.setRoot(saveItem);
        setListener(databasePath);
        setListener(savePath);
    }

    /**
     * @param item
     * @param name
     */
    private void getRootNode(FileTreeItem item, Path name) {

        FileTreeItem newItem = new FileTreeItem(name);
        newItem.setGraphic(getFolderImage());
        item.getChildren().add(newItem);
        item.setExpanded(true);
    }

    /**
     * @param view
     */
    private void setListener(TreeView view) {

        view.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                // Skip for root element
                if (newValue.equals(view.getRoot())) {
                    return;
                }

                FileTreeItem item = (FileTreeItem) newValue;
                Path path = item.getPath();

                if (Files.isDirectory(path)) {
                    if (item.isLeaf()) {
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                getTree(item);
                            }
                        });
                        item.setExpanded(true);
                    }
                }
            }
        });
    }

    /**
     * @param item
     * @throws IOException
     */
    private void getTree(FileTreeItem item) {

        try {
            if (Files.isReadable(item.getPath())) {
                DirectoryStream<Path> directoryStream = Files.newDirectoryStream(item.getPath());

                directoryStream.forEach(path -> {

                    FileTreeItem newItem = new FileTreeItem(path);

                    if (Files.isDirectory(path)) {
                        newItem.setGraphic(getFolderImage());
                        item.getChildren().add(newItem);
                    } else {
                        boolean isDbFile = path.toFile().getName().toUpperCase().endsWith(".DB");

                        if (isDbFile || !filter.isSelected()) {
                            item.getChildren().add(newItem);
                        }
                    }
                });
            }
        } catch (IOException e) {
            logger.error("RecoveryPaneController error [getTree]: " + e);
        }
    }

    /**
     * @return view with folder image
     */
    private ImageView getFolderImage() {
        return new ImageView(new Image("/img/folder_test.png", 16, 16, false, false));
    }

}

/**
 * Custom implementation of tree item for better work with file names
 */
class FileTreeItem extends TreeItem {

    private Path path;

    public FileTreeItem(Path path) {
        super(String.valueOf(path.getFileName() == null ? path : path.getFileName()));
        this.path = path;
    }

    public FileTreeItem(String path) {
        super(path);
        this.path = Paths.get(path);
    }

    public Path getPath() {
        return path;
    }
}
