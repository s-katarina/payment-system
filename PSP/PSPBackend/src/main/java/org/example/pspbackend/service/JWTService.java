package org.example.pspbackend.service;

import jakarta.servlet.http.HttpServletRequest;

public interface JWTService {
    String getJWTFromRequest(HttpServletRequest request);
    boolean validateToken(String token);
    String getUsernameFromJWT(String token);
    String getRoleFromJWT(String token);
    String generateToken(String username, String role);
}

