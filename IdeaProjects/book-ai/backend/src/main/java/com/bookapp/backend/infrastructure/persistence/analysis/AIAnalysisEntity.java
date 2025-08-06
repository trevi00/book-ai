package com.bookapp.backend.infrastructure.persistence.analysis;

import com.bookapp.backend.domain.analysis.AIAnalysis;
import com.bookapp.backend.domain.analysis.AnalysisType;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "ai_analyses")
public class AIAnalysisEntity {
    @Id
    @Column(name = "analysis_id", length = 36)
    private String analysisId;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "book_id", nullable = false)
    private Long bookId;

    @Enumerated(EnumType.STRING)
    @Column(name = "analysis_type", nullable = false)
    private AnalysisType analysisType;

    @Column(name = "content", columnDefinition = "TEXT", nullable = false)
    private String content;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    public AIAnalysisEntity() {
    }

    public AIAnalysisEntity(String analysisId, Long userId, Long bookId, AnalysisType analysisType, String content, LocalDateTime createdAt) {
        this.analysisId = analysisId;
        this.userId = userId;
        this.bookId = bookId;
        this.analysisType = analysisType;
        this.content = content;
        this.createdAt = createdAt;
    }

    public static AIAnalysisEntity fromDomain(AIAnalysis analysis) {
        return new AIAnalysisEntity(
                analysis.getAnalysisId(),
                analysis.getUserId(),
                analysis.getBookId(),
                analysis.getAnalysisType(),
                analysis.getContent(),
                analysis.getCreatedAt()
        );
    }

    public AIAnalysis toDomain() {
        AIAnalysis analysis = new AIAnalysis();
        analysis.setAnalysisId(this.analysisId);
        analysis.setUserId(this.userId);
        analysis.setBookId(this.bookId);
        analysis.setAnalysisType(this.analysisType);
        analysis.setContent(this.content);
        analysis.setCreatedAt(this.createdAt);
        return analysis;
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