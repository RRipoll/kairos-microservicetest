package com.testjava.priceservice.infrastructure.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfiguration {

    @Value("${server.port:8080}")
    private String serverPort;

    @Bean
    public OpenAPI openAPI() {
        Server server = new Server();
        server.setUrl("http://localhost:" + serverPort);
        server.setDescription("Development server");

        Contact contact = new Contact();
        contact.setName("Price Service Team");
        contact.setEmail("priceservice@testjava.com");

        License license = new License()
                .name("MIT License")
                .url("https://opensource.org/licenses/MIT");

        Info info = new Info()
                .title("Price Service API")
                .version("1.0.0")
                .contact(contact)
                .description("REST API for e-commerce price consultation with hexagonal architecture and DDD principles")
                .license(license)
                .summary("Price consultation service that manages product pricing with time-based validity and priority rules");

        return new OpenAPI()
                .info(info)
                .servers(List.of(server));
    }
}