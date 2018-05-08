package org.zalando.junit.example;

import org.assertj.core.api.Assertions;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.zalando.stups.junit.postgres.PostgreSqlRule;

@RunWith(SpringRunner.class)
@SpringBootTest
public class Junit4ExampleApplicationTest {

    @ClassRule
    public static final PostgreSqlRule postgres = new PostgreSqlRule.Builder().build();

    @Test
    public void contextLoads() {
        Assertions.assertThat(true).isTrue();
    }
}
