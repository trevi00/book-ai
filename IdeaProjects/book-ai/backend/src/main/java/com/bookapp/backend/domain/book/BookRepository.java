package com.bookapp.backend.domain.book;

import java.util.List;
import java.util.Optional;

public interface BookRepository {
    Book save(Book book);
    Optional<Book> findById(Long id);
    Optional<Book> findByIsbn(String isbn);
    List<Book> findByGenre(Genre genre);
    List<Book> findByTitleContaining(String title);
    List<Book> findAll();
    void deleteById(Long id);
    
    // 사용자별 책 관리 메서드
    Optional<Book> findByIdAndUserId(Long id, Long userId);
    List<Book> findByUserId(Long userId);
    List<Book> findByUserIdAndGenre(Long userId, Genre genre);
    List<Book> findByUserIdAndTitleContaining(Long userId, String title);
}