package org.zalando.spring.boot.scheduling;

import java.util.concurrent.Executor;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Role;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.ScheduledAnnotationBeanPostProcessor;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.concurrent.ConcurrentTaskScheduler;
import org.springframework.scheduling.concurrent.CustomizableThreadFactory;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

@Configuration
@EnableScheduling
public class CustomSchedulingConfiguration implements SchedulingConfigurer {

    private final Logger log = LoggerFactory.getLogger(CustomSchedulingConfiguration.class);

    @Autowired(required = false)
    @Qualifier("taskExecutor")
    private Executor executor;

    @Autowired
    private SchedulingProperties properties;

    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        taskRegistrar.setScheduler(taskScheduler());
    }

    @Bean(destroyMethod = "shutdown")
    @Role(BeanDefinition.ROLE_INFRASTRUCTURE)
    public ScheduledExecutorService scheduledExecutorService() {
        ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(properties.getCorePoolSize());

        executor.setMaximumPoolSize(properties.getMaxPoolSize());

        executor.setThreadFactory(new CustomizableThreadFactory(properties.getThreadNamePrefix()));

        executor.setRejectedExecutionHandler(getConfiguredRejectedExecutionHandler());
        return executor;
    }

    private RejectedExecutionHandler getConfiguredRejectedExecutionHandler() {
        try {
            return (RejectedExecutionHandler) Class.forName(properties.getRejectedExecutionHandler()).newInstance();
        } catch (InstantiationException e) {
            throw new RuntimeException(e.getMessage(), e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e.getMessage(), e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    @Bean(name = ScheduledAnnotationBeanPostProcessor.DEFAULT_TASK_SCHEDULER_BEAN_NAME)
    @Role(BeanDefinition.ROLE_INFRASTRUCTURE)
    public TaskScheduler taskScheduler() {
        if (executor != null) {
            log.info("create task-scheduler with pre-configured executor ...");
            return new ConcurrentTaskScheduler(executor, scheduledExecutorService());
        } else {
            log.info("create task-scheduler without pre-configured executor ...");
            return new ConcurrentTaskScheduler(scheduledExecutorService());
        }
    }

}
