package com.webapp.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {
    private static final Logger logger = LoggerFactory.getLogger(SecurityConfig.class);

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        logger.debug("Configuring SecurityFilterChain");
        http
            .authorizeHttpRequests(authorizeRequests -> {
                logger.debug("Configuring request authorization");
                authorizeRequests
                    .requestMatchers("/", "/login**", "/logout**","/webjars/**").permitAll()
                    .anyRequest().authenticated();
                logger.info("Public access configured for '/', '/login**', '/webjars/**'. All other requests require authentication.");
            })
            .oauth2Login(oauth2Login -> {
                logger.debug("Configuring OAuth2 login");
                oauth2Login
                    .loginPage("/login")
                    .defaultSuccessUrl("/dashboard", true);
                logger.info("OAuth2 login configured with custom login page and success URL set to '/dashboard'");
            })
            .logout(logout -> {
                logger.debug("Configuring logout behavior");
                logout
                    .logoutUrl("/logout")
                    .logoutSuccessUrl("/")
                    .invalidateHttpSession(true)
                    .deleteCookies("JSESSIONID")
                    .addLogoutHandler((request, response, authentication) -> {
                        logger.info("Logout requested for user: " + (authentication != null ? authentication.getName() : "unknown"));
                    });
                logger.info("Logout configured with custom URL, success URL, and session invalidation");
            });

        logger.debug("SecurityFilterChain configuration completed");
        return http.build();
    }
}