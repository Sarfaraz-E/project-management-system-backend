package com.example.ProjectManagementSystem;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;

@SpringBootApplication
@EnableMethodSecurity
public class ProjectManagementSystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProjectManagementSystemApplication.class, args);
	}

}
