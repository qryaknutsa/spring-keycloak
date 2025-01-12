package org.example.springkeycloak.config;

import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KeycloakAdminConfig {

    @Value("${keycloak.auth-server-url}") // Из application.properties
    private String serverUrl;

    @Value("${keycloak.realm}") // Из application.properties
    private String realm;

    @Value("${keycloak.client-id}") // Из application.properties,  клиент с правами админа
    private String clientId;

    @Value("${keycloak.client-secret}") // Из application.properties
    private String clientSecret;

    @Value("${keycloak.admin.username}")
    private String adminUsername; // credentials админа keycloak

    @Value("${keycloak.admin.password}")
    private String adminPassword;


    @Bean
    public Keycloak keycloakAdmin() {
        return KeycloakBuilder.builder()
                .serverUrl(serverUrl)
                .realm("master") // realm, где у тебя админский клиент
                .clientId("admin-cli") // обычно admin-cli
                .username(adminUsername)
                .password(adminPassword)
                .build();
    }
    @Bean
    public Keycloak keycloakClient() {
        return KeycloakBuilder.builder()
                .serverUrl(serverUrl)
                .realm(realm)
                .clientId(clientId)
                .clientSecret(clientSecret)
                .grantType("client_credentials")
                .build();
    }
}