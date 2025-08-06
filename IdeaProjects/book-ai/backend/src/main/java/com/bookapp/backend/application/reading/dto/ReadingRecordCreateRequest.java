package com.bookapp.backend.application.reading.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReadingRecordCreateRequest {
    
    @NotNull(message = "책 ID는 필수입니다")
    @Positive(message = "책 ID는 양수여야 합니다")
    private Long bookId;
    
    private String content;
}