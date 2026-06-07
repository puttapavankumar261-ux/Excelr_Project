package com.emp.manag.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
@Configuration
@Validated
@ConfigurationProperties(prefix = "app.api")
public class ApiConfig {

    @NotBlank(message = "API Title cannot be blank")
    private String title;

    @NotBlank(message = "API Version cannot be blank")
    private String version;

    @Min(value = 10, message = "Max connections must be at least 10")
    private int maxConnections;

    @NotEmpty(message = "At least one feature must be enabled")
    private List<String> enabledFeatures;

    @Autowired
    private CorsProperties cors;
}
