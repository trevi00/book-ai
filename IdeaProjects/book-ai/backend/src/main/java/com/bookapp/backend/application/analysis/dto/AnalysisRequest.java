package com.bookapp.backend.application.analysis.dto;

import com.bookapp.backend.domain.analysis.AnalysisType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class AnalysisRequest {
    @NotNull(message = "독서 기록 ID는 필수입니다")
    @Positive(message = "독서 기록 ID는 양수여야 합니다")
    private Long readingRecordId;

    @NotNull(message = "분석 타입은 필수입니다")
    private AnalysisType analysisType;

    public AnalysisRequest() {
    }

    public AnalysisRequest(Long readingRecordId, AnalysisType analysisType) {
        this.readingRecordId = readingRecordId;
        this.analysisType = analysisType;
    }

    // Getters and Setters
    public Long getReadingRecordId() {
        return readingRecordId;
    }

    public void setReadingRecordId(Long readingRecordId) {
        this.readingRecordId = readingRecordId;
    }

    public AnalysisType getAnalysisType() {
        return analysisType;
    }

    public void setAnalysisType(AnalysisType analysisType) {
        this.analysisType = analysisType;
    }
}