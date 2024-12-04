package org.tokio.spring.securityjwt.configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfiguration {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .addSecurityItem( securityRequirement() )
                .components( components() )
                .info( info() );
    }

    private static  SecurityRequirement securityRequirement() {
        return new SecurityRequirement().addList("bearerAuth");
    }

    private static SecurityScheme securityScheme() {
        return new SecurityScheme().name("Bearer Token")
                .type(SecurityScheme.Type.HTTP)
                .scheme("bearer")
                .bearerFormat("JWT");
    }

    private static Components components() {
        return new Components()
                .addSecuritySchemes("bearerAuth", securityScheme() );
    }

    private static Info info() {
        return new Info()
                .title("Shop Spring Boot 3 API -------")
                .version("0.11")
                .description("Sample app Spring Boot 3 with Swagger")
                .termsOfService("http://swagger.io/terms/")
                .license(new License().name("Apache 2.0").url("http://springdoc.org"));
    }
}
