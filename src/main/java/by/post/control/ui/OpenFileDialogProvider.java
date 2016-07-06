package by.post.control.ui;

import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;

/**
 * Create dialog for db files opening.
 *
 * @author Dmitriy V.Yefremov
 */
public class OpenFileDialogProvider {

    private static final Logger logger = LogManager.getLogger(OpenFileDialogProvider.class);


    public OpenFileDialogProvider() {

    }

    public File getFileDialog(String title, boolean dir) {

        File file = getFileChooser("Select db file...").showOpenDialog(getFileOpenStage());

        if (file != null) {
            String path = file.getPath();
            logger.info(title + path);
        }

        return file;
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
