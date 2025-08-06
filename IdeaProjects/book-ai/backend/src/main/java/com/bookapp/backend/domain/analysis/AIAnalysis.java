package com.bookapp.backend.domain.analysis;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AIAnalysis {
    private String analysisId;
    private Long userId;
    private Long bookId;
    private AnalysisType analysisType;
    private String content;
    private LocalDateTime createdAt;


    public void validateContent() {
        if (content == null || content.trim().isEmpty()) {
            throw new IllegalArgumentException("분석 내용은 비어있을 수 없습니다");
        }
        if (content.length() > 10000) {
            throw new IllegalArgumentException("분석 내용은 10000자를 초과할 수 없습니다");
        }
    }

    public void validateAnalysisType() {
        if (analysisType == null) {
            throw new IllegalArgumentException("분석 타입은 필수입니다");
        }
    }

    public void validateIds() {
        if (userId == null || userId <= 0) {
            throw new IllegalArgumentException("유효한 사용자 ID가 필요합니다");
        }
        if (bookId == null || bookId <= 0) {
            throw new IllegalArgumentException("유효한 도서 ID가 필요합니다");
        }
    }

    public void validate() {
        validateIds();
        validateAnalysisType();
        validateContent();
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