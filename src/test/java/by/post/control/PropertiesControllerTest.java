package by.post.control;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * @author Dmitriy V.Yefremov
 */
public class PropertiesControllerTest {

    @Ignore
    @Before
    public void setProperties() throws Exception {
        Map<String, String> settings = new HashMap<>();

        settings.put(Settings.USER, Settings.DEFAULT_USER);
        settings.put(Settings.PASSWORD, Settings.DEFAULT_PASSWORD);
        settings.put(Settings.URL, Settings.DEFAULT_URL);

        PropertiesController.setProperties(settings);
    }

    @Ignore
    @Test
    public void getProperties() throws Exception {

        Properties properties = PropertiesController.getProperties();

        Assert.assertEquals(properties.getProperty(Settings.USER), Settings.DEFAULT_USER);
        Assert.assertEquals(properties.getProperty(Settings.PASSWORD), Settings.DEFAULT_PASSWORD);
        Assert.assertEquals(properties.getProperty(Settings.URL), Settings.DEFAULT_URL);
   }
}