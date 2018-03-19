package org.zalando.stups.docker;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.junit.jupiter.api.extension.ExtendWith;

import com.palantir.docker.compose.connection.waiting.HealthCheck;
import com.palantir.docker.compose.connection.waiting.HealthChecks;

/**
 * Annotation to activate {@link DockerComposeExtension} and to
 * configure some parts of it.
 */
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@ExtendWith(DockerComposeExtension.class)
public @interface DockerCompose {

    /**
     * Should images be pulled on startup?<br/>
     * Defaults to 'true'.
     */
    boolean pullOnStartup() default true;

    /**
     * Path to 'docker-compose.yaml'.<br/>
     * Defaults to 'src/test/resources/docker-compose.yaml'.
     */
    String file() default "src/test/resources/docker-compose.yaml";

    /**
     * Path were logs should be written to.<br/>
     * Defaults to 'target/test-docker-logs'
     */
    String saveLogsTo() default "target/test-docker-logs";

    /**
     * Name of container should wait for {@link HealthCheck}.<br/>
     * Currently only {@link HealthChecks#toHaveAllPortsOpen()} is supported.
     */
    WaitFor[] waitFor() default {};

    /**
     * To wait for {@link HealthCheck}.
     * TODO rethink this.
     */
    static @interface WaitFor {
        String containerName();
    }

}
