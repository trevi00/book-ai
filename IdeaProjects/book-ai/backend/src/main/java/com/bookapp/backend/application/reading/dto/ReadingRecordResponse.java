package com.bookapp.backend.application.reading.dto;

import com.bookapp.backend.application.book.dto.BookResponse;
import com.bookapp.backend.application.user.dto.UserResponse;
import com.bookapp.backend.domain.reading.ReadingRecord;
import com.bookapp.backend.domain.reading.ReadingStatus;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class ReadingRecordResponse {
    
    private Long id;
    private UserResponse user;
    private BookResponse book;
    private String content;
    private ReadingStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    public static ReadingRecordResponse from(ReadingRecord readingRecord) {
        return ReadingRecordResponse.builder()
                .id(readingRecord.getId())
                .user(UserResponse.from(readingRecord.getUser()))
                .book(BookResponse.from(readingRecord.getBook()))
                .content(readingRecord.getContent())
                .status(readingRecord.getStatus())
                .createdAt(readingRecord.getCreatedAt())
                .updatedAt(readingRecord.getUpdatedAt())
                .build();
    }
}