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

import java.util.concurrent.TimeUnit;

import org.junit.Test;

import org.junit.runner.RunWith;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;

import org.springframework.context.support.AbstractApplicationContext;

import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author  jbellmann
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = {ExampleApp.class})
@IntegrationTest
public class ExampleAppIT {

    private static final Logger LOG = LoggerFactory.getLogger(ExampleAppIT.class);

    @Autowired
    private MultiEventBusSender multiEventBusSender;

    @Autowired
    private SimpleEventSender simpleEventSender;

    @Autowired
    private EventBusSupportSender eventBusSupportSender;

    @Autowired
    private SimpleAsyncEventSender simpleAsyncEventSender;

    @Autowired
    private SimpleSubscriber subscriber;

    @Autowired
    private LoggingDeadEventListener deadEventListener;

    @Autowired
    private AbstractApplicationContext aac;

    @Test
    public void testEventBus() throws InterruptedException {
        LOG.info("Send Events ....");
        this.multiEventBusSender.sendEvents();
        this.simpleEventSender.sendEvents();
        this.simpleAsyncEventSender.sendEvents();
        this.eventBusSupportSender.sendEvents();
        subscriber.getLatch().await(10, TimeUnit.SECONDS);
        deadEventListener.getLatch().await(5, TimeUnit.SECONDS);
        aac.close();
    }
}
