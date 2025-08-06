package com.bookapp.backend.infrastructure.persistence.book;

import com.bookapp.backend.domain.book.Genre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookJpaRepository extends JpaRepository<BookEntity, Long> {
    Optional<BookEntity> findByIsbn(String isbn);
    List<BookEntity> findByGenre(Genre genre);
    List<BookEntity> findByTitleContainingIgnoreCase(String title);
    
    // 사용자별 쿼리 메서드
    Optional<BookEntity> findByIdAndUserId(Long id, Long userId);
    List<BookEntity> findByUserId(Long userId);
    List<BookEntity> findByUserIdAndGenre(Long userId, Genre genre);
    List<BookEntity> findByUserIdAndTitleContainingIgnoreCase(Long userId, String title);
}