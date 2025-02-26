package com.cleaner.java;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {
        "com.cleaner.java.controller",
        "com.cleaner.java.models",
        "com.cleaner.java.service",
})
public class JavaCodeCleanerApplication {

    public static void main(String[] args) {
        SpringApplication.run(JavaCodeCleanerApplication.class, args);
    }

}
