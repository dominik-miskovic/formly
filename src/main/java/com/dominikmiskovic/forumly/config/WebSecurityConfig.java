package com.dominikmiskovic.forumly.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authorize -> authorize
                        // Allow access to the H2 console
                        .requestMatchers(AntPathRequestMatcher.antMatcher("/h2-console/**")).permitAll()
                        // Allow access to the login page and other static resources
                        .requestMatchers("/", "/login", "/register", "/css/**", "/js/**", "/images/**").permitAll()
                        // Require authentication for all other requests
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login") // Specify your custom login page
                        .permitAll()
                )
                .logout(LogoutConfigurer::permitAll
                )
                .csrf(csrf -> csrf
                        // Disable CSRF for the H2 console
                        .ignoringRequestMatchers(AntPathRequestMatcher.antMatcher("/h2-console/**"))
                )
                // Allow frames for the H2 console
                .headers(headers -> headers.frameOptions().disable()); // This is important for H2 console to work in an iframe

        return http.build();
    }
}
