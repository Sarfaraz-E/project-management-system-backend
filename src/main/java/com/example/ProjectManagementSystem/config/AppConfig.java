package com.example.ProjectManagementSystem.config;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.Collections;

@Configuration
@EnableWebSecurity
public class AppConfig {

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http.sessionManagement(management -> management.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authorize -> authorize
                        // 1. Allow Pre-flight OPTIONS requests (Crucial for live frontend)
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

                        // 2. Swagger & API Docs
                        .requestMatchers(
                                "/v3/api-docs/**",
                                "/swagger-ui/**",
                                "/swagger-ui.html",
                                "/swagger-resources/**",
                                "/webjars/**"
                        ).permitAll()

                        // 3. Auth Endpoints
                        .requestMatchers("/auth/**").permitAll()

                        // 4. Internal Error path (Prevents 403 on internal failures)
                        .requestMatchers("/error").permitAll()

                        // 5. Secure all other /api/ endpoints
                        .requestMatchers("/api/**").authenticated()

                        // 6. Any other request (like root /) requires authentication
                        .anyRequest().authenticated()
                )
                .addFilterBefore(new jwtTokenValidator(), BasicAuthenticationFilter.class)
                .csrf(csrf -> csrf.disable())
                .cors(cors -> cors.configurationSource(corsConfigurationSource()));

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration cfg = new CorsConfiguration();

        // Add your production frontend URL here when you deploy it
        cfg.setAllowedOrigins(Arrays.asList(
                "http://localhost:3000",
                "http://localhost:5173",
                "http://localhost:4200"
        ));

        cfg.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));
        cfg.setAllowCredentials(true);
        cfg.setAllowedHeaders(Collections.singletonList("*"));
        cfg.setExposedHeaders(Arrays.asList("Authorization"));
        cfg.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", cfg);
        return source;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}