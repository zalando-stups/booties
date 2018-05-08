package org.zalando.junit.example;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.zalando.stups.junit.postgres.PostgresqlExtension;

@ExtendWith({PostgresqlExtension.class,SpringExtension.class})
@SpringBootTest
public class Junit5ExampleApplicationTest {

    @Test
    void contextLoads() {
        Assertions.assertTrue(true);;
    }
}
