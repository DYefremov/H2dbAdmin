package by.post.control.ui.dialogs;

import by.post.control.recovery.Recovery;
import by.post.control.recovery.RecoveryManager;
import by.post.ui.ConfirmationDialog;
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
    private Recovery recovery;

    private static final Logger logger = LogManager.getLogger(RecoveryPaneController.class);

    public RecoveryPaneController() {

    }

    /**
     * Action for Run button
     */
    @FXML
    public void onRun() {

        if (recoveryService.isRunning() || recovery.isRunning()) {
            new Alert(Alert.AlertType.ERROR, "Task is already running!").showAndWait();
            return;
        }

        if (new ConfirmationDialog("This operation may require large resources!").showAndWait().get() != ButtonType.OK) {
            return;
        }

        setProgressVisible(false);

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

        recovery = new RecoveryManager();
        recoveryService = new RecoveryService();

    }

    public void onCancel() {

        if (recoveryService.isRunning()) {
            recovery.cancel();
            recoveryService.cancel();
        }
    }

    /**
     * @param visible
     */
    private void setProgressVisible(boolean visible) {

        Platform.runLater(() -> {
            progress.setText(visible ? "Please wait..." : "");
            progress.setVisible(visible);
            progressBar.setVisible(visible);
        });
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

                    setProgressVisible(true);
                    Platform.runLater(() -> dialogPane.setExpanded(true));

                    final Path file = new File(dbPath.getText()).toPath();
                    final Path save = new File(savePath.getText()).toPath();
                    final String user = userField.getText();
                    final String password = passwordField.getText();

                    recovery.recover(file, save, user, password);

                    while (recovery.isRunning()) {
                        /**NOP*/
                    }

                    return false;
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