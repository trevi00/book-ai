package com.bookapp.backend.domain.reading;

import com.bookapp.backend.domain.book.Book;
import com.bookapp.backend.domain.user.User;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ReadingRecord {
    private final Long id;
    private final User user;
    private final Book book;
    private String content;
    private ReadingStatus status;
    private final LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @Builder
    public ReadingRecord(Long id, User user, Book book, String content, 
                        ReadingStatus status, LocalDateTime createdAt, LocalDateTime updatedAt) {
        validateUser(user);
        validateBook(book);
        
        this.id = id;
        this.user = user;
        this.book = book;
        this.content = content != null ? content : "";
        this.status = status != null ? status : ReadingStatus.IN_PROGRESS;
        this.createdAt = createdAt != null ? createdAt : LocalDateTime.now();
        this.updatedAt = updatedAt != null ? updatedAt : LocalDateTime.now();
    }

    private void validateUser(User user) {
        if (user == null) {
            throw new IllegalArgumentException("사용자는 필수입니다");
        }
    }

    private void validateBook(Book book) {
        if (book == null) {
            throw new IllegalArgumentException("책은 필수입니다");
        }
    }

    public void updateContent(String newContent) {
        if (this.status == ReadingStatus.COMPLETED) {
            throw new IllegalStateException("완료된 독서는 수정할 수 없습니다");
        }
        
        this.content = newContent != null ? newContent : "";
        this.updatedAt = LocalDateTime.now();
    }

    public void complete() {
        if (this.status == ReadingStatus.COMPLETED) {
            throw new IllegalStateException("이미 완료된 독서입니다");
        }
        
        this.status = ReadingStatus.COMPLETED;
        this.updatedAt = LocalDateTime.now();
    }

    public boolean isCompleted() {
        return this.status == ReadingStatus.COMPLETED;
    }

    public boolean isInProgress() {
        return this.status == ReadingStatus.IN_PROGRESS;
    }

    public Long getUserId() {
        return this.user.getId();
    }

    public Long getBookId() {
        return this.book.getId();
    }
}