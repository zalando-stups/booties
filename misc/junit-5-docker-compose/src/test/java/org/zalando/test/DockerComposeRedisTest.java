package org.zalando.test;

import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.Test;
import org.zalando.stups.docker.DockerCompose;
import org.zalando.stups.docker.DockerCompose.WaitFor;

@DockerCompose(
        file="src/test/resources/docker-compose-2.yaml",
        saveLogsTo="target/redis-test-logs",
        waitFor= {
             @WaitFor(containerName="redis")
        })
public class DockerComposeRedisTest {

    @Test
    void runDockerCompose() throws InterruptedException {
        System.out.println("before sleep");
        TimeUnit.SECONDS.sleep(3);
        System.out.println("after sleep");
    }
}
