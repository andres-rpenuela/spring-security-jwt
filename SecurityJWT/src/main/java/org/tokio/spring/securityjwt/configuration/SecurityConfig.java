package org.tokio.spring.securityjwt.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    @Order(1)
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // Desactiva CSRF para simplificar el acceso a la consola
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/h2-console/**","/actuator/**").permitAll() // Permite acceso a la consola de H2 sin autenticación
                        .anyRequest().authenticated() // Protege otras rutas
                )
                .headers(headers -> headers
                        .frameOptions(frameOptions -> frameOptions.disable()) // Permite el uso de frames para H2
                );

        return http.build();
    }
}