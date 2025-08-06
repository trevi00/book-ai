package com.bookapp.backend.application.analysis.dto;

import com.bookapp.backend.domain.analysis.AnalysisType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

/**
 * 도서 내용을 직접 분석하기 위한 요청 DTO
 * 독서 기록 없이 바로 AI 분석을 요청할 때 사용
 */
public class DirectAnalysisRequest {
    
    @NotNull(message = "도서 ID는 필수입니다")
    @Positive(message = "도서 ID는 양수여야 합니다")
    private Long bookId;
    
    @NotBlank(message = "분석할 내용은 필수입니다")
    private String content;
    
    @NotNull(message = "분석 타입은 필수입니다")
    private AnalysisType analysisType;
    
    public DirectAnalysisRequest() {
    }
    
    public DirectAnalysisRequest(Long bookId, String content, AnalysisType analysisType) {
        this.bookId = bookId;
        this.content = content;
        this.analysisType = analysisType;
    }
    
    // Getters and Setters
    public Long getBookId() {
        return bookId;
    }
    
    public void setBookId(Long bookId) {
        this.bookId = bookId;
    }
    
    public String getContent() {
        return content;
    }
    
    public void setContent(String content) {
        this.content = content;
    }
    
    public AnalysisType getAnalysisType() {
        return analysisType;
    }
    
    public void setAnalysisType(AnalysisType analysisType) {
        this.analysisType = analysisType;
    }
}