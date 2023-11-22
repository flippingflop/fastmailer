package com.flippingflop.fastmailer.test;

import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.testcontainers.containers.MySQLContainer;

@Configuration
public class MysqlTestContainerConfig {

    private static final MySQLContainer<?> MYSQL_CONTAINER;

    static {
        MYSQL_CONTAINER = new MySQLContainer<>("mysql:8.0.29")
                .withDatabaseName("test-fastmailer")
                .withUsername("test-user")
                .withPassword("test-password");
        MYSQL_CONTAINER.start();
    }

    public static class MysqlTestContainerInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
        @Override
        public void initialize(ConfigurableApplicationContext applicationContext) {
            TestPropertyValues.of(
                    "spring.datasource.url=" + MYSQL_CONTAINER.getJdbcUrl(),
                    "spring.datasource.username=" + MYSQL_CONTAINER.getUsername(),
                    "spring.datasource.password=" + MYSQL_CONTAINER.getPassword()
            ).applyTo(applicationContext.getEnvironment());
        }
    }

}
