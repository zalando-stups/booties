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

import java.io.File;
import java.util.concurrent.TimeUnit;

import org.junit.ClassRule;
import org.junit.Test;
import org.zalando.stups.junit.postgres.PostgreSqlRule;

public class ExampleTest {

    @ClassRule
    public static final PostgreSqlRule postgres = new PostgreSqlRule.Builder().withPort(5434)
            .addScriptLocation(getScriptDirectory())
            .build();

    @Test
    public void runTest() throws InterruptedException {
        TimeUnit.SECONDS.sleep(3);
    }

    private static String getScriptDirectory() {
        return new File(ExampleTest.class.getResource("/scripts/00_create_schema.sql").getFile()).getParent();
    }
}
