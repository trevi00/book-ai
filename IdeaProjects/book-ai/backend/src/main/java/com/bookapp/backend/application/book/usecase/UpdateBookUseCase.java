package com.bookapp.backend.application.book.usecase;

import com.bookapp.backend.application.book.dto.BookResponse;
import com.bookapp.backend.application.book.dto.BookUpdateRequest;
import com.bookapp.backend.application.common.CurrentUserService;
import com.bookapp.backend.domain.book.Book;
import com.bookapp.backend.domain.book.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class UpdateBookUseCase {
    
    private final BookRepository bookRepository;
    private final CurrentUserService currentUserService;
    
    public BookResponse execute(Long bookId, BookUpdateRequest request) {
        // 현재 인증된 사용자 ID 가져오기
        Long currentUserId = currentUserService.getCurrentUserId();
        
        // 사용자의 책인지 확인하면서 조회
        Book book = bookRepository.findByIdAndUserId(bookId, currentUserId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 책이거나 수정 권한이 없습니다"));
        
        // 제목과 저자가 다른 책과 중복되는지 확인 (자기 자신 제외)
        validateTitleAndAuthorNotDuplicated(bookId, request.getTitle(), request.getAuthor());
        
        // ISBN이 다른 책과 중복되는지 확인 (자기 자신 제외)
        if (request.getIsbn() != null) {
            validateIsbnNotDuplicated(bookId, request.getIsbn());
        }
        
        // 새로운 책 객체 생성 - 기존 ID, userId, 생성일시 유지
        Book updatedBook = Book.builder()
                .id(book.getId())
                .title(request.getTitle())
                .author(request.getAuthor())
                .isbn(request.getIsbn())
                .genre(request.getGenre())
                .description(request.getDescription())
                .content(request.getContent())
                .userId(book.getUserId())
                .createdAt(book.getCreatedAt())
                .updatedAt(null) // JPA에서 자동으로 현재 시간 설정
                .build();
        
        Book savedBook = bookRepository.save(updatedBook);
        return BookResponse.from(savedBook);
    }
    
    private void validateTitleAndAuthorNotDuplicated(Long bookId, String title, String author) {
        // 추후 필요시 구현 - 현재는 제목+저자 중복 허용
    }
    
    private void validateIsbnNotDuplicated(Long bookId, String isbn) {
        bookRepository.findByIsbn(isbn)
                .filter(book -> !book.getId().equals(bookId))
                .ifPresent(book -> {
                    throw new IllegalArgumentException("이미 존재하는 ISBN입니다");
                });
    }
}