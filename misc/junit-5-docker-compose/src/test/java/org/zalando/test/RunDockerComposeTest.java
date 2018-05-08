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
