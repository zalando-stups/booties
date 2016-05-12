package org.zalando.spring.boot.example.job;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class ExampleJob {

    private final Logger log = LoggerFactory.getLogger(ExampleJob.class);

    @Async
    public void run() {
        log.info("RUN EXAMPLE JOB");
    }

}
