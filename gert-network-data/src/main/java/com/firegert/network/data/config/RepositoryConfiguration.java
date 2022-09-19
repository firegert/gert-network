package com.firegert.network.data.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jdbc.repository.config.EnableJdbcRepositories;

import javax.annotation.PostConstruct;

@Slf4j
@Configuration
@EnableJdbcRepositories(basePackages = {"com.firegert.network.data"})
@EntityScan(basePackages = {"com.firegert.network.data.entity"})
public class RepositoryConfiguration {


    @PostConstruct
    public void log() {
        log.info("I am in RepositoryConfiguration");
    }
}
