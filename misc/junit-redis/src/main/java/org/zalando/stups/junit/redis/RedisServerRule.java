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
package org.zalando.stups.junit.redis;

import org.junit.rules.ExternalResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import redis.embedded.RedisServer;
import redis.embedded.RedisServerBuilder;

/**
 * 
 * @author jbellmann
 *
 */
public class RedisServerRule extends ExternalResource {

    private final Logger log = LoggerFactory.getLogger(RedisServerRule.class);

    public static final String SKIP_REDIS_RULE = "skipRedisRule";

    private RedisServer redisServer;

    private final Builder builder;

    private RedisServerRule(Builder builder) {
        this.builder = builder;
    }

    @Override
    protected void before() throws Throwable {
        if (System.getProperty(builder.skipProperty) != null) {
            log.info("Skip RedisRule because of existing property '" + builder.skipProperty + "'");
            return;
        }
        log.info("start Redis-Instance ...");
        redisServer = new RedisServerBuilder().port(builder.port).build();
        redisServer.start();
        log.info("Redis-Instance started.");
    }

    @Override
    protected void after() {
        if (redisServer != null) {
            log.info("stopping Redis-Instance ...");
            redisServer.stop();
            log.info("Redis-Instance stopped.");
        }
    }

    public static class Builder {
        private int port = 6379;
        private String skipProperty = SKIP_REDIS_RULE;

        public Builder withPort(int port) {
            this.port = port;
            return this;
        }

        public Builder withSkipOnProperty(String property) {
            this.skipProperty = property;
            return this;
        }

        public RedisServerRule build() {
            return new RedisServerRule(this);
        }
    }
}
