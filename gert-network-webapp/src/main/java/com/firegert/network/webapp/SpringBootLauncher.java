package com.firegert.network.webapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"com.firegert.network", "com.firegert.network.data.config"})
public class SpringBootLauncher {

    public static void main(String[] args) {
        SpringApplication.run(SpringBootLauncher.class, args);
    }
}
