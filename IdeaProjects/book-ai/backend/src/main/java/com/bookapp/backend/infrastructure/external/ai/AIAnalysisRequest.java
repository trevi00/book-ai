package com.bookapp.backend.infrastructure.external.ai;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AIAnalysisRequest {
    @JsonProperty("user_id")
    private String userId;
    
    @JsonProperty("book_id")
    private String bookId;
    
    @JsonProperty("book_title")
    private String bookTitle;
    
    @JsonProperty("book_author")
    private String bookAuthor;
    
    private String genre;
    
    @JsonProperty("reading_content")
    private String readingContent;

    public AIAnalysisRequest() {
    }

    public AIAnalysisRequest(String userId, String bookId, String bookTitle, String bookAuthor, String genre, String readingContent) {
        this.userId = userId;
        this.bookId = bookId;
        this.bookTitle = bookTitle;
        this.bookAuthor = bookAuthor;
        this.genre = genre;
        this.readingContent = readingContent;
    }

    // Getters and Setters
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getBookId() {
        return bookId;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
    }

    public String getBookTitle() {
        return bookTitle;
    }

    public void setBookTitle(String bookTitle) {
        this.bookTitle = bookTitle;
    }

    public String getBookAuthor() {
        return bookAuthor;
    }

    public void setBookAuthor(String bookAuthor) {
        this.bookAuthor = bookAuthor;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getReadingContent() {
        return readingContent;
    }

    public void setReadingContent(String readingContent) {
        this.readingContent = readingContent;
    }
}