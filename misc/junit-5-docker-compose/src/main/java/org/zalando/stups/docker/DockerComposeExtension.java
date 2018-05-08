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
package org.zalando.stups.docker;

import static com.palantir.docker.compose.connection.waiting.HealthChecks.toHaveAllPortsOpen;
import static java.util.Optional.ofNullable;

import java.io.IOException;
import java.util.Optional;

import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;
import org.zalando.stups.docker.DockerCompose.WaitFor;

import com.palantir.docker.compose.DockerComposeRule;

public class DockerComposeExtension implements BeforeAllCallback, AfterAllCallback, ParameterResolver {

    private DockerComposeRule docker;

    public DockerComposeExtension() {
    }

    public void beforeAll(ExtensionContext extensionContext) throws Exception {
        Class<?> clazz = extensionContext.getTestClass().get();
        DockerCompose annotation = clazz.getAnnotation(DockerCompose.class);

        Optional<DockerComposeRule> ruleOpt = buildRule(annotation);
        ruleOpt.ifPresent(rule -> {
            try {
                rule.before();
                docker = rule;
            } catch (IOException | InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public void afterAll(ExtensionContext extensionContext) throws Exception {
        ofNullable(docker).ifPresent(docker -> {
            docker.after();
        });
    }

    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext)
            throws ParameterResolutionException {
        return parameterContext.getParameter().getType().equals(DockerComposeRule.class);
    }

    public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext)
            throws ParameterResolutionException {
        return docker;
    }

    protected Optional<DockerComposeRule> buildRule(DockerCompose annotation) {
        DockerComposeRule.Builder builder = DockerComposeRule.builder()
                                                                .pullOnStartup(annotation.pullOnStartup())
                                                                .file(annotation.file())
                                                                .saveLogsTo(annotation.saveLogsTo());
                                                                for(WaitFor wf : annotation.waitFor()) {
                                                                    builder.waitingForService(wf.containerName(), toHaveAllPortsOpen());
                                                                }
        return Optional.ofNullable(builder.build());
    }
}
