package by.post.control.ui;

import by.post.control.db.Recovery;
import by.post.control.db.RecoveryManager;
import by.post.ui.ConfirmationDialog;
import by.post.ui.InputDialog;
import by.post.ui.LoginDialog;
import by.post.ui.Resources;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Callback;
import javafx.util.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.file.*;
import java.util.Comparator;
import java.util.Optional;


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
    @FXML
    private Label progress;
    @FXML
    private ProgressBar progressBar;

    private static final Logger logger = LogManager.getLogger(RecoveryPaneController.class);

    public RecoveryPaneController() {

    }

    @FXML
    public void onRun() {

        //TODO The ability to put a warning about the increased consumption of RAM

        FileTreeItem baseItem = (FileTreeItem) databasePath.getSelectionModel().getSelectedItem();
        FileTreeItem saveItem = (FileTreeItem) savePath.getSelectionModel().getSelectedItem();

        Path file = baseItem != null ? baseItem.getPath() : null;
        Path save = saveItem != null ? saveItem.getPath() : null;

        if (file == null || !file.toFile().isFile()) {
            new Alert(Alert.AlertType.ERROR,"Please select database file!").showAndWait();
            logger.error("RecoveryPaneController error [onRun]: " +
                    "Not selected properly database file.");
            return;
        }

        if (save == null || !save.toFile().canWrite()) {
            new Alert(Alert.AlertType.ERROR,"Please set path for save!").showAndWait();
            logger.error("RecoveryPaneController error [onRun]: " +
                    "Not selected properly path for save recovered database file.");
            return;
        }

        Optional<Pair<String, String>> userData = null;

        try {
            userData = new LoginDialog().showAndWait();
        } catch (Exception e) {
            logger.error("RecoveryPaneController error [onRun]: " + e);
        }

        if (userData.isPresent()) {
            final String user = userData.get().getKey();
            final String password = userData.get().getValue();

            progress.setText("Please wait...");
            progress.setVisible(true);
            progressBar.setVisible(true);

            /**
             * Run recovery task
             * @see "http://docs.oracle.com/javafx/2/threads/jfxpub-threads.htm"
             */
            Task task = new Task<Void>() {
                @Override
                protected Void call() throws Exception {

                    Recovery recovery = new RecoveryManager();

                    recovery.recover(file, save, user, password, new Callback<Boolean, Boolean>() {
                        @Override
                        public Boolean call(Boolean param) {
                            Platform.runLater(new Runnable() {
                                @Override
                                public void run() {
                                    progress.setText(param.booleanValue() ? "Done!" : "Error...");
                                    progressBar.setVisible(false);
                                }
                            });
                            return param.booleanValue();
                        }
                    });
                    return null;
                }
            };
            //TODO maybe need adding termination of thread by button click for canceling.
            new Thread(task).start();
        }
    }

    /**
     * Context menu for save path view
     */
    @FXML
    public void onAddFolder() {

        Optional<String> folder = new InputDialog("Write folder name", "new", false).showAndWait();

        FileTreeItem item = (FileTreeItem) savePath.getSelectionModel().getSelectedItem();

        if (folder.isPresent()) {
            if (item.getPath().toFile().isDirectory()) {
                item.setExpanded(true);
                getNewDir(folder.get(), item);
            } else {
                item = (FileTreeItem) item.getParent();
                if (item != null && item.getPath() != null) {
                    getNewDir(folder.get(), item);
                } else {
                    new Alert(Alert.AlertType.ERROR,
                            "Create here a directory is impossible!", ButtonType.OK).showAndWait();
                }
            }
        }

    }

    @FXML
    public void onDeleteFolder() {

        Optional<ButtonType> result = new ConfirmationDialog().showAndWait();

        if (result.get() == ButtonType.OK) {
            deleteDir((FileTreeItem) savePath.getSelectionModel().getSelectedItem());
        }
    }

    @FXML
    private void initialize() {

        Iterable<Path> rootDirectories = FileSystems.getDefault().getRootDirectories();
        FileTreeItem databaseItem = new FileTreeItem(Resources.HOST_NAME);
        FileTreeItem saveItem = new FileTreeItem(Resources.HOST_NAME);

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
                                item.setExpanded(true);
                            }
                        });
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

    /**
     * Create new directory in file system and add new item to tree view
     */
    private void getNewDir(String dir, FileTreeItem item) {

        String path = item.getPath().toFile().getAbsolutePath() + File.separator + dir;

        try {
            Files.createDirectory(Paths.get(path));
            FileTreeItem newItem = new FileTreeItem(path);
            newItem.setGraphic(getFolderImage());
            item.getChildren().add(newItem);
        } catch (IOException e) {
            logger.error("RecoveryPaneController error [getNewDir]: " + e);
        }
    }

    /**
     * Delete directory in file system and item from tree view
     *
     * @param item
     */
    private void deleteDir(FileTreeItem item) {

        Path path = item.getPath();

        try {
            if (!Files.isDirectory(path)) {
                Files.deleteIfExists(path);
            } else {
                if (Files.list(path).findFirst().isPresent()) {

                    Optional<ButtonType> result = new ConfirmationDialog("Directory is not empty!").showAndWait();

                    if (result.get() == ButtonType.OK) {
                        // Deleting directories recursively
                        Files.walk(path, FileVisitOption.FOLLOW_LINKS)
                                .sorted(Comparator.reverseOrder())
                                .map(Path::toFile).forEach(File::delete);
                    }
                } else {
                    Files.deleteIfExists(path);
                }
            }

            item.getParent().getChildren().remove(item);
        } catch (IOException e) {
            logger.error("RecoveryPaneController error [deleteDir]: " + e);
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
            super();
            this.path = Paths.get(path);
            this.setValue(this.path.getFileName());
        }

        public Path getPath() {
            return path;
        }
    }

}


