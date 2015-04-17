/**
 * Copyright 2015 Zalando SE
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.zalando.stups.example;

import java.util.concurrent.CountDownLatch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Component;

import com.google.common.eventbus.DeadEvent;
import com.google.common.eventbus.Subscribe;

/**
 * This listener brings dead events (events where no listener was found) to the console.
 *
 * @author  Joerg Bellmann
 */
@Component
public class LoggingDeadEventListener {

    private static final Logger LOG = LoggerFactory.getLogger(LoggingDeadEventListener.class);
    private CountDownLatch latch = new CountDownLatch(1);

    @Subscribe
    public void handleDeadEvent(final DeadEvent deadEvent) {
        LOG.warn("Ooops, no listener found for this event: {}", deadEvent.getEvent());
        latch.countDown();
    }

    public CountDownLatch getLatch() {
        return latch;
    }

}
