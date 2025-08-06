package com.bookapp.backend.application.reading.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ReadingRecordUpdateRequest {
    
    private String content;
}