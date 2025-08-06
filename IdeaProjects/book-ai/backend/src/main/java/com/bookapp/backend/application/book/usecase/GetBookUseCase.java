package com.bookapp.backend.application.book.usecase;

import com.bookapp.backend.domain.book.Book;
import com.bookapp.backend.domain.book.BookRepository;
import com.bookapp.backend.domain.book.Genre;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class GetBookUseCase {

    private final BookRepository bookRepository;

    @Autowired
    public GetBookUseCase(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public Book findById(Long id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("도서를 찾을 수 없습니다: " + id));
    }

    public List<Book> findAll() {
        return bookRepository.findAll();
    }

    public List<Book> findByGenre(Genre genre) {
        return bookRepository.findByGenre(genre);
    }

}