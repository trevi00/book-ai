package com.bookapp.backend.domain.book;

public enum Genre {
    FICTION("소설"),
    NON_FICTION("논픽션"),
    SCIENCE("과학"),
    HISTORY("역사"),
    BIOGRAPHY("전기"),
    TECHNOLOGY("기술"),
    OTHER("기타");

    private final String description;

    Genre(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}