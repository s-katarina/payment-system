package org.example.pspbackend.config;

import org.example.pspbackend.auth.MerchantApiKeyAuthenticationFilter;
import org.example.pspbackend.config.jwt.JWTAdminAuthFilter;
import org.example.pspbackend.service.JWTService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private final JWTService jwtService;
    private final MerchantApiKeyAuthenticationFilter merchantApiKeyAuthenticationFilter;

    public SecurityConfig(JWTService jwtService,
                         @Lazy MerchantApiKeyAuthenticationFilter merchantApiKeyAuthenticationFilter) {
        this.jwtService = jwtService;
        this.merchantApiKeyAuthenticationFilter = merchantApiKeyAuthenticationFilter;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:3000", "http://localhost:3001"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setAllowCredentials(true);
        configuration.setExposedHeaders(Arrays.asList("Authorization", "Content-Type"));
        configuration.setMaxAge(3600L);
        
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            .csrf(csrf -> csrf.disable())
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                // Public endpoints - must be before authenticated() to allow access
                .requestMatchers("/api/v1/merchant/register").permitAll() // Merchant registration (public)
                .requestMatchers("/api/v1/auth/login").permitAll() // Admin login endpoint (public)
                .requestMatchers("/api/v1/auth/test").permitAll() // Test endpoint (public, for debugging)
                // All other endpoints require authentication (either ADMIN via JWT or MERCHANT via API key)
                .anyRequest().authenticated()
            )
            // Add custom authentication filters before the default authentication filter
            // JWT admin filter first (so admin can override merchant auth if needed)
            .addFilterBefore(new JWTAdminAuthFilter(jwtService), UsernamePasswordAuthenticationFilter.class)
            .addFilterBefore(merchantApiKeyAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
