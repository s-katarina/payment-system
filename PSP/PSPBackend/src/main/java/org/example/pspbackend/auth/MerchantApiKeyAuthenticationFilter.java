package org.example.pspbackend.auth;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.pspbackend.model.Merchant;
import org.example.pspbackend.repository.MerchantRepository;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

/**
 * Custom authentication filter that validates merchant API key (password) authentication.
 * 
 * 1. Extracts X-Merchant-Id and X-API-Key from request headers
 * 2. Validates merchantId + API key combination to find match 
 * 3. If valid, creates Authentication object and sets it in SecurityContext
 * 4. If invalid, request continues but will be rejected by Spring Security
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class MerchantApiKeyAuthenticationFilter extends OncePerRequestFilter {

    private final MerchantRepository merchantRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, 
                                    FilterChain filterChain) throws ServletException, IOException {
        
        String merchantId = request.getHeader("X-Merchant-Id");
        String apiKey = request.getHeader("X-API-Key");

        // Only process if both headers are present
        if (StringUtils.hasText(merchantId) && StringUtils.hasText(apiKey)) {
            try {
                // Find merchant by ID only (password is hashed, so we can't query by it)
                Merchant merchant = merchantRepository.findById(merchantId)
                        .orElse(null);

                // Verify API key using BCrypt password matching
                if (merchant != null && passwordEncoder.matches(apiKey, merchant.getMerchantPassword())) {
                    // Check if merchant is active
                    if (merchant.getActive() == null || !merchant.getActive()) {
                        log.warn("Inactive merchant attempted to access: {}", merchantId);
                        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                        response.setContentType("application/json");
                        response.setCharacterEncoding("UTF-8");
                        response.getWriter().write("{\"error\":\"Merchant is inactive\",\"message\":\"This merchant account has been deactivated\"}");
                        return;
                    }
                    
                    // Create authentication object
                    // MERCHANT role to distinguish from ADMIN users
                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                            merchant, // Principal (the merchant entity)
                            null,     // Credentials (not needed after authentication)
                            Collections.singletonList(new SimpleGrantedAuthority("ROLE_MERCHANT"))
                    );
                    
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    
                    // Set authentication in SecurityContext
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                    
                    log.debug("Merchant authenticated: {}", merchantId);
                } else {
                    log.warn("Invalid merchant credentials for merchantId: {}", merchantId);
                }
            } catch (Exception e) {
                log.error("Error validating merchant credentials", e);
            }
        }

        // Continue filter chain
        filterChain.doFilter(request, response);
    }
}

