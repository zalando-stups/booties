package org.zalando.spring.boot.example.scheduling;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ScheduledJob {

    private final Logger log = LoggerFactory.getLogger(ScheduledJob.class);

    @Scheduled(fixedDelay = 1 * 1000, initialDelay = 1 * 1000)
    public void run() {
        log.info("SCHEDULED ACTION EXECUTED");
    }
}
