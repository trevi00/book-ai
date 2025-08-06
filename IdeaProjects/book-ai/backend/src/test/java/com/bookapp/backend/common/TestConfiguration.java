package com.bookapp.backend.common;

import com.bookapp.backend.infrastructure.external.ai.AIServiceClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.test.context.TestPropertySource;

import static org.mockito.Mockito.mock;

@org.springframework.boot.test.context.TestConfiguration
@TestPropertySource(locations = "classpath:application-test.properties")
public class TestConfiguration {

    @Bean
    @Primary
    public AIServiceClient mockAIService() {
        return mock(AIServiceClient.class);
    }
}