package by.post.control;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author Dmitriy V.Yefremov
 */
public class PropertiesControllerTest {

    @Before
    public void setProperties() throws Exception {
        PropertiesController.setProperties("~/", "test", "sa", "");
    }

    @Ignore
    @Test
    public void getProperties() throws Exception {
        Assert.assertEquals(PropertiesController.getProperties().getProperty("db"), "test");
    }

}