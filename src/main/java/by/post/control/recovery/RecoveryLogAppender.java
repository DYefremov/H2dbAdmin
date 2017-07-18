package by.post.control.recovery;

import javafx.application.Platform;
import javafx.scene.control.TextArea;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.appender.AbstractAppender;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginElement;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.layout.PatternLayout;

import java.io.Serializable;

/**
 * @author Dmitriy V.Yefremov
 */
@Plugin(name="RecoveryLogAppender", category = "Core", elementType = "appender", printObject = true)
public class RecoveryLogAppender extends AbstractAppender {

    private static TextArea textArea;

    protected RecoveryLogAppender(String name, Filter filter, Layout<? extends Serializable> layout) {
        super(name, filter, layout);
    }

    public static void setTextArea(TextArea textArea) {
        RecoveryLogAppender.textArea = textArea;
    }

    @Override
    public void append(LogEvent event) {

        if (textArea == null) {
            return;
        }
        Platform.runLater(() -> textArea.appendText(event.getMessage().getFormattedMessage() + "\n"));
    }

    @PluginFactory
    public static RecoveryLogAppender create(@PluginAttribute("name") String name, @PluginElement("Filter") Filter filter,
                                             @PluginElement("Layout") Layout<? extends Serializable> layout){

        if (name == null) {
            return null;
        }

        if (layout == null) {
            layout = PatternLayout.createDefaultLayout();
        }

        return new RecoveryLogAppender(name, filter, layout);
    }
}
