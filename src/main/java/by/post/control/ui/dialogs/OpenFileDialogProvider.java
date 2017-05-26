package by.post.control.ui.dialogs;

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

    /**
     * @param title
     * @param dir
     * @return
     */
    public File getFileDialog(String title, boolean dir, boolean isFiltered) {

        if (dir) {
            return getDirectoryChooser(title).showDialog(getFileOpenStage());
        }

        return getFileChooser(title, isFiltered).showOpenDialog(getFileOpenStage());
    }

    /**
     * @param title
     * @return
     */
    public File getSaveFileDialog(String title) {
        return getFileChooser(title, false).showSaveDialog(getFileOpenStage());
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
    private FileChooser getFileChooser(String title, boolean filtered) {

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle(title);

        if (filtered) {
            //Add filter for open only ".db" files
            fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("DB","*.db"));
        }

        return fileChooser;
    }

    /**
     * @param title
     * @return
     */
    private DirectoryChooser getDirectoryChooser(String title) {

        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle(title);

        return directoryChooser;
    }
}
