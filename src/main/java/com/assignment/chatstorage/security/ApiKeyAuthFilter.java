package com.assignment.chatstorage.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import com.assignment.chatstorage.dto.ApiResponse;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Arrays;

@Component
public class ApiKeyAuthFilter extends OncePerRequestFilter {

    @Value("${API_KEY:}")
    private String configuredApiKey;

    private static final String HEADER_NAME = "X-API-KEY";
    private final ObjectMapper objectMapper = new ObjectMapper();

    private static final List<String> WHITELIST = Arrays.asList(
            "/v3/api-docs",
            "/swagger-ui",
            "/swagger-ui.html",
            "/actuator/health"
    );

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String path = request.getRequestURI();
        System.out.println("ApiKeyAuthFilter request path: " + path);
        if (WHITELIST.stream().anyMatch(path::startsWith)) {
            filterChain.doFilter(request, response);
            return;
        }

        // SKIP API KEY CHECK IF MISSING
        if (configuredApiKey == null || configuredApiKey.isEmpty()) {
            filterChain.doFilter(request, response);
            return;
        }

        String incomingApiKey = request.getHeader(HEADER_NAME);

        if (incomingApiKey == null) {
            sendErrorResponse(response, "API key required");
            return;
        }

        if (!incomingApiKey.equals(configuredApiKey)) {
            sendErrorResponse(response, "Invalid API key");
            return;
        }

        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken("api-user", null, Collections.emptyList());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        filterChain.doFilter(request, response);
    }

    private void sendErrorResponse(HttpServletResponse response, String message) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        ApiResponse<Object> errorResponse = ApiResponse.error(401, message);
        objectMapper.writeValue(response.getOutputStream(), errorResponse);
    }
}