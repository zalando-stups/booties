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

import java.lang.reflect.Method;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;

import org.springframework.util.Assert;

import com.google.common.eventbus.AsyncEventBus;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;

/**
 * @author  jbellmann
 */
public class EventBusSubscriberBeanPostProcessor implements BeanPostProcessor {

    private final Logger logger = LoggerFactory.getLogger(EventBusSubscriberBeanPostProcessor.class);

    private EventBus eventBus;
    private AsyncEventBus asyncEventBus;

    @Autowired
    public EventBusSubscriberBeanPostProcessor(final EventBus eventBus, final AsyncEventBus asyncEventBus) {
        Assert.notNull(eventBus, "EventBus should not be null");
        Assert.notNull(asyncEventBus, "AsyncEventBus should not be null");
        this.eventBus = eventBus;
        this.asyncEventBus = asyncEventBus;
    }

    @Override
    public Object postProcessBeforeInitialization(final Object bean, final String beanName) throws BeansException {
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(final Object bean, final String beanName) throws BeansException {

        for (Method m : bean.getClass().getMethods()) {
            if (m.isAnnotationPresent(Subscribe.class)) {
                logger.info("register bean as subscriber");
                this.eventBus.register(bean);
                this.asyncEventBus.register(bean);
                break;
            }
        }

        return bean;
    }

}
