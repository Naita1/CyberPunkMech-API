package com.cyberpunk.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("CyberpunkMech API")
                        .description("REST API for managing Players and Mechs with polymorphic support.")
                        .version("1.0.0"));
    }
}
