package com.example.ProjectManagementSystem;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;

@SpringBootApplication
@EnableMethodSecurity
@ComponentScan(basePackages = {
    "com.example.ProjectManagementSystem.controller",
    "com.example.ProjectManagementSystem.service",
    "com.example.ProjectManagementSystem.config",
    "com.example.ProjectManagementSystem.repository"
})
public class ProjectManagementSystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProjectManagementSystemApplication.class, args);
	}

}
