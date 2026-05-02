package com.example.ProjectManagementSystem.swagger;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Project-Management-System-APIs",
                version = "1.0",
                description = "Backend documentation for the Project Management System.",
                contact = @Contact(
                        name = "Sarfaraz Essa",
                        email = "sarfarazessa18@gmail.com",
                        url = "https://github.com/sarfarazessa" 
                )
        ),
        security = @SecurityRequirement(name = "bearerAuth")
)
@SecurityScheme(
        name = "bearerAuth",
        type = SecuritySchemeType.HTTP,
        scheme = "bearer",
        bearerFormat = "JWT"
)

public class SwaggerConfig {


    public class OpenApiConfig {
    }

}
