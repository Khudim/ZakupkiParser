package com.khudim.config;

import liquibase.integration.spring.SpringLiquibase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.util.Assert;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class LiquibaseConfig {

    @Autowired
    private DataSource dataSource;

    @Autowired
    private ResourceLoader resourceLoader;

    @Bean
    public SpringLiquibase liquibase() {

        // Locate change log file
        String changelogFile = "classpath:/liquibase/changelog.xml";
        Resource resource = resourceLoader.getResource(changelogFile);
        Assert.state(resource.exists(), "Unable to find file: " + changelogFile);

        // Configure Liquibase
        SpringLiquibase liquibase = new SpringLiquibase();
        liquibase.setChangeLog(changelogFile);
        liquibase.setContexts("test,dev,prod");
        liquibase.setDataSource(dataSource);
        liquibase.setDropFirst(false);
        liquibase.setShouldRun(true);

        // Verbose logging
        Map<String, String> params = new HashMap<>();
        params.put("verbose","true");
        liquibase.setChangeLogParameters(params);

        return liquibase;
    }
}
