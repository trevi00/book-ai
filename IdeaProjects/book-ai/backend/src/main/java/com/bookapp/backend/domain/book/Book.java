package com.bookapp.backend.domain.book;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.regex.Pattern;

@Getter
public class Book {
    private static final Pattern ISBN_PATTERN = Pattern.compile("^(?=(?:\\D*\\d){10}(?:(?:\\D*\\d){3})?$)[\\d-]+$");

    private final Long id;
    private final String title;
    private final String author;
    private final String isbn;
    private final Genre genre;
    private String description;
    private String content;
    private final Long userId; // 책 소유자 ID
    private final LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @Builder
    public Book(Long id, String title, String author, String isbn, Genre genre, 
                String description, String content, Long userId, LocalDateTime createdAt, LocalDateTime updatedAt) {
        validateTitle(title);
        validateAuthor(author);
        validateGenre(genre);
        if (isbn != null) {
            validateIsbn(isbn);
        }
        
        this.id = id;
        this.title = title;
        this.author = author;
        this.isbn = isbn;
        this.genre = genre;
        this.description = description;
        this.content = content;
        this.userId = userId;
        this.createdAt = createdAt != null ? createdAt : LocalDateTime.now();
        this.updatedAt = updatedAt != null ? updatedAt : LocalDateTime.now();
    }

    private void validateTitle(String title) {
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("제목은 필수입니다");
        }
    }

    private void validateAuthor(String author) {
        if (author == null || author.trim().isEmpty()) {
            throw new IllegalArgumentException("저자는 필수입니다");
        }
    }

    private void validateGenre(Genre genre) {
        if (genre == null) {
            throw new IllegalArgumentException("장르는 필수입니다");
        }
    }

    private void validateIsbn(String isbn) {
        if (!ISBN_PATTERN.matcher(isbn.replaceAll("[\\s-]", "")).matches()) {
            throw new IllegalArgumentException("올바른 ISBN 형식이 아닙니다");
        }
    }

    public void updateDescription(String newDescription) {
        this.description = newDescription;
        this.updatedAt = LocalDateTime.now();
    }

    public void updateContent(String newContent) {
        this.content = newContent;
        this.updatedAt = LocalDateTime.now();
    }

    public boolean isTechnology() {
        return this.genre == Genre.TECHNOLOGY;
    }

    public boolean isFiction() {
        return this.genre == Genre.FICTION;
    }
    
    public boolean isNonFiction() {
        return this.genre == Genre.NON_FICTION;
    }
}