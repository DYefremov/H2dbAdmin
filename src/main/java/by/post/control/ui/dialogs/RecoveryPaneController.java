package by.post.control.ui.dialogs;

import by.post.control.db.Recovery;
import by.post.control.db.RecoveryManager;
import javafx.application.Platform;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
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
    @FXML
    private DialogPane dialogPane;
    @FXML
    private ButtonType startButton;
    @FXML
    private TextArea consoleTextArea;
    private RecoveryService recoveryService;

    private static final Logger logger = LogManager.getLogger(RecoveryPaneController.class);

    public RecoveryPaneController() {

    }

    /**
     * Action for Run button
     */
    @FXML
    public void onRun() {

        setProgressVisible(false);

        if (true) {
            new Alert(Alert.AlertType.INFORMATION, "Service temporary disabled!").showAndWait();
            return;
        }

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

        Platform.runLater(() -> {
            dialogPane.setExpanded(true);
            setProgressVisible(true);
        });

        recoveryService.restart();
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
    private void initialize() {

        dialogPane.lookupButton(startButton).addEventFilter(ActionEvent.ACTION, event -> {
            event.consume();
            onRun();
        });

        //Redirecting System.out to a TextArea
        PrintStream ps = new PrintStream(new ConsoleAppender(), true);
        System.setOut(ps);
        System.setErr(ps);

        recoveryService = new RecoveryService();
    }

    public void onCancel() {

        if (recoveryService.isRunning()) {
            recoveryService.cancel();
        }
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

    private class RecoveryService extends Service<Boolean> {

        private RecoveryService() {
            setOnSucceeded(event -> setProgressVisible(false));
            setOnFailed(event -> setProgressVisible(false));
            setOnCancelled(event -> setProgressVisible(false));
        }

        @Override
        protected Task<Boolean> createTask() {
            Task<Boolean> task = new Task<Boolean>() {

                @Override
                protected Boolean call() throws Exception {

                    final Path file = new File(dbPath.getText()).toPath();
                    final Path save = new File(savePath.getText()).toPath();
                    final String user = userField.getText();
                    final String password = passwordField.getText();

                    return recovery(file, save, user, password);
                }
            };
            return task;
        }
    }

    /**
     * Class for redirecting System.out
     */
    private class ConsoleAppender extends OutputStream {

        @Override
        public void write(int b) throws IOException {
            Platform.runLater(() -> consoleTextArea.appendText(String.valueOf((char) b)));
        }
    }

}