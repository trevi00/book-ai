package com.bookapp.backend.application.book.usecase;

import com.bookapp.backend.application.book.dto.BookResponse;
import com.bookapp.backend.domain.book.Book;
import com.bookapp.backend.domain.book.BookRepository;
import com.bookapp.backend.domain.book.Genre;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SearchBookUseCase {
    
    private final BookRepository bookRepository;
    
    public List<BookResponse> findAll() {
        return bookRepository.findAll()
                .stream()
                .map(BookResponse::from)
                .collect(Collectors.toList());
    }
    
    public List<BookResponse> findByGenre(Genre genre) {
        return bookRepository.findByGenre(genre)
                .stream()
                .map(BookResponse::from)
                .collect(Collectors.toList());
    }
    
    public List<BookResponse> findByTitle(String title) {
        return bookRepository.findByTitleContaining(title)
                .stream()
                .map(BookResponse::from)
                .collect(Collectors.toList());
    }
    
    public BookResponse findById(Long id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 책입니다"));
        return BookResponse.from(book);
    }
}