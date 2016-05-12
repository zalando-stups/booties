package org.zalando.spring.boot.scheduling;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@EnableConfigurationProperties({ SchedulingProperties.class })
@Import({ CustomSchedulingConfiguration.class })
public class SchedulingAutoConfiguration {
}