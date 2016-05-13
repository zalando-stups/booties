package booties.async.jobs;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class SimpleJob {

    private final Logger log = LoggerFactory.getLogger(SimpleJob.class);

    @Async
    public void doSomething() {
        log.info("DO SOMETHING IN JOB...");
    }
}
