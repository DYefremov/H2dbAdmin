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
    @FXML
    private CheckBox filter;

    private static final Logger logger = LogManager.getLogger(RecoveryPaneController.class);

    public RecoveryPaneController() {

    }

    @FXML
    public void onRun() {

        String file = null;
        String save = null;

        TreeItem baseItem = databasePath.getSelectionModel().getSelectedItem();
        TreeItem saveItem = savePath.getSelectionModel().getSelectedItem();

        if (baseItem != null) {
            file = String.valueOf(baseItem.getValue());
        }

        if (saveItem != null) {
           save = String.valueOf(saveItem.getValue());
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
        TreeItem<Path> databaseItem = new TreeItem(hostName);
        TreeItem<Path> saveItem = new TreeItem(hostName);

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
    private void getRootNode(TreeItem<Path> item, Path name) {

        TreeItem newItem = new TreeItem(name);
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

                TreeItem<Path> item = (TreeItem<Path>) newValue;
                Path path = (Path) item.getValue();

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
                } else {
                    System.out.println(path.toFile().getAbsolutePath());
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

                    if (Files.isDirectory(path)) {
                        newItem.setGraphic(getFolderImage());
                        item.getChildren().add(newItem);
                        System.out.println(path);
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
