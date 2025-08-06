package com.bookapp.backend.integration.fastapi;

import com.bookapp.backend.infrastructure.external.ai.AIAnalysisRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * FastAPI 통합 테스트를 위한 기본 클래스
 * MockWebServer를 사용하여 FastAPI 서버를 시뮬레이션
 */
@SpringBootTest
@ActiveProfiles("test")
@Transactional
public abstract class FastAPIIntegrationTestBase {

    protected static MockWebServer mockFastAPIServer;

    @Autowired
    protected ObjectMapper objectMapper;

    @BeforeAll
    static void setUpClass() {
        try {
            mockFastAPIServer = new MockWebServer();
            mockFastAPIServer.start();
        } catch (IOException e) {
            throw new RuntimeException("Failed to start MockWebServer", e);
        }
    }

    @AfterAll
    static void tearDownClass() throws IOException {
        if (mockFastAPIServer != null) {
            mockFastAPIServer.shutdown();
        }
    }

    @BeforeEach
    void setUpTest() {
        // Clear any pending requests from previous tests
        try {
            while (mockFastAPIServer.getRequestCount() > 0) {
                mockFastAPIServer.takeRequest(10, TimeUnit.MILLISECONDS);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("ai.service.base-url", () -> "http://localhost:" + mockFastAPIServer.getPort());
    }

    /**
     * FastAPI 건강 상태 성공 응답 모킹
     */
    protected void mockHealthCheckSuccess() {
        Map<String, Object> healthResponse = Map.of(
                "status", "healthy",
                "timestamp", System.currentTimeMillis(),
                "service", "book-ai-fastapi"
        );

        mockFastAPIServer.enqueue(new MockResponse()
                .setResponseCode(200)
                .setHeader("Content-Type", "application/json")
                .setBody(toJson(healthResponse)));
    }

    /**
     * FastAPI 건강 상태 실패 응답 모킹
     */
    protected void mockHealthCheckFailure() {
        mockFastAPIServer.enqueue(new MockResponse()
                .setResponseCode(503)
                .setHeader("Content-Type", "application/json")
                .setBody("{\"error\": \"Service Unavailable\"}"));
    }

    /**
     * AI 분석 성공 응답 모킹
     */
    protected void mockAnalysisSuccess(String content) {
        Map<String, Object> analysisResponse = new HashMap<>();
        analysisResponse.put("success", true);
        analysisResponse.put("message", "분석이 성공적으로 완료되었습니다");
        
        Map<String, Object> data = new HashMap<>();
        data.put("content", content);
        data.put("analysis_type", "SUMMARY");
        data.put("confidence", 0.95);
        data.put("processing_time_ms", 1250);
        
        analysisResponse.put("data", data);
        analysisResponse.put("timestamp", System.currentTimeMillis());

        mockFastAPIServer.enqueue(new MockResponse()
                .setResponseCode(200)
                .setHeader("Content-Type", "application/json")
                .setBody(toJson(analysisResponse)));
    }

    /**
     * AI 분석 실패 응답 모킹 (400 Bad Request)
     */
    protected void mockAnalysisBadRequest(String errorMessage) {
        Map<String, Object> errorResponse = Map.of(
                "success", false,
                "message", errorMessage,
                "error_code", "INVALID_REQUEST",
                "timestamp", System.currentTimeMillis()
        );

        mockFastAPIServer.enqueue(new MockResponse()
                .setResponseCode(400)
                .setHeader("Content-Type", "application/json")
                .setBody(toJson(errorResponse)));
    }

    /**
     * AI 분석 실패 응답 모킹 (500 Internal Server Error)
     */
    protected void mockAnalysisInternalError() {
        Map<String, Object> errorResponse = Map.of(
                "success", false,
                "message", "내부 서버 오류가 발생했습니다",
                "error_code", "INTERNAL_ERROR",
                "timestamp", System.currentTimeMillis()
        );

        mockFastAPIServer.enqueue(new MockResponse()
                .setResponseCode(500)
                .setHeader("Content-Type", "application/json")
                .setBody(toJson(errorResponse)));
    }

    /**
     * AI 분석 타임아웃 응답 모킹
     */
    protected void mockAnalysisTimeout() {
        mockFastAPIServer.enqueue(new MockResponse()
                .setResponseCode(408)
                .setHeader("Content-Type", "application/json")
                .setBody("{\"error\": \"Request Timeout\"}")
                .setBodyDelay(30, TimeUnit.SECONDS)); // 30초 지연
    }

    /**
     * 네트워크 연결 오류 시뮬레이션
     */
    protected void mockNetworkError() {
        mockFastAPIServer.enqueue(new MockResponse()
                .setSocketPolicy(okhttp3.mockwebserver.SocketPolicy.DISCONNECT_DURING_REQUEST_BODY));
    }

    /**
     * 느린 응답 시뮬레이션
     */
    protected void mockSlowResponse(String content, long delaySeconds) {
        Map<String, Object> analysisResponse = new HashMap<>();
        analysisResponse.put("success", true);
        analysisResponse.put("message", "분석이 성공적으로 완료되었습니다");
        
        Map<String, Object> data = new HashMap<>();
        data.put("content", content);
        analysisResponse.put("data", data);

        mockFastAPIServer.enqueue(new MockResponse()
                .setResponseCode(200)
                .setHeader("Content-Type", "application/json")
                .setBody(toJson(analysisResponse))
                .setBodyDelay(delaySeconds, TimeUnit.SECONDS));
    }

    /**
     * 다양한 AI 분석 타입별 성공 응답 모킹
     */
    protected void mockAnalysisSuccessByType(String analysisType, String content) {
        Map<String, Object> analysisResponse = new HashMap<>();
        analysisResponse.put("success", true);
        analysisResponse.put("message", "분석이 성공적으로 완료되었습니다");
        
        Map<String, Object> data = new HashMap<>();
        data.put("content", content);
        data.put("analysis_type", analysisType);
        
        // 분석 타입별 특화된 응답 데이터
        switch (analysisType) {
            case "SUMMARY":
                data.put("word_count", 150);
                data.put("readability_score", 8.5);
                break;
            case "KEYWORDS":
                data.put("keyword_count", 12);
                data.put("relevance_score", 0.92);
                break;
            case "SENTIMENT":
                data.put("sentiment_score", 0.75);
                data.put("emotion", "positive");
                break;
            case "RECOMMENDATION":
                data.put("confidence_score", 0.88);
                data.put("category", "personal_growth");
                break;
        }
        
        analysisResponse.put("data", data);
        analysisResponse.put("timestamp", System.currentTimeMillis());

        mockFastAPIServer.enqueue(new MockResponse()
                .setResponseCode(200)
                .setHeader("Content-Type", "application/json")
                .setBody(toJson(analysisResponse)));
    }

    /**
     * FastAPI 서버 점검 중 응답 모킹
     */
    protected void mockMaintenanceMode() {
        Map<String, Object> maintenanceResponse = Map.of(
                "success", false,
                "message", "서버가 점검 중입니다. 잠시 후 다시 시도해주세요.",
                "error_code", "MAINTENANCE_MODE",
                "estimated_completion", "2024-12-25T15:00:00Z"
        );

        mockFastAPIServer.enqueue(new MockResponse()
                .setResponseCode(503)
                .setHeader("Content-Type", "application/json")
                .setHeader("Retry-After", "3600")
                .setBody(toJson(maintenanceResponse)));
    }

    /**
     * 요청 검증을 위한 헬퍼 메서드
     */
    protected RecordedRequest takeRequest() throws InterruptedException {
        return mockFastAPIServer.takeRequest(5, TimeUnit.SECONDS);
    }

    /**
     * AI 분석 요청 검증 헬퍼 메서드
     */
    protected void verifyAnalysisRequest(RecordedRequest request, AIAnalysisRequest expectedRequest) throws Exception {
        // HTTP 메서드 검증
        assert request.getMethod().equals("POST");
        
        // URL 경로 검증
        assert request.getPath().equals("/api/v1/analysis/generate");
        
        // Content-Type 헤더 검증
        assert request.getHeader("Content-Type").equals("application/json");
        
        // 요청 본문 검증
        String requestBody = request.getBody().readUtf8();
        AIAnalysisRequest actualRequest = objectMapper.readValue(requestBody, AIAnalysisRequest.class);
        
        assert actualRequest.getUserId().equals(expectedRequest.getUserId());
        assert actualRequest.getBookId().equals(expectedRequest.getBookId());
        assert actualRequest.getBookTitle().equals(expectedRequest.getBookTitle());
        assert actualRequest.getBookAuthor().equals(expectedRequest.getBookAuthor());
        assert actualRequest.getGenre().equals(expectedRequest.getGenre());
        assert actualRequest.getReadingContent().equals(expectedRequest.getReadingContent());
    }

    /**
     * 건강 상태 확인 요청 검증 헬퍼 메서드
     */
    protected void verifyHealthCheckRequest(RecordedRequest request) {
        assert request.getMethod().equals("GET");
        assert request.getPath().equals("/api/v1/health/");
    }

    /**
     * 객체를 JSON 문자열로 변환하는 헬퍼 메서드
     */
    protected String toJson(Object object) {
        try {
            return objectMapper.writeValueAsString(object);
        } catch (Exception e) {
            throw new RuntimeException("JSON 변환 실패", e);
        }
    }

    /**
     * JSON 문자열을 객체로 변환하는 헬퍼 메서드
     */
    protected <T> T fromJson(String json, Class<T> clazz) {
        try {
            return objectMapper.readValue(json, clazz);
        } catch (Exception e) {
            throw new RuntimeException("JSON 파싱 실패", e);
        }
    }

    /**
     * 테스트용 AI 분석 요청 생성 헬퍼 메서드
     */
    protected AIAnalysisRequest createTestAnalysisRequest() {
        return new AIAnalysisRequest(
                "1",
                "1",
                "클린 코드",
                "로버트 C. 마틴",
                "TECHNOLOGY",
                "클린 코드는 소프트웨어 개발에서 가독성과 유지보수성을 높이는 중요한 개념입니다."
        );
    }

    /**
     * 다양한 장르의 테스트 요청 생성
     */
    protected AIAnalysisRequest createTestAnalysisRequest(String genre, String content) {
        return new AIAnalysisRequest(
                "1",
                "1",
                "테스트 도서",
                "테스트 저자",
                genre,
                content
        );
    }
}