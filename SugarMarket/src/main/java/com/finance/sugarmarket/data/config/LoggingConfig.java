package com.finance.sugarmarket.data.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.File;

@Configuration
public class LoggingConfig {

    @Value("${logging.file.path:${user.home}/ZZZ/spend-app-logs}")
    private String logFilePath;

    @Bean
    public CommandLineRunner createLogDirectory() {
        return args -> {
            File directory = new File(logFilePath);
            if (!directory.exists()) {
                boolean created = directory.mkdirs();
                if (created) {
                    System.out.println("Log directory created at: " + logFilePath);
                } else {
                    System.err.println("Failed to create log directory at: " + logFilePath);
                }
            } else {
                System.out.println("Log directory already exists at: " + logFilePath);
            }
        };
    }
}