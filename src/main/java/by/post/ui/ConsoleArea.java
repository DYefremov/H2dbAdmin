package by.post.ui;

import javafx.scene.control.TextArea;

import java.io.IOException;
import java.io.OutputStream;

/**
 * @author Dmitriy V.Yefremov
 */
public class ConsoleArea extends OutputStream{

    private TextArea out;

    public ConsoleArea() {
    }

    public ConsoleArea(TextArea out) {

        this.out = out;
    }

    @Override
    public void write(int b) throws IOException {
        out.appendText(String.valueOf((char)b));
    }

    public TextArea getOut() {

        return out;
    }

    public void setOut(TextArea out) {

        this.out = out;
    }
}
