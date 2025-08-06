package com.bookapp.backend.application.book.dto;

import com.bookapp.backend.domain.book.Genre;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookCreateRequest {
    
    @NotBlank(message = "제목은 필수입니다")
    private String title;
    
    @NotBlank(message = "저자는 필수입니다")
    private String author;
    
    private String isbn;
    
    @NotNull(message = "장르는 필수입니다")
    private Genre genre;
    
    private String description;
    
    private String content;
}