package com.bookapp.backend.domain.reading;

public enum ReadingStatus {
    IN_PROGRESS("진행중"),
    COMPLETED("완료");

    private final String description;

    ReadingStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}