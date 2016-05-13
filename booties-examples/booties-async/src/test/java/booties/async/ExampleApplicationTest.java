package booties.async;

import java.util.concurrent.TimeUnit;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import booties.async.jobs.SimpleJob;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = { ExampleApplication.class })
public class ExampleApplicationTest {

    @Autowired
    private SimpleJob simpleJob;

    @Test
    public void startUp() throws InterruptedException {
        TimeUnit.SECONDS.sleep(2);
        simpleJob.doSomething();
        TimeUnit.SECONDS.sleep(2);
        simpleJob.doSomething();
        TimeUnit.SECONDS.sleep(10);
    }

}
