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
package org.zalando.spring.boot.async;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Role;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.ProxyAsyncConfiguration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
@AutoConfigureBefore({ ProxyAsyncConfiguration.class })
@EnableAsync
public class AsyncExecutorConfiguration implements AsyncConfigurer {

    private final Logger log = LoggerFactory.getLogger(AsyncExecutorConfiguration.class);

    @Autowired
    private AsyncExecutorProperties properties;

    @Autowired(required = false)
    private ThreadPoolTaskExecutor executor;

    @Override
    public Executor getAsyncExecutor() {
        if (properties.isEnabled()) {
            if (executor != null) {
                return executor;
            } else {
                throw new BeanCreationException("Expecting a 'ThreadPoolTaskExecutor' injected, but was 'null'");
            }
        } else {
            log.info(
                    "'AsyncExecutorConfiguration' is disabled, so create 'SimpleAsyncTaskExecutor' with 'threadNamePrefix' - '{}'",
                    properties.getThreadNamePrefix());
            return new SimpleAsyncTaskExecutor(properties.getThreadNamePrefix());
        }
    }

    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        try {
            return (AsyncUncaughtExceptionHandler) Class.forName(properties.getExceptionHandler()).newInstance();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e.getMessage(), e);
        } catch (InstantiationException e) {
            throw new RuntimeException(e.getMessage(), e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    @Configuration
    @AutoConfigureBefore({ ProxyAsyncConfiguration.class })
    public static class ThreadPoolTaskExecutorConfiguration {

        private final Logger log = LoggerFactory.getLogger(ThreadPoolTaskExecutorConfiguration.class);

        @Autowired
        private AsyncExecutorProperties properties;

        @ConditionalOnProperty(prefix = "async.executor", name = "enabled", matchIfMissing = true)
        @Bean(name = "taskExecutor")
        @Role(BeanDefinition.ROLE_INFRASTRUCTURE)
        public ThreadPoolTaskExecutor threadPoolTaskExecutor() {
            log.info("Creating Async Pool with core-size={} max-size={} queue-capacity={}",
                    properties.getCorePoolSize(), properties.getMaxPoolSize(), properties.getQueueCapacity());

            ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
            executor.setCorePoolSize(properties.getCorePoolSize());
            executor.setMaxPoolSize(properties.getMaxPoolSize());

            executor.setQueueCapacity(properties.getQueueCapacity());
            executor.setThreadNamePrefix(properties.getThreadNamePrefix());
            executor.setAwaitTerminationSeconds(properties.getAwaitTerminationSeconds());

            executor.setRejectedExecutionHandler(new ThreadPoolExecutor.DiscardOldestPolicy());

            executor.afterPropertiesSet();
            return executor;
        }
    }
}
