import org.h2.tools.RunScript;
import org.junit.Ignore;
import org.junit.Test;

import java.sql.SQLException;

/**
 * @author Dmitriy V.Yefremov
 */
public class RunScriptTest {
    @Ignore
    @Test
    public void test() {
        String path = "~/test.h2.sql";
        String url = "jdbc:h2:~/recovered";
        String user = "sa";
        String password = "";
        try {
            RunScript.execute(url, user, password, path, null, true);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
