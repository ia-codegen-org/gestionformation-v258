package com.gestionformation.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.Components;
import org.springdoc.core.customizers.OperationCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class OpenApiConfig implements WebMvcConfigurer {

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addRedirectViewController(
                "/swagger-ui.html",
                "/swagger-ui/index.html"
        );
    }

    @Bean
    public OpenAPI customOpenAPI() {

        final String securitySchemeName = "bearerAuth";

        return new OpenAPI()
                .info(new Info()
                        .title("GestionFormation API")
                        .description("Documentation de l'API REST générée automatiquement pour le projet GestionFormation")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("IACodeGen")
                                .email("contact@iacodegen.com")
                                .url("https://iacodegen.com"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("https://www.apache.org/licenses/LICENSE-2.0")))

                .addSecurityItem(new SecurityRequirement()
                        .addList(securitySchemeName))
                .components(new Components()
                        .addSecuritySchemes(securitySchemeName,
                                new SecurityScheme()
                                        .name(securitySchemeName)
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")
                                        .description("Entrez votre token JWT")
                        )
                );
    }

    @Bean
    public OperationCustomizer customizeAuthOperations() {
        return (Operation operation, HandlerMethod handlerMethod) -> {
            String className  = handlerMethod.getBeanType().getSimpleName();
            String methodName = handlerMethod.getMethod().getName();
            // ✅ Seulement register et login → pas de token
            if (className.equals("AuthController")
                    && (methodName.equals("register")
                        || methodName.equals("login"))) {
                operation.setSecurity(java.util.Collections.emptyList());
            }
            return operation;
        };
    }
    }
