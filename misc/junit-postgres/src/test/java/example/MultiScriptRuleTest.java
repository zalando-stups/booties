package example;

import static org.zalando.stups.junit.postgres.MavenProjectLayout.projectBaseDir;

import java.util.concurrent.TimeUnit;

import org.junit.ClassRule;
import org.junit.Test;
import org.zalando.stups.junit.postgres.PostgreSqlRule;

public class MultiScriptRuleTest {

    @ClassRule
    public static final PostgreSqlRule postgres = new PostgreSqlRule.Builder()
            .addScriptLocation(projectBaseDir(BlockScriptRuleTest.class) + "/multiscripts").build();

    @Test
    public void runTest() throws InterruptedException {
        TimeUnit.SECONDS.sleep(3);
    }
}
