package com.foundly.foundlyplatform;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class FoundlyPlatformApplication {

    public static void main(String[] args) {
        SpringApplication.run(FoundlyPlatformApplication.class, args);
    }

}