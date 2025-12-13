package org.example.pspbackend.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class JWTServiceImpl implements JWTService {

    private SecretKey secret;

    @Value("${JWT_SECRET:your-super-secret-jwt-key-that-is-at-least-64-characters-long-for-hs512-algorithm-security}")
    private String jwtSecret;

    @PostConstruct
    public void init() {
        // Ensure secret is at least 64 characters for HS512
        this.secret = Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
    }

    @Override
    public String getJWTFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    @Override
    public boolean validateToken(String token) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(secret)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            // Check expiration
            if (claims.getExpiration() != null && claims.getExpiration().before(new java.util.Date())) {
                return false;
            }

            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    @Override
    public String getUsernameFromJWT(String token) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(secret)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            return claims.getSubject();
        } catch (Exception e) {
            throw new AuthenticationCredentialsNotFoundException("Invalid JWT token: " + e.getMessage());
        }
    }

    @Override
    public String getRoleFromJWT(String token) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(secret)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            
            // Try to get role from claims (could be "roles" or "role" claim)
            Object roleClaim = claims.get("roles");
            if (roleClaim == null) {
                roleClaim = claims.get("role");
            }
            
            if (roleClaim != null) {
                return roleClaim.toString();
            }
            
            return null;
        } catch (Exception e) {
            throw new AuthenticationCredentialsNotFoundException("Invalid JWT token: " + e.getMessage());
        }
    }

    @Override
    public String generateToken(String username, String role) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("role", role);
        
        long expirationTime = 86400000; // 24 hours in milliseconds
        Date expirationDate = new Date(System.currentTimeMillis() + expirationTime);
        
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(expirationDate)
                .signWith(secret)
                .compact();
    }
}

