package by.post.control.ui;

import by.post.control.db.Recovery;
import by.post.control.db.RecoveryManager;
import by.post.ui.RecoveryDialog;
import by.post.ui.SimpleProgressIndicator;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.util.Callback;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;

/**
 * @deprecated will used RecoveryPane
 *
 * @author Dmitriy V.Yefremov
 */
public class RecoveryDialogController {

    @FXML
    private TextField inFileTextField;
    @FXML
    private TextField outFileTextField;

    private File dbFile;
    private File saveDir;

    private Recovery recovery;

    private RecoveryDialog recoveryDialog;

    private static final Logger logger = LogManager.getLogger(MainUiController.class);

    public RecoveryDialogController() {

    }

    public void setRecoveryDialog(RecoveryDialog recoveryDialog) {
        this.recoveryDialog = recoveryDialog;
    }

    @FXML
    private void initialize() {
        recovery = new RecoveryManager();
    }

    @FXML
    public void onRunClick(ActionEvent event) {

        Platform.runLater(new Runnable() {
            @Override
            public void run() {

                SimpleProgressIndicator pi = new SimpleProgressIndicator();
                pi.show();

                boolean done = recovery.recover(dbFile, saveDir, new Callback<Boolean, Boolean>() {
                    @Override
                    public Boolean call(Boolean param) {
                        if (param) {
                            inFileTextField.setText("Recovery done!");
                            logger.info("Recovery done!");
                        }
                        pi.close();
                        return param;
                    }
                });

            }
        });

    }

    /**
     * Close window
     *
     * @param event
     * @throws Exception
     */
    @FXML
    public void onCancelClick(ActionEvent event) throws Exception {
        recoveryDialog.close();
    }

    @FXML
    public void onInTextFieldClick(MouseEvent event) {

        if (event.getClickCount() == 2) {
            dbFile = new OpenFileDialogProvider().getFileDialog("Select db file...", false);
        }
    }

    @FXML
    public void onOutTextFieldClick(MouseEvent event) {

        if (event.getClickCount() == 2) {
            saveDir = new OpenFileDialogProvider().getFileDialog("Select folder for save...", true);
        }
    }

}
