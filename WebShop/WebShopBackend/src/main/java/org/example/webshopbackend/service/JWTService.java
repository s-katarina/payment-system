package org.example.webshopbackend.service;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.userdetails.UserDetails;

public interface JWTService {
    String generateToken();
    String getUsernameFromJWT(String token);
    boolean validateToken(String token);

    UserDetails getAuthenticatedUser();

    String getJWTFromRequest(HttpServletRequest request);
}
