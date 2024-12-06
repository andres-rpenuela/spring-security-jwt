package org.tokio.spring.securityjwt.configuration;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.tokio.spring.securityjwt.core.entrypoint.JwtAuthenticationEntryPoint;
import org.tokio.spring.securityjwt.core.filter.JwtRequestFilter;

@Configuration
@RequiredArgsConstructor
@DependsOn({"authenticationProvider","jwtAuthenticationEntryPoint","jwtRequestFilter"})
public class SecurityConfig {

    private final AuthenticationProvider authenticationProvider;
    private final JwtRequestFilter jwtRequestFilter;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    @Bean("securityFilterChain")
    @Order(1)
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // custom rest api: SIN ESTADO
        http.sessionManagement(httpSecuritySessionManagementConfigurer -> httpSecuritySessionManagementConfigurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        http
                .csrf(csrf -> csrf.disable()) // Desactiva CSRF para simplificar el acceso a la consola
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(new AntPathRequestMatcher("/h2-console/**")).permitAll() // Permite acceso a la consola de H2 sin autenticación
                        .requestMatchers("/swagger-ui/**","/v3/api-docs").permitAll()
                        .requestMatchers("/api/auth/**").permitAll()
                        .anyRequest().authenticated() // Protege otras rutas
                )
                .headers(headers -> headers
                        .frameOptions(frameOptions -> frameOptions.disable()) // Permite el uso de frames para H2
                );
        // login con el formulario de spring
        http.formLogin(Customizer.withDefaults());

        // login con auth basic
        //http.httpBasic(Customizer.withDefaults()); // Basic Auth, allow username-password in header field authentication
        //http.httpBasic(basic -> basic.authenticationEntryPoint(jwtAuthenticationEntryPoint));

        // jwt token
        http.authenticationProvider(authenticationProvider);
        http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
        http.exceptionHandling(httpSecurityExceptionHandlingConfigurer -> httpSecurityExceptionHandlingConfigurer.authenticationEntryPoint(jwtAuthenticationEntryPoint));


        return http.build();
    }

}
