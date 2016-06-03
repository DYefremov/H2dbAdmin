package by.post.ui;

import javafx.application.Platform;
import javafx.scene.control.TextArea;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.appender.AbstractAppender;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginBuilderFactory;
import org.apache.logging.log4j.core.config.plugins.PluginElement;
import org.apache.logging.log4j.core.layout.PatternLayout;

import java.io.Serializable;

/**
 * @author Dmitriy V.Yefremov
 */
@Plugin(name="LogArea", category = "Core", elementType = "appender", printObject = true)
public class LogArea extends AbstractAppender {

    private static TextArea area;

    protected LogArea(String name, Filter filter, Layout<? extends Serializable> layout) {
        super(name, filter, layout);
    }

    public static void setArea(TextArea area) {
        LogArea.area = area;
    }

    @Override
    public void append(LogEvent event) {

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                area.appendText(String.valueOf(getLayout().toByteArray(event)));
            }
        });

    }

    @PluginBuilderFactory
    public static LogArea create(@PluginAttribute("name") String name,
                                 @PluginElement("Filter") Filter filter,
                                 @PluginElement("Layout") Layout<? extends Serializable> layout){

        if (name == null) {
            return null;
        }

        if (layout == null) {
            layout = PatternLayout.createDefaultLayout();
        }

        return new LogArea(name, filter, layout);
    }

}


