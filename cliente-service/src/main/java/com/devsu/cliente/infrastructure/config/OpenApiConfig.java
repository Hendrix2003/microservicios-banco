package com.devsu.cliente.infrastructure.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI clienteOpenAPI() {
        return new OpenAPI().info(new Info()
                .title("Microservicio de Clientes")
                .description("API para la gestion de clientes y personas")
                .version("1.0.0"));
    }
}
