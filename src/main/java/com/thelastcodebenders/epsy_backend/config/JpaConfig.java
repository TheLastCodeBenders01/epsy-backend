package com.thelastcodebenders.epsy_backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

@Configuration
@EnableJpaAuditing
public class JpaConfig {
    
    @Bean
    public AuditorAware<String> auditorProvider() {
        return () -> {
            // Get the current authentication from Spring Security context
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            // Check if user is authenticated and not an anonymous user
            if (authentication == null || !authentication.isAuthenticated() || authentication.getPrincipal().equals("anonymousUser")) {
                // Return "system" or empty if no specific user is logged in or identified
                return Optional.of("system");
            }

            // Return the username of the authenticated user
            // Assuming your UserDetails object has a getUsername() method
            return Optional.ofNullable(authentication.getName());
        };
    }
}