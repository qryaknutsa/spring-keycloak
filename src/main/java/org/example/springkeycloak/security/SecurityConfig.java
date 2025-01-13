package org.example.springkeycloak.security;

import org.keycloak.adapters.authorization.integration.jakarta.ServletPolicyEnforcerFilter;
import org.keycloak.adapters.authorization.spi.ConfigurationResolver;
import org.keycloak.adapters.authorization.spi.HttpRequest;

import org.keycloak.representations.adapters.config.PolicyEnforcerConfig;
import org.keycloak.util.JsonSerialization;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.server.resource.web.authentication.BearerTokenAuthenticationFilter;
import org.springframework.security.web.SecurityFilterChain;

import java.io.IOException;


@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http)
            throws Exception {
        http.csrf(t -> t.disable());
        http.addFilterAfter(createPolicyEnforcerFilter(),
                BearerTokenAuthenticationFilter.class);

        http.sessionManagement(
                t -> t.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        );
        return http.build();
    }

    private ServletPolicyEnforcerFilter createPolicyEnforcerFilter() {
        return new ServletPolicyEnforcerFilter(new ConfigurationResolver() {
            @Override
            public PolicyEnforcerConfig resolve(HttpRequest request) {
                try {
                    return JsonSerialization.
                            readValue(getClass().getResourceAsStream("/policy-enforcer.json"),
                                    PolicyEnforcerConfig.class);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

}


//@Configuration
//@EnableWebSecurity
//@EnableMethodSecurity
//public class SecurityConfig {
//
//    @Autowired
//    private JwtAuthConverter jwtAuthConverter;
//
//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        http
//                // Отключение CSRF, так как мы используем JWT и OAuth2
//                .csrf(csrf -> csrf.disable())
//
//                // Настройка авторизации запросов
//                .authorizeHttpRequests(auth -> auth
//                        .requestMatchers(HttpMethod.GET, "/hello").permitAll()
//                        .requestMatchers(HttpMethod.POST, "/register").permitAll()
//                        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()
//                        .requestMatchers("/", "/login", "/register").permitAll()
//                        .anyRequest().authenticated()
//                )
//
//                // Настройка OAuth2 логина
//                .oauth2Login(oauth2 -> oauth2
//                        .defaultSuccessUrl("/hello")
//                )
//                .oauth2Login(withDefaults())
//
//                // Настройка OAuth2 ресурсного сервера для обработки JWT
//                .oauth2ResourceServer(oauth2Resource -> oauth2Resource
//                        .jwt(jwt -> jwt
//                                .jwtAuthenticationConverter(jwtAuthConverter)
//                        )
//                )
//
//                // Настройка управления сессиями как статeless
//                .sessionManagement(session -> session
//                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
//                )
//
//                // Настройка выхода из системы
//                .logout(logout -> logout
//                        .logoutSuccessUrl("/").permitAll()
//                );
//
//        return http.build();
//    }
//
//    @Bean
//    public DefaultMethodSecurityExpressionHandler methodSecurityExpressionHandler() {
//        DefaultMethodSecurityExpressionHandler handler = new DefaultMethodSecurityExpressionHandler();
//        // Убираем префикс ROLE_ из ролей
//        handler.setDefaultRolePrefix("");
//        return handler;
//    }
//}

//@Configuration
//@EnableWebSecurity
//@EnableMethodSecurity
//public class SecurityConfig {
//
//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        http
//                .authorizeHttpRequests(auth -> auth
//                        .requestMatchers("/", "/login", "/register").permitAll()
//                        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()
//                        .anyRequest().authenticated()
//                )
//                .oauth2Login(oauth2 -> oauth2
//                        .defaultSuccessUrl("/hello")  // Перенаправляем на главную после успешного входа
//                )
//                .logout(logout -> logout
//                        .logoutSuccessUrl("/").permitAll()
//                )
//                .oauth2ResourceServer(oauth2 -> oauth2
//                        .jwt(jwt -> jwt
//                                .jwtAuthenticationConverter(jwtAuthenticationConverter())
//                        )
//                );
//
//        return http.build();
//    }
//
//    private Converter<Jwt, ? extends AbstractAuthenticationToken> jwtAuthenticationConverter() {
//        JwtAuthenticationConverter jwtConverter = new JwtAuthenticationConverter();
//        jwtConverter.setJwtGrantedAuthoritiesConverter(new KeycloakRealmRoleConverter());
//        return jwtConverter;
//    }
//}

//@Configuration
//@EnableWebSecurity
//@EnableMethodSecurity
//public class SecurityConfig {
//
//    @Autowired
//    JwtAuthConverter jwtAuthConverter;
//
//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        http.csrf(AbstractHttpConfigurer::disable);
//        http.authorizeHttpRequests(auth -> {
//            auth
//                    .requestMatchers(HttpMethod.GET, "/hello").permitAll()
//                    .requestMatchers(HttpMethod.POST, "/register").permitAll() // Добавили разрешение
//                    .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()
//                    .anyRequest().authenticated();
//        });
//        http.oauth2ResourceServer(t -> {
//            t.jwt(configurer -> configurer.jwtAuthenticationConverter(jwtAuthConverter));
////            t.opaqueToken(Customizer.withDefaults());
//        });
//        http.sessionManagement(
//                t -> t.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
//        );
//        return http.build();
//    }
//
//
//    @Bean
//    public DefaultMethodSecurityExpressionHandler msecurity() {
//        DefaultMethodSecurityExpressionHandler defaultMethodSecurityExpressionHandler =
//                new DefaultMethodSecurityExpressionHandler();
//        defaultMethodSecurityExpressionHandler.setDefaultRolePrefix("");
//        return defaultMethodSecurityExpressionHandler;
//    }
//}
