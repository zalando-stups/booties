package booties.async.tasks;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class AnotherSimpleTask {

    private final Logger log = LoggerFactory.getLogger(AnotherSimpleTask.class);

    @Scheduled(fixedRate = 1 * 1000, initialDelay = 1 * 1000)
    public void doSomething() {
        log.info("DO SOMETHING IN SCHEDULED TASK ...");
        try {
            Thread.sleep(2300);
        } catch (InterruptedException e) {
        }
    }
}
