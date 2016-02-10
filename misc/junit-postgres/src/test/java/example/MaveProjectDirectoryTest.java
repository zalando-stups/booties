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
package example;

import static org.zalando.stups.junit.postgres.MavenProjectLayout.projectBaseDir;
import static org.zalando.stups.junit.postgres.MavenProjectLayout.targetDirectory;

import java.net.URL;

import org.junit.Test;

public class MaveProjectDirectoryTest {

    @Test
    public void getProjectRoot() {
        URL url = MaveProjectDirectoryTest.class.getResource("/");
        System.out.println(url.getFile());
    }

    @Test
    public void testMavenProjectLayout() {
        String projectBaseDir = projectBaseDir(MaveProjectDirectoryTest.class);
        System.out.println(projectBaseDir);
        String targetDirectory = targetDirectory(MaveProjectDirectoryTest.class);
        System.out.println(targetDirectory);
    }

}
