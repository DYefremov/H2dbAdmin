package by.post.control;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.util.Properties;

/**
 * @author Dmitriy V.Yefremov
 */
public class PropertiesControllerTest {
    @Ignore
    @Before
    public void setProperties() throws Exception {
        PropertiesController.setProperties("~/", "sa", "");
    }

    @Ignore
    @Test
    public void getProperties() throws Exception {
        Properties properties = PropertiesController.getProperties();
        Assert.assertEquals(properties.getProperty("url"), "test");
   }

}