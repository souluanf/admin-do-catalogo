package dev.luanfernandes.admin.catalogo.infrastructure.configuration;

import static java.util.stream.Stream.of;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@OpenAPIDefinition(
        info =
                @Info(
                        title = "admin do catalogo API",
                        description = "API para administração do catalogo",
                        version = "0.0.1",
                        contact =
                                @Contact(
                                        name = "Luan Fernandes",
                                        email = "souluanf@icloud.com",
                                        url = "https://luanfernandes.dev")))
@Configuration
public class SwaggerConfig {

    @Value("${swagger-servers-urls}")
    private String[] swaggerServersUrls;

    @Bean
    public OpenAPI customOpenAPI() {
        OpenAPI openApi = new OpenAPI();
        of(swaggerServersUrls).forEach(url -> openApi.addServersItem(new Server().url(url)));
        return openApi;
    }
}
