package org.example.pspbackend.config.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.pspbackend.service.JWTService;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

/**
 * JWT authentication filter for admin users.
 * Validates JWT tokens from Authorization header and grants ADMIN role if token is valid.
 */
@Slf4j
@RequiredArgsConstructor
public class JWTAdminAuthFilter extends OncePerRequestFilter {

    private final JWTService jwtService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, 
                                    FilterChain filterChain) throws ServletException, IOException {
        
        String token = jwtService.getJWTFromRequest(request);
        
        if (StringUtils.hasText(token) && jwtService.validateToken(token)) {
            try {
                String username = jwtService.getUsernameFromJWT(token);
                String role = jwtService.getRoleFromJWT(token);
                
                // Only process if role is ADMIN
                if (username != null && "ADMIN".equalsIgnoreCase(role)) {
                    // Create authentication object with ADMIN role
                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                            username, // Principal (username)
                            null,     // Credentials (not needed after authentication)
                            Collections.singletonList(new SimpleGrantedAuthority("ROLE_ADMIN"))
                    );
                    
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    
                    // Set authentication in SecurityContext
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                    
                    log.debug("Admin authenticated via JWT: {}", username);
                } else {
                    log.debug("JWT token present but not for ADMIN role. Role: {}", role);
                }
            } catch (Exception e) {
                log.warn("Error processing JWT token for admin authentication", e);
                SecurityContextHolder.clearContext();
            }
        }

        // Continue filter chain
        filterChain.doFilter(request, response);
    }
}

