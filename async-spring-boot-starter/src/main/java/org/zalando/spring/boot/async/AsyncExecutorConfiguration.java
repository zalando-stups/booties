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

import static org.springframework.scheduling.annotation.AsyncAnnotationBeanPostProcessor.DEFAULT_TASK_EXECUTOR_BEAN_NAME;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.NoUniqueBeanDefinitionException;
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
@EnableAsync
public class AsyncExecutorConfiguration {

    @Configuration
    @AutoConfigureBefore({ ProxyAsyncConfiguration.class })
    @Role(BeanDefinition.ROLE_INFRASTRUCTURE)
    static class CustomAsyncConfigurerConfiguration {

        @Bean
        @Role(BeanDefinition.ROLE_INFRASTRUCTURE)
        public CustomAsyncConfigurer customAsyncConfigurer() {
            return new CustomAsyncConfigurer();
        }
    }

    @Configuration
    @Role(BeanDefinition.ROLE_INFRASTRUCTURE)
    @AutoConfigureBefore({ ProxyAsyncConfiguration.class })
    static class ThreadPoolTaskExecutorConfiguration {

        private final Logger log = LoggerFactory.getLogger(ThreadPoolTaskExecutorConfiguration.class);

        @ConditionalOnProperty(prefix = "async.executor", name = "enabled", matchIfMissing = true)
        @Bean(name = DEFAULT_TASK_EXECUTOR_BEAN_NAME)
        @Role(BeanDefinition.ROLE_INFRASTRUCTURE)
        public ThreadPoolTaskExecutor threadPoolTaskExecutor(AsyncExecutorProperties properties) {
            log.info("Creating ThreadPoolTaskExecutor 'taskExecutor' with core-size={} max-size={} queue-capacity={}",
                    properties.getCorePoolSize(), properties.getMaxPoolSize(), properties.getQueueCapacity());

            ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
            executor.setCorePoolSize(properties.getCorePoolSize());
            executor.setMaxPoolSize(properties.getMaxPoolSize());

            executor.setQueueCapacity(properties.getQueueCapacity());
            executor.setThreadNamePrefix(properties.getThreadNamePrefix());
            executor.setAwaitTerminationSeconds(properties.getAwaitTerminationSeconds());

            executor.setRejectedExecutionHandler(new ThreadPoolExecutor.DiscardOldestPolicy());

            executor.setDaemon(properties.isDaemons());
            executor.setWaitForTasksToCompleteOnShutdown(properties.isWaitForTasksToCompleteOnShutdown());

            executor.afterPropertiesSet();
            return executor;
        }
    }

    static class CustomAsyncConfigurer implements AsyncConfigurer, BeanFactoryAware, InitializingBean {

        private final Logger log = LoggerFactory.getLogger(CustomAsyncConfigurer.class);

        private AsyncExecutorProperties properties;
        private BeanFactory beanFactory;

        @Override
        public Executor getAsyncExecutor() {
            if (properties.isEnabled()) {
                ThreadPoolTaskExecutor executor = null;
                try {
                    executor = beanFactory.getBean(ThreadPoolTaskExecutor.class);
                } catch (NoUniqueBeanDefinitionException e) {
                    executor = beanFactory.getBean(DEFAULT_TASK_EXECUTOR_BEAN_NAME, ThreadPoolTaskExecutor.class);
                } catch (NoSuchBeanDefinitionException ex) {
                }
                if (executor != null) {
                    log.info("use default TaskExecutor ...");
                    return executor;
                } else {
                    throw new BeanCreationException("Expecting a 'ThreadPoolTaskExecutor' exists, but was 'null'");
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

        @Override
        public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
            this.beanFactory = beanFactory;
        }

        @Override
        public void afterPropertiesSet() throws Exception {
            this.properties = beanFactory.getBean(AsyncExecutorProperties.class);
        }

    }
}
