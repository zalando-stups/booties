package org.zalando.junit.example;

import static org.assertj.core.api.Assertions.assertThat;

import javax.sql.DataSource;

import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit4.SpringRunner;
import org.zalando.stups.junit.postgres.PostgreSqlRule;

@RunWith(SpringRunner.class)
@SpringBootTest
public class Junit4ExampleApplicationTest {

    @ClassRule
    public static final PostgreSqlRule postgres = new PostgreSqlRule.Builder()
                                                                    .addScriptLocation("src/test/resources")
                                                                    .build();

    private static final String SQL = "SELECT ALL FROM EMPLOYEE;";

    @Autowired
    private DataSource dataSource;

    @Test
    public void contextLoads() {
        assertThat(dataSource).isNotNull();
        JdbcTemplate jdbc = new JdbcTemplate(dataSource);
        jdbc.afterPropertiesSet();
        jdbc.execute(SQL);
    }
}
