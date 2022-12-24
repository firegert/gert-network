package com.firegert.gert.network.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"com.firegert.gert.network"})
public class TaskManagerWebApplication {

    public static void main(String[] args) {
        SpringApplication.run(TaskManagerWebApplication.class, args);
    }

}
