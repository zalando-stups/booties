package org.zalando.test;

import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.zalando.stups.docker.DockerCompose;

import com.palantir.docker.compose.DockerComposeRule;

@DockerCompose
class RunDockerComposeTest {

    private DockerComposeRule docker;

    @Test
    void testParametersWork(DockerComposeRule rule) {
        this.docker = rule;
        Assertions.assertNotNull(docker, "should be passed as a parameter");
    }

    @Test
    void runDockerCompose() throws InterruptedException {
        System.out.println("before sleep");
        TimeUnit.SECONDS.sleep(3);
        System.out.println("after sleep");
    }
}
