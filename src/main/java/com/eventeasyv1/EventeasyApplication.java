package com.eventeasyv1;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class EventeasyApplication {
    public static void main(String[] args) {
        SpringApplication.run(EventeasyApplication.class, args);
    }
}