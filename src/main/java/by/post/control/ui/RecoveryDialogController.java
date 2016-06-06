package by.post.control.ui;

import by.post.control.db.Recovery;
import by.post.control.db.RecoveryManager;
import by.post.ui.RecoveryDialog;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;

/**
 * @author Dmitriy V.Yefremov
 */
public class RecoveryDialogController {

    @FXML
    private TextField inFileTextField;
    @FXML
    private TextField outFileTextField;

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
        recovery.recover("", "");
    }

    /**
     * Close window
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
            File inFile = getFileChooser("Select db file...").showOpenDialog(getFileOpenStage());
            if (inFile != null) {
                String path = inFile.getPath();
                logger.info("Selected db file : " + path);
            }
        }
    }

    @FXML
    public  void onOutTextFieldClick(MouseEvent event) {
        if (event.getClickCount() == 2) {

        }
    }

    /**
     * @return stage for open file dialog
     */
    private Stage getFileOpenStage() {
        Stage stage = new Stage();
        stage.centerOnScreen();
        return stage;
    }

    /**
     * @param title
     * @return file chooser with filtered selection
     */
    private FileChooser getFileChooser(String title) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle(title);
        //Add filter for open only ".db" files
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("DB","*.db"));
        return fileChooser;
    }
}
