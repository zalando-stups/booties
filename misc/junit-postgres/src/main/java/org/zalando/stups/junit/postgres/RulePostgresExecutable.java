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

import de.flapdoodle.embed.process.config.IRuntimeConfig;
import de.flapdoodle.embed.process.distribution.Distribution;
import de.flapdoodle.embed.process.extract.IExtractedFileSet;
import ru.yandex.qatools.embed.postgresql.PostgresExecutable;
import ru.yandex.qatools.embed.postgresql.PostgresProcess;
import ru.yandex.qatools.embed.postgresql.config.PostgresConfig;

/**
 * To create a custom {@link PostgresProcess}.
 * 
 * @author jbellmann
 *
 */
public class RulePostgresExecutable extends PostgresExecutable {

    public RulePostgresExecutable(Distribution distribution, PostgresConfig config, IRuntimeConfig runtimeConfig,
            IExtractedFileSet exe) {
        super(distribution, config, runtimeConfig, exe);
    }

    @Override
    protected PostgresProcess start(Distribution distribution, PostgresConfig config, IRuntimeConfig runtime)
            throws IOException {
        return new RulePostgresProcess(distribution, config, runtime, this);
    }

}
