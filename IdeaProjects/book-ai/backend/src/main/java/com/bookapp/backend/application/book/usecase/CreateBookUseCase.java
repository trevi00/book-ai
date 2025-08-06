package com.bookapp.backend.application.book.usecase;

import com.bookapp.backend.application.book.dto.BookCreateRequest;
import com.bookapp.backend.application.book.dto.BookResponse;
import com.bookapp.backend.application.common.CurrentUserService;
import com.bookapp.backend.domain.book.Book;
import com.bookapp.backend.domain.book.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class CreateBookUseCase {
    
    private final BookRepository bookRepository;
    private final CurrentUserService currentUserService;
    
    public BookResponse execute(BookCreateRequest request) {
        validateIsbnNotExists(request.getIsbn());
        
        // 현재 인증된 사용자 ID 가져오기
        Long currentUserId = currentUserService.getCurrentUserId();
        
        Book book = Book.builder()
                .title(request.getTitle())
                .author(request.getAuthor())
                .isbn(request.getIsbn())
                .genre(request.getGenre())
                .description(request.getDescription())
                .content(request.getContent())
                .userId(currentUserId)
                .build();
        
        Book savedBook = bookRepository.save(book);
        return BookResponse.from(savedBook);
    }
    
    private void validateIsbnNotExists(String isbn) {
        if (isbn != null && bookRepository.findByIsbn(isbn).isPresent()) {
            throw new IllegalArgumentException("이미 존재하는 ISBN입니다");
        }
    }
}