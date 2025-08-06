package com.bookapp.backend.infrastructure.persistence.reading;

import com.bookapp.backend.domain.reading.ReadingRecord;
import com.bookapp.backend.domain.reading.ReadingStatus;
import com.bookapp.backend.infrastructure.persistence.book.BookEntity;
import com.bookapp.backend.infrastructure.persistence.user.UserEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "reading_records")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReadingRecordEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id", nullable = false)
    private BookEntity book;
    
    @Column(columnDefinition = "LONGTEXT", nullable = false)
    private String content;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ReadingStatus status;
    
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
    
    @Builder
    public ReadingRecordEntity(Long id, UserEntity user, BookEntity book, String content,
                              ReadingStatus status, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.user = user;
        this.book = book;
        this.content = content != null ? content : "";
        this.status = status != null ? status : ReadingStatus.IN_PROGRESS;
        this.createdAt = createdAt != null ? createdAt : LocalDateTime.now();
        this.updatedAt = updatedAt != null ? updatedAt : LocalDateTime.now();
    }
    
    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
    
    public ReadingRecord toDomain() {
        return ReadingRecord.builder()
                .id(this.id)
                .user(this.user.toDomain())
                .book(this.book.toDomain())
                .content(this.content)
                .status(this.status)
                .createdAt(this.createdAt)
                .updatedAt(this.updatedAt)
                .build();
    }
    
    public static ReadingRecordEntity fromDomain(ReadingRecord readingRecord) {
        return ReadingRecordEntity.builder()
                .id(readingRecord.getId())
                .user(UserEntity.fromDomain(readingRecord.getUser()))
                .book(BookEntity.fromDomain(readingRecord.getBook()))
                .content(readingRecord.getContent())
                .status(readingRecord.getStatus())
                .createdAt(readingRecord.getCreatedAt())
                .updatedAt(readingRecord.getUpdatedAt())
                .build();
    }
    
    public void updateContent(String content) {
        this.content = content;
        this.updatedAt = LocalDateTime.now();
    }
    
    public void complete() {
        this.status = ReadingStatus.COMPLETED;
        this.updatedAt = LocalDateTime.now();
    }
}