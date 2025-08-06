package com.bookapp.backend.infrastructure.persistence.book;

import com.bookapp.backend.domain.book.Book;
import com.bookapp.backend.domain.book.BookRepository;
import com.bookapp.backend.domain.book.Genre;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class BookRepositoryImpl implements BookRepository {
    
    private final BookJpaRepository bookJpaRepository;
    
    @Override
    public Book save(Book book) {
        BookEntity entity = BookEntity.fromDomain(book);
        BookEntity savedEntity = bookJpaRepository.save(entity);
        return savedEntity.toDomain();
    }
    
    @Override
    public Optional<Book> findById(Long id) {
        return bookJpaRepository.findById(id)
                .map(BookEntity::toDomain);
    }
    
    @Override
    public Optional<Book> findByIsbn(String isbn) {
        return bookJpaRepository.findByIsbn(isbn)
                .map(BookEntity::toDomain);
    }
    
    @Override
    public List<Book> findByGenre(Genre genre) {
        return bookJpaRepository.findByGenre(genre)
                .stream()
                .map(BookEntity::toDomain)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<Book> findByTitleContaining(String title) {
        return bookJpaRepository.findByTitleContainingIgnoreCase(title)
                .stream()
                .map(BookEntity::toDomain)
                .collect(Collectors.toList());
    }

    
    @Override
    public List<Book> findAll() {
        return bookJpaRepository.findAll()
                .stream()
                .map(BookEntity::toDomain)
                .collect(Collectors.toList());
    }
    
    @Override
    public void deleteById(Long id) {
        bookJpaRepository.deleteById(id);
    }
    
    @Override
    public Optional<Book> findByIdAndUserId(Long id, Long userId) {
        return bookJpaRepository.findByIdAndUserId(id, userId)
                .map(BookEntity::toDomain);
    }
    
    @Override
    public List<Book> findByUserId(Long userId) {
        return bookJpaRepository.findByUserId(userId)
                .stream()
                .map(BookEntity::toDomain)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<Book> findByUserIdAndGenre(Long userId, Genre genre) {
        return bookJpaRepository.findByUserIdAndGenre(userId, genre)
                .stream()
                .map(BookEntity::toDomain)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<Book> findByUserIdAndTitleContaining(Long userId, String title) {
        return bookJpaRepository.findByUserIdAndTitleContainingIgnoreCase(userId, title)
                .stream()
                .map(BookEntity::toDomain)
                .collect(Collectors.toList());
    }
}