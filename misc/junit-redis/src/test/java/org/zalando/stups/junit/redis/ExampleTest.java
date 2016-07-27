package org.zalando.stups.junit.redis;

import java.util.concurrent.TimeUnit;

import org.junit.ClassRule;
import org.junit.Test;

public class ExampleTest {

    @ClassRule
    public static RedisServerRule redisServerRule = new RedisServerRule.Builder().build();

    @Test
    public void startUp() throws InterruptedException {
        TimeUnit.SECONDS.sleep(3);
    }

}
