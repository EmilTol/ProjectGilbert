package com.example.projectgilbert;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class ProjectGilbertApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProjectGilbertApplication.class, args);
    }

}
