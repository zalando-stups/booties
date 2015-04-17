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
package org.zalando.stups.boot.eventbus;

import java.util.concurrent.Executors;

import org.springframework.beans.factory.config.BeanPostProcessor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.util.Assert;

import com.google.common.eventbus.AsyncEventBus;
import com.google.common.eventbus.EventBus;

/**
 * Configures multiple beans and and {@link BeanPostProcessor} to support simple eventbus in the context.
 *
 * @author  jbellmann
 */
@Configuration
public class EventBusAutoConfiguration {

    @Bean
    public EventBusSupport eventBusWrapper() {
        return new EventBusSupportImpl(eventBus(), asyncEventBus());
    }

    @Bean
    public EventBus eventBus() {
        EventBus eventBus = new EventBus();
        return eventBus;
    }

    @Bean
    public AsyncEventBus asyncEventBus() {
        AsyncEventBus asyncEventBus = new AsyncEventBus("asyncDefault", Executors.newFixedThreadPool(2));
        return asyncEventBus;
    }

    @Bean
    public EventBusSubscriberBeanPostProcessor subscriberAnnotationProcessor() {
        return new EventBusSubscriberBeanPostProcessor(eventBus(), asyncEventBus());
    }

    /**
     * Simple implementation of {@link EventBusSupport}.
     *
     * @author  jbellmann
     */
    static final class EventBusSupportImpl implements EventBusSupport {
        private EventBus eventBus;
        private AsyncEventBus asyncEventBus;

        EventBusSupportImpl(final EventBus eventBus, final AsyncEventBus asyncEventBus) {
            Assert.notNull(eventBus, "EventBus should not be null");
            Assert.notNull(asyncEventBus, "AsyncEventBus should not be null");
            this.eventBus = eventBus;
            this.asyncEventBus = asyncEventBus;
        }

        @Override
        public void post(final Object event) {
            this.eventBus.post(event);
        }

        @Override
        public void postAsync(final Object event) {
            this.asyncEventBus.post(event);
        }
    }
}
