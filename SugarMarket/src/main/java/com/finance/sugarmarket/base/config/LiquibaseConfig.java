package com.finance.sugarmarket.base.config;

import liquibase.integration.spring.SpringLiquibase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import javax.sql.DataSource;

@Configuration
public class LiquibaseConfig {

    private final DataSource dataSource;
    private final Environment env;

    public LiquibaseConfig(DataSource dataSource, Environment env) {
        this.dataSource = dataSource;
        this.env = env;
    }

    @Bean
    public SpringLiquibase liquibase() {
        SpringLiquibase liquibase = new SpringLiquibase();
        liquibase.setDataSource(dataSource);
        liquibase.setChangeLog(env.getRequiredProperty("spring.liquibase.change-log"));
        liquibase.setContexts(env.getProperty("spring.liquibase.contexts"));
        liquibase.setDefaultSchema(env.getProperty("spring.liquibase.default-schema"));
        liquibase.setDropFirst(Boolean.parseBoolean(env.getProperty("spring.liquibase.drop-first", "false")));
        return liquibase;
    }
}