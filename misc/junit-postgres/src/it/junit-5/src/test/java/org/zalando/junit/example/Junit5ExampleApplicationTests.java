package org.zalando.junit.example;

import static org.assertj.core.api.Assertions.assertThat;

import javax.sql.DataSource;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.zalando.stups.junit.postgres.PostgreSQL;
import org.zalando.stups.junit.postgres.PostgresqlExtension;

@PostgreSQL(locations = { "src/test/resources" })
@ExtendWith({ PostgresqlExtension.class, SpringExtension.class })
@SpringBootTest
public class Junit5ExampleApplicationTests {

    private static final String SQL = "SELECT ALL FROM EMPLOYEE;";

    @Autowired
    private DataSource dataSource;

    @Test
    void contextLoads() {
        assertThat(dataSource).isNotNull();
        JdbcTemplate jdbc = new JdbcTemplate(dataSource);
        jdbc.afterPropertiesSet();
        jdbc.execute(SQL);
    }
}
