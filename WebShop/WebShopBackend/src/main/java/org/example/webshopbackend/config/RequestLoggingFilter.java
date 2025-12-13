package org.example.webshopbackend.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@Order(1)
public class RequestLoggingFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        System.out.println("📥 Incoming Request: " + request.getMethod() + " " + request.getRequestURI() + " from " + request.getRemoteAddr());
        System.out.println("📥 Headers: " + java.util.Collections.list(request.getHeaderNames()));
        filterChain.doFilter(request, response);
        System.out.println("📤 Response Status: " + response.getStatus());
    }
}



