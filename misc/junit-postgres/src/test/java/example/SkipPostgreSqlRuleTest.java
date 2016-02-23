package example;

import static org.zalando.stups.junit.postgres.MavenProjectLayout.projectBaseDir;

import java.util.concurrent.TimeUnit;

import org.junit.Assert;
import org.junit.ClassRule;
import org.junit.Test;
import org.zalando.stups.junit.postgres.PostgreSqlRule;

public class SkipPostgreSqlRuleTest {

    static {
        System.setProperty("skipPostgreSqlRule", "anyValue");
    }

    @ClassRule
    public static final PostgreSqlRule postgres = new PostgreSqlRule.Builder()
            .addScriptLocation(projectBaseDir(SkipPostgreSqlRuleTest.class) + "/dbscripts")
            .build();

    @Test
    public void skip() throws InterruptedException {
        String value = System.getProperty("skipPostgreSqlRule");
        Assert.assertNotNull(value);
        TimeUnit.SECONDS.sleep(3);
    }

}
