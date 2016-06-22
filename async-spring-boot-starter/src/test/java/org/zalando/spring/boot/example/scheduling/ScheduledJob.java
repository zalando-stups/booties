/**
 * Copyright (C) 2015 Zalando SE (http://tech.zalando.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.zalando.spring.boot.example.scheduling;

import static java.lang.Thread.currentThread;
import static org.assertj.core.api.Assertions.assertThat;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.zalando.spring.boot.scheduling.SchedulingProperties;

@Component
public class ScheduledJob {

    private final Logger log = LoggerFactory.getLogger(ScheduledJob.class);

    @Autowired
    private SchedulingProperties schedulingProperties;

    @Scheduled(fixedDelay = 1 * 1000, initialDelay = 1 * 1000)
    public void run() {
        log.info("SCHEDULED ACTION EXECUTED");
        assertThat(currentThread().getName().startsWith(schedulingProperties.getThreadNamePrefix()));
    }
}
