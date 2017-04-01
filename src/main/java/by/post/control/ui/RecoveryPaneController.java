package by.post.control.ui;

import by.post.control.db.Recovery;
import by.post.control.db.RecoveryManager;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.nio.file.Path;

/**
 * @author Dmitriy V.Yefremov
 */
public class RecoveryPaneController {

    @FXML
    private TextField dbPath;
    @FXML
    private TextField savePath;
    @FXML
    private TextField userField;
    @FXML
    private TextField passwordField;
    @FXML
    private Label progress;
    @FXML
    private ProgressBar progressBar;
    @FXML
    private CheckBox isFiltered;

    private static final Logger logger = LogManager.getLogger(RecoveryPaneController.class);

    public RecoveryPaneController() {

    }

    /**
     * Action for Run button
     */
    @FXML
    public void onRun() {

        setProgressVisible(false);

        //TODO The ability to put a warning about the increased consumption of RAM

        Path file = new File(dbPath.getText()).toPath();
        Path save = new File(savePath.getText()).toPath();

        if (file == null || !file.toFile().isFile()) {
            showError("Error! Path not set!", dbPath);
            logger.error("RecoveryPaneController error [onRun]: " +
                    "Not selected properly database file.");
            return;
        }

        if (save == null || !save.toFile().canWrite()) {
            showError("Error! Path not set!", savePath);
            logger.error("RecoveryPaneController error [onRun]: " +
                    "Not selected properly path for save recovered database file.");
            return;
        }

        final String user = userField.getText();
        final String password = passwordField.getText();

        setProgressVisible(true);

        Task<Boolean> task = new Task<Boolean>() {
            @Override
            protected Boolean call() throws Exception {
                return recovery(file, save, user, password);
            }
        };

        task.setOnSucceeded(event -> setProgressVisible(false));
        task.setOnFailed(event -> setProgressVisible(false));
        task.setOnCancelled(event -> setProgressVisible(false));

        Thread thread = new Thread(task);
        thread.setDaemon(true);
        thread.start();
    }

    @FXML
    public void onDbPath() {
        getDbPath();
    }

    @FXML
    public void onSavePath() {
        getSavePath();
    }

    @FXML
    public void initialize() {

    }

    /**
     * @param dbFile
     * @param savePath
     * @param user
     * @param password
     */
    private boolean recovery(Path dbFile, Path savePath, String user, String password) {

        Recovery recovery = new RecoveryManager();
        recovery.recover(dbFile, savePath, user, password, param -> {
            Platform.runLater(() -> {
                progress.setText(param.booleanValue() ? "Done!" : "Error...");
                progressBar.setVisible(false);
            });
            return param.booleanValue();
        });

        return false;
    }

    /**
     * @param visible
     */
    private void setProgressVisible(boolean visible) {

        progress.setText(visible ? "Please wait..." : "");
        progress.setVisible(visible);
        progressBar.setVisible(visible);
    }

    /**
     * @param message
     * @param field
     */
    private void showError(String message, TextField field) {
        field.setText(message);
        field.setStyle("-fx-border-color: red;");
//        field.setStyle("-fx-text-fill: red; -fx-border-color: red;");
    }

    /**
     *
     */
    private void getDbPath() {

        File file = new OpenFileDialogProvider().getFileDialog("", false, isFiltered.isSelected());

        if (file != null) {
            dbPath.setStyle(null);
            dbPath.setText(file.getAbsolutePath());

        }
    }

    /**
     *
     */
    private void getSavePath() {

        File file = new OpenFileDialogProvider().getFileDialog("", true, false);

        if (file != null) {
            savePath.setStyle(null);
            savePath.setText(file.getAbsolutePath());
        }
    }

}
