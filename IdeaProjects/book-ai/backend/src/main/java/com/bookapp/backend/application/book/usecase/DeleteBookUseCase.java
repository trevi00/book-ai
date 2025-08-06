package com.bookapp.backend.application.book.usecase;

import com.bookapp.backend.application.common.CurrentUserService;
import com.bookapp.backend.domain.book.Book;
import com.bookapp.backend.domain.book.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class DeleteBookUseCase {
    
    private final BookRepository bookRepository;
    private final CurrentUserService currentUserService;
    
    public void execute(Long bookId) {
        // 현재 인증된 사용자 ID 가져오기
        Long currentUserId = currentUserService.getCurrentUserId();
        
        // 사용자의 책인지 확인하면서 조회
        Book book = bookRepository.findByIdAndUserId(bookId, currentUserId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 책이거나 삭제 권한이 없습니다"));
        
        bookRepository.deleteById(bookId);
    }
}