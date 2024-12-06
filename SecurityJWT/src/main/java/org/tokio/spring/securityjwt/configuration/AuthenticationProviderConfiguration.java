package org.tokio.spring.securityjwt.configuration;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@DependsOn({"basicUserDetailsService","passwordEncoder"})
@RequiredArgsConstructor
public class AuthenticationProviderConfiguration {

    // The userDetailsService() defines how to retrieve the user using the UserRepository that is injected.
    @Qualifier("basicUserDetailsService")
    private final UserDetailsService userDetailsService;

    // The authenticationProvider() sets the new strategy to perform the authentication.
    private final PasswordEncoder passwordEncoder;

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();

        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder);

        return authProvider;
    }
}
