package org.zalando.stups.junit.redis;

import org.junit.rules.ExternalResource;

import redis.embedded.RedisServer;
import redis.embedded.RedisServerBuilder;

/**
 * 
 * @author jbellmann
 *
 */
public class RedisServerRule extends ExternalResource {

    private RedisServer redisServer;
    private final int port;

    RedisServerRule(int port) {
        this.port = port;
    }

    public RedisServerRule() {
        this(6379);
    }

    @Override
    protected void before() throws Throwable {
        redisServer = new RedisServerBuilder().port(port).build();
        redisServer.start();
    }

    @Override
    protected void after() {
        if (redisServer != null) {
            redisServer.stop();
        }
    }

}
