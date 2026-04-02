package com.riya.expense_tracker.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableMethodSecurity // Allows @PreAuthorize to work in your controllers
public class SecurityConfig {

    private final JwtFilter jwtFilter;

    public SecurityConfig(JwtFilter jwtFilter) {
        this.jwtFilter = jwtFilter;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        // 1. Public Access
                        .requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers("/h2-console/**").permitAll()

                        // 2. Role-Based Access Control for Transactions
                        // Requirement: Viewers, Analysts, and Admins can all VIEW (GET) transactions
                        .requestMatchers(HttpMethod.GET, "/api/transactions/**").hasAnyRole("VIEWER", "ANALYST", "ADMIN")

                        // Requirement: Only ADMIN can manage (POST, PUT, DELETE) transactions
                        .requestMatchers(HttpMethod.POST, "/api/transactions/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/transactions/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/transactions/**").hasRole("ADMIN")

                        // 3. Dashboard Analytics
                        // Requirement: Analysts and Admins can access insights
                        .requestMatchers("/api/dashboard/**").hasAnyRole("ANALYST", "ADMIN")

                        // 4. User Management
                        // Requirement: Only Admin can manage other users
                        .requestMatchers("/api/users/**").hasRole("ADMIN")

                        .requestMatchers("/api/auth/**", "/h2-console/**", "/swagger-ui/**", "/v3/api-docs/**").permitAll()

                        .anyRequest().authenticated()
                )
                // Frame options must be disabled specifically for the H2 Console to load in the browser
                .headers(headers -> headers.frameOptions(frame -> frame.disable()))
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}