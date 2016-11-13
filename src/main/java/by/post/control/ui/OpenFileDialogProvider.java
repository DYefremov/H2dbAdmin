package by.post.control.ui;

import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;

/**
 * Create dialog for db files opening.
 *
 * @author Dmitriy V.Yefremov
 */
public class OpenFileDialogProvider {

    public OpenFileDialogProvider() {

    }

    public File getFileDialog(String title, boolean dir) {

        if (dir) {
            return getDirectoryChooser(title).showDialog(getFileOpenStage());
        }

        return getFileChooser(title).showOpenDialog(getFileOpenStage());
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

    private DirectoryChooser getDirectoryChooser(String title) {

        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle(title);

        return directoryChooser;
    }
}
