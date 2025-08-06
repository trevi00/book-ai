package com.bookapp.backend.infrastructure.external.ai;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.ResourceAccessException;

import java.util.HashMap;
import java.util.Map;

@Component
public class FastAPIServiceClient implements AIServiceClient {

    private final RestTemplate restTemplate;
    
    @Value("${ai.service.base-url:http://localhost:8000}")
    private String aiServiceBaseUrl;

    public FastAPIServiceClient() {
        this.restTemplate = new RestTemplate();
    }

    @Override
    public String generateAnalysis(AIAnalysisRequest request) {
        try {
            String url = aiServiceBaseUrl + "/api/v1/generate";
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            
            HttpEntity<AIAnalysisRequest> entity = new HttpEntity<>(request, headers);
            
            ResponseEntity<Map> response = restTemplate.exchange(
                url, 
                HttpMethod.POST, 
                entity, 
                Map.class
            );
            
            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                Map<String, Object> responseBody = response.getBody();
                Map<String, Object> data = (Map<String, Object>) responseBody.get("data");
                if (data != null && data.get("content") != null) {
                    return data.get("content").toString();
                }
            }
            
            throw new RuntimeException("AI 서비스로부터 유효한 응답을 받지 못했습니다");
            
        } catch (ResourceAccessException e) {
            throw new RuntimeException("AI 서비스에 연결할 수 없습니다: " + e.getMessage(), e);
        } catch (Exception e) {
            throw new RuntimeException("AI 분석 요청 중 오류가 발생했습니다: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean isHealthy() {
        try {
            String url = aiServiceBaseUrl + "/api/v1/";
            
            ResponseEntity<Map> response = restTemplate.getForEntity(url, Map.class);
            
            return response.getStatusCode() == HttpStatus.OK;
            
        } catch (Exception e) {
            return false;
        }
    }
}