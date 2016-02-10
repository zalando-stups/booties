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
package org.zalando.stups.junit.postgres;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.flapdoodle.embed.process.config.IRuntimeConfig;
import de.flapdoodle.embed.process.distribution.Distribution;
import ru.yandex.qatools.embed.postgresql.PostgresExecutable;
import ru.yandex.qatools.embed.postgresql.PostgresProcess;
import ru.yandex.qatools.embed.postgresql.config.PostgresConfig;

/**
 * To override the {@link #stopInternal()} method.
 * 
 * @author jbellmann
 *
 */
public class RulePostgresProcess extends PostgresProcess {

    private final Logger logger = LoggerFactory.getLogger(RulePostgresProcess.class);

    boolean stopped = false;

    public RulePostgresProcess(Distribution distribution, PostgresConfig config, IRuntimeConfig runtimeConfig,
            PostgresExecutable executable) throws IOException {
        super(distribution, config, runtimeConfig, executable);
    }

    /**
     * The only thing we override here is to use {@link #sendKillToProcess()}
     * directly for faster termination. The process in super
     * {@link #stopInternal()} take to long. (30+ seconds)
     */
    @Override
    protected void stopInternal() {
        synchronized (this) {
            if (!stopped) {
                stopped = true;
                logger.info("trying to stop postgresql");
                if (!sendKillToProcess()) {
                    logger.warn("could not stop postgresql, try next");
                    if (!sendTermToProcess()) {
                        logger.warn("could not stop postgresql, try next");
                        if (!tryKillToProcess()) {
                            logger.warn("could not stop postgresql the second time, try one last thing");
                        }
                    }
                }
            }
            deleteTempFiles();
        }
    }

}
