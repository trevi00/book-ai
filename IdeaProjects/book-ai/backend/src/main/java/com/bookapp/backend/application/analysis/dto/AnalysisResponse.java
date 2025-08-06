package com.bookapp.backend.application.analysis.dto;

import com.bookapp.backend.domain.analysis.AIAnalysis;
import com.bookapp.backend.domain.analysis.AnalysisType;

import java.time.LocalDateTime;

public class AnalysisResponse {
    private String analysisId;
    private Long userId;
    private Long bookId;
    private AnalysisType analysisType;
    private String content;
    private LocalDateTime createdAt;

    public AnalysisResponse() {
    }

    public AnalysisResponse(String analysisId, Long userId, Long bookId, AnalysisType analysisType, String content, LocalDateTime createdAt) {
        this.analysisId = analysisId;
        this.userId = userId;
        this.bookId = bookId;
        this.analysisType = analysisType;
        this.content = content;
        this.createdAt = createdAt;
    }

    public static AnalysisResponse fromDomain(AIAnalysis analysis) {
        return new AnalysisResponse(
                analysis.getAnalysisId(),
                analysis.getUserId(),
                analysis.getBookId(),
                analysis.getAnalysisType(),
                analysis.getContent(),
                analysis.getCreatedAt()
        );
    }

    // Getters and Setters
    public String getAnalysisId() {
        return analysisId;
    }

    public void setAnalysisId(String analysisId) {
        this.analysisId = analysisId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getBookId() {
        return bookId;
    }

    public void setBookId(Long bookId) {
        this.bookId = bookId;
    }

    public AnalysisType getAnalysisType() {
        return analysisType;
    }

    public void setAnalysisType(AnalysisType analysisType) {
        this.analysisType = analysisType;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}