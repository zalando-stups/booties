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

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.jdbc.datasource.init.ScriptUtils;

/**
 * Activates {@link PostgresqlExtension} and provides some configuration to be
 * applied.
 * 
 * @author jbellmann
 *
 */
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@ExtendWith(PostgresqlExtension.class)
public @interface PostgreSQL {
    /**
     * Port to be used by postgres.<br/>
     * Defaults to 5432.
     */
    int port() default 5432;

    /**
     * Username to be used to connect.<br/>
     * Default is 'postgres'.
     */
    String username() default "postgres";

    /**
     * Password to be used to connnect.<br/>
     * Default is 'postgres'.
     */
    String password() default "postgres";

    /**
     * Name for db to be created.<br/>
     * Default is 'test'.
     */
    String dbName() default "test";

    /**
     * Version of postgres to be used.<br/>
     * Defaults to {@link Version#V10}
     */
    Version version() default Version.V10;

    /**
     * Locations for scripts to run as migrations.
     */
    String[] locations() default {};

    /**
     * End of statement separator.<br/>
     * Defaults to {@link ScriptUtils#EOF_STATEMENT_SEPARATOR}
     */
    String separator() default ScriptUtils.EOF_STATEMENT_SEPARATOR;

    /**
     * Environment property that when set the {@link PostgreSqlRule} will be
     * skipped.<br/>
     * Default is {@link PostgreSqlRule#SKIP_POSTGRE_SQL_RULE}
     */
    String skipProperty() default PostgreSqlRule.SKIP_POSTGRE_SQL_RULE;

    /**
     * Versions available.
     */
    enum Version {
        V10,
        V9_6
    }
}
