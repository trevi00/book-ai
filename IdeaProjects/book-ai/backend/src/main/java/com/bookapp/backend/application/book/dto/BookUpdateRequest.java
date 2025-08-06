package com.bookapp.backend.application.book.dto;

import com.bookapp.backend.domain.book.Genre;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BookUpdateRequest {
    
    @NotBlank(message = "제목은 필수입니다")
    private String title;
    
    @NotBlank(message = "저자는 필수입니다")
    private String author;
    
    @Pattern(regexp = "^(?=(?:\\D*\\d){10}(?:(?:\\D*\\d){3})?$)[\\d-]+$", message = "올바른 ISBN 형식이 아닙니다")
    private String isbn;
    
    @NotNull(message = "장르는 필수입니다")
    private Genre genre;
    
    private String description;
    
    private String content;
}