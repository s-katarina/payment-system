package org.example.webshopbackend.config.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.example.webshopbackend.service.JWTServiceImpl;
import org.example.webshopbackend.service.UserDetailsServiceImpl;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
public class JWTAuthFilter extends OncePerRequestFilter {

    private final JWTServiceImpl jwtService;
    private final UserDetailsServiceImpl userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String path = request.getServletPath();
        String method = request.getMethod();
        
        System.out.println("🔍 JWT Filter - Request: " + method + " " + path);
        
        // Skip JWT check for auth endpoints and OPTIONS requests (CORS preflight)
        if (path.startsWith("/api/v1/auth") || "OPTIONS".equalsIgnoreCase(method)) {
            System.out.println("✅ JWT Filter - Skipping JWT check for: " + path);
            filterChain.doFilter(request, response);
            return;
        }

        String token = jwtService.getJWTFromRequest(request);
        if (StringUtils.hasText(token) && jwtService.validateToken(token)) {
            try {
                String username = jwtService.getUsernameFromJWT(token);
                if (username != null) {
                    UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                    System.out.println("🔐 Authenticated user: " + username);
                    System.out.println("🔐 User authorities: " + userDetails.getAuthorities());
                    UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities());
                    authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                }
            } catch (UsernameNotFoundException e) {
                // User not found - let Spring Security handle the authentication failure
                SecurityContextHolder.clearContext();
            } catch (Exception e) {
                // Invalid token or other error - clear context and let Spring Security handle it
                SecurityContextHolder.clearContext();
            }
        }
        filterChain.doFilter(request, response);
    }


}
