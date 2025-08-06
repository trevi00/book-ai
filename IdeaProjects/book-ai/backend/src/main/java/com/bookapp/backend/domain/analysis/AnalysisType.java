package com.bookapp.backend.domain.analysis;

public enum AnalysisType {
    LITERATURE_ANALYSIS("문학 분석"),
    TECHNICAL_SUMMARY("기술 요약");

    private final String description;

    AnalysisType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}