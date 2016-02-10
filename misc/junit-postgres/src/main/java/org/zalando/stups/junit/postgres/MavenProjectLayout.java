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

import java.io.File;

/**
 * Utility class to easier configure script-directories.
 * 
 * @author jbellmann
 *
 */
public class MavenProjectLayout {

    public static String targetClassesDirectory(Class<?> testClass) {
        return testClass.getResource("/").getPath();
    }

    public static String targetDirectory(Class<?> testClass) {
        return new File(testClass.getResource("/").getPath()).getParentFile().getAbsolutePath();
    }

    public static String projectBaseDir(Class<?> testClass) {
        return new File(targetClassesDirectory(testClass)).getParentFile().getParentFile().getAbsolutePath();
    }
}