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

import de.flapdoodle.embed.process.config.RuntimeConfigBuilder;
import de.flapdoodle.embed.process.config.io.ProcessOutput;
import de.flapdoodle.embed.process.extract.UUIDTempNaming;
import de.flapdoodle.embed.process.io.progress.IProgressListener;
import de.flapdoodle.embed.process.io.progress.StandardConsoleProgressListener;
import de.flapdoodle.embed.process.runtime.ICommandLinePostProcessor;
import de.flapdoodle.embed.process.store.Downloader;
import de.flapdoodle.embed.process.store.IArtifactStore;
import ru.yandex.qatools.embed.postgresql.Command;
import ru.yandex.qatools.embed.postgresql.config.DownloadConfigBuilder;
import ru.yandex.qatools.embed.postgresql.ext.ArtifactStoreBuilder;
import ru.yandex.qatools.embed.postgresql.ext.SubdirTempDir;

/**
 * Maybe I did not get it the right way but all this was just needed to change
 * the ProgressListenr for extraction.
 * 
 * @author jbellmann
 *
 */
public class RuleRuntimeConfigBuilder extends RuntimeConfigBuilder {

    public RuntimeConfigBuilder defaults(Command command, boolean fullLog) {
        processOutput().setDefault(ProcessOutput.getDefaultInstance(command.commandName()));
        commandLinePostProcessor().setDefault(new ICommandLinePostProcessor.Noop());
        artifactStore().setDefault(getArtifactStore(command, fullLog));
        return this;
    }

    protected IArtifactStore getArtifactStore(Command command, boolean fullLog) {
        return new RuleArtifactStoreBuilder().defaultsWithoutCache(command, fullLog).build();
    }

    static class RuleArtifactStoreBuilder extends ArtifactStoreBuilder {

        // @formatter:off
        public ArtifactStoreBuilder defaultsWithoutCache(Command command, boolean fullLog) {
            IProgressListener progressListner = new StandardConsoleProgressListener();
            if (!fullLog) {
                progressListner = new StartEndConsoleProgressListener();
            }

            tempDir().setDefault(new SubdirTempDir());
            executableNaming().setDefault(new UUIDTempNaming());
            download().setDefault(new DownloadConfigBuilder().defaultsForCommand(command)
                                                             .progressListener(progressListner)
                                                             .build());

            downloader().setDefault(new Downloader());
            // disable caching
            useCache().setDefault(false);
            return this;
        }
        // @formatter:on

    }
}
