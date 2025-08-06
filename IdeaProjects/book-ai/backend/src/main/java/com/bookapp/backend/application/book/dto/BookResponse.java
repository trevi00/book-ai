package com.bookapp.backend.application.book.dto;

import com.bookapp.backend.domain.book.Book;
import com.bookapp.backend.domain.book.Genre;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class BookResponse {
    
    private Long id;
    private String title;
    private String author;
    private String isbn;
    private Genre genre;
    private String description;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    public static BookResponse from(Book book) {
        return BookResponse.builder()
                .id(book.getId())
                .title(book.getTitle())
                .author(book.getAuthor())
                .isbn(book.getIsbn())
                .genre(book.getGenre())
                .description(book.getDescription())
                .content(book.getContent())
                .createdAt(book.getCreatedAt())
                .updatedAt(book.getUpdatedAt())
                .build();
    }

    public static BookResponse fromDomain(Book book) {
        return from(book);
    }
}