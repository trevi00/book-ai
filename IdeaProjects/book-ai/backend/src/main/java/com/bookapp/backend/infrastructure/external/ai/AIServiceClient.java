package com.bookapp.backend.infrastructure.external.ai;

import com.bookapp.backend.domain.analysis.AnalysisType;

public interface AIServiceClient {
    String generateAnalysis(AIAnalysisRequest request);
    boolean isHealthy();
}