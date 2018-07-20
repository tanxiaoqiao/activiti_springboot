package com.example.act;

import org.activiti.spring.boot.SecurityAutoConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
public class ActApplication {

    public static void main(String[] args) {
        SpringApplication.run(ActApplication.class, args);

    }
}
