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

import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.zalando.stups.junit.postgres.PostgreSQL.Version;

/**
 * Extension that delegates to {@link PostgreSqlRule}.
 * 
 * @author jbellmann
 *
 */
public class PostgresqlExtension implements BeforeAllCallback, AfterAllCallback {

    private PostgreSqlRule rule;

    @Override
    public void beforeAll(ExtensionContext context) throws Exception {
        Class<?> clazz = context.getTestClass().get();
        PostgreSQL annotation = clazz.getAnnotation(PostgreSQL.class);
        rule = buildRule(annotation);
        try {
            rule.before();
        } catch (Throwable e) {
            rule = null;
            throw new Exception(e);
        }
    }

    @Override
    public void afterAll(ExtensionContext context) throws Exception {
        if (rule != null) {
            rule.after();
        }
    }

    protected PostgreSqlRule buildRule(PostgreSQL annotation) {
        PostgreSqlRule.Builder builder = new PostgreSqlRule.Builder()
                                .withPort(annotation.port())
                                .withUsername(annotation.username())
                                .withPassword(annotation.password())
                                .withDbName(annotation.dbName())
                                .withSeparator(annotation.separator())
                                .skipOnProperty(annotation.skipProperty());

        if(Version.V10.equals(annotation.version())) {
            builder = builder.withVersion(ru.yandex.qatools.embed.postgresql.distribution.Version.V10_2);
        }else {
            builder = builder.withVersion(ru.yandex.qatools.embed.postgresql.distribution.Version.V9_6_7);
        }

        for(String location : annotation.locations()) {
            builder = builder.addScriptLocation(location);
        }

        return builder.build();
    }

}
