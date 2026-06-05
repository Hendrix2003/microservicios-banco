package com.devsu.cuenta.infrastructure.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI cuentaOpenAPI() {
        return new OpenAPI().info(new Info()
                .title("Microservicio de Cuentas y Movimientos")
                .description("API para la gestion de cuentas, movimientos y reportes")
                .version("1.0.0"));
    }
}
