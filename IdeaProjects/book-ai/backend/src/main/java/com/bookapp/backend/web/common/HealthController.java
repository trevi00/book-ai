package com.bookapp.backend.web.common;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequestMapping("")
public class HealthController {

    @GetMapping("/health")
    public ApiResponse<Map<String, Object>> health() {
        Map<String, Object> healthInfo = Map.of(
                "status", "UP",
                "timestamp", LocalDateTime.now(),
                "service", "book-ai-backend"
        );
        return ApiResponse.success(healthInfo);
    }

    @GetMapping("/api/health")
    public ApiResponse<Map<String, Object>> apiHealth() {
        Map<String, Object> healthInfo = Map.of(
                "status", "UP",
                "timestamp", LocalDateTime.now(),
                "service", "book-ai-backend"
        );
        return ApiResponse.success(healthInfo);
    }
}