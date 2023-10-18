package com.assignment.ekart.ekartms.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    private static final String APP_TITLE = "ekartApp";
    private static final String APP_VERSION = "1.0";
    private static final String APP_DESC = "E-kart application for kart demo";
    private static final String SCHEME_NAME = "bearer";
    private static final String SECURITY_SCHEME_NAME = "bearerAuth";
    private static final String SCHEME_DECRIPTION = "Provide JWT token";
    private static final String BEARER_FORMAT = "JWT";

    @Bean
    public OpenAPI customOpenAPI(){
        return new OpenAPI().info(
                new Info().title(APP_TITLE)
                        .version(APP_VERSION)
                        .description(APP_DESC)
                )
                .addSecurityItem(new SecurityRequirement()
                        .addList(SECURITY_SCHEME_NAME)
                )
                .components(new Components()
                        .addSecuritySchemes(SECURITY_SCHEME_NAME,new SecurityScheme()
                        .name(SECURITY_SCHEME_NAME)
                        .type(SecurityScheme.Type.HTTP)
                        .description(SCHEME_DECRIPTION)
                        .bearerFormat(BEARER_FORMAT))
                );

    }
}
