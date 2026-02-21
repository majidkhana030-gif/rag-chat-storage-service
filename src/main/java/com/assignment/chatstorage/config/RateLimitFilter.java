package com.assignment.chatstorage.config;

import com.assignment.chatstorage.constant.AppConstants;
import com.assignment.chatstorage.dto.ApiResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
public class RateLimitFilter extends OncePerRequestFilter {

    private final Map<String, Bucket> buckets = new ConcurrentHashMap<>();
    private final ObjectMapper objectMapper = new ObjectMapper();

    private Bucket createNewBucket() {
        Bandwidth limit = Bandwidth.simple(50, Duration.ofMinutes(1));
        return Bucket.builder().addLimit(limit).build();
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String ip = request.getRemoteAddr();
        Bucket bucket = buckets.computeIfAbsent(ip, k -> createNewBucket());

        if (bucket.tryConsume(1)) {
            filterChain.doFilter(request, response);
        } else {

            String traceId = MDC.get("traceId");

            log.warn("Rate limit exceeded. traceId={}, ip={}", traceId, ip);

            response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
            response.setContentType("application/json");

            ApiResponse<Void> apiResponse =
                    ApiResponse.error(
                            HttpStatus.TOO_MANY_REQUESTS.value(),
                            AppConstants.TOO_MANY_REQUEST
                    );

            String json = objectMapper.writeValueAsString(apiResponse);
            response.getWriter().write(json);
        }
    }
}