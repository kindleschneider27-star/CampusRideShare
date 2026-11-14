package com.example.campusrideshare.config;

import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(title="Campus Ride Share API",
                version="1.0",
                description="REST API for registering and managing rides"))
@SecurityScheme(
        name="bearerAuth",
        type=SecuritySchemeType.HTTP,
        scheme = "bearer",
        bearerFormat = "JWT"
)
public class OpenApiConfig {
}
