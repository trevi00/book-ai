package com.bookapp.backend.application.book;

import com.bookapp.backend.application.book.dto.BookCreateRequest;
import com.bookapp.backend.application.book.dto.BookResponse;
import com.bookapp.backend.application.book.dto.BookUpdateRequest;
import com.bookapp.backend.application.book.usecase.CreateBookUseCase;
import com.bookapp.backend.application.book.usecase.DeleteBookUseCase;
import com.bookapp.backend.application.book.usecase.GetBookUseCase;
import com.bookapp.backend.application.book.usecase.SearchBookUseCase;
import com.bookapp.backend.application.book.usecase.UpdateBookUseCase;
import com.bookapp.backend.application.common.CurrentUserService;
import com.bookapp.backend.domain.book.Book;
import com.bookapp.backend.domain.book.Genre;
import com.bookapp.backend.domain.book.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookApplicationService {
    
    private final CreateBookUseCase createBookUseCase;
    private final UpdateBookUseCase updateBookUseCase;
    private final DeleteBookUseCase deleteBookUseCase;
    private final GetBookUseCase getBookUseCase;
    private final SearchBookUseCase searchBookUseCase;
    private final CurrentUserService currentUserService;
    private final BookRepository bookRepository;

    @Autowired
    public BookApplicationService(CreateBookUseCase createBookUseCase, UpdateBookUseCase updateBookUseCase, 
                                DeleteBookUseCase deleteBookUseCase, GetBookUseCase getBookUseCase, 
                                SearchBookUseCase searchBookUseCase, CurrentUserService currentUserService,
                                BookRepository bookRepository) {
        this.createBookUseCase = createBookUseCase;
        this.updateBookUseCase = updateBookUseCase;
        this.deleteBookUseCase = deleteBookUseCase;
        this.getBookUseCase = getBookUseCase;
        this.searchBookUseCase = searchBookUseCase;
        this.currentUserService = currentUserService;
        this.bookRepository = bookRepository;
    }
    
    public BookResponse createBook(BookCreateRequest request) {
        return createBookUseCase.execute(request);
    }
    
    public BookResponse updateBook(Long id, BookUpdateRequest request) {
        return updateBookUseCase.execute(id, request);
    }
    
    public void deleteBook(Long id) {
        deleteBookUseCase.execute(id);
    }
    
    public BookResponse findBookById(Long id) {
        // 현재 사용자의 책만 조회
        Long currentUserId = currentUserService.getCurrentUserId();
        Book book = bookRepository.findByIdAndUserId(id, currentUserId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 책이거나 조회 권한이 없습니다"));
        return BookResponse.fromDomain(book);
    }
    
    public List<BookResponse> findAllBooks() {
        // 현재 사용자의 책만 조회
        Long currentUserId = currentUserService.getCurrentUserId();
        List<Book> books = bookRepository.findByUserId(currentUserId);
        return books.stream()
                .map(BookResponse::fromDomain)
                .collect(Collectors.toList());
    }
    
    public List<BookResponse> findBooksByGenre(Genre genre) {
        // 현재 사용자의 책 중 특정 장르만 조회
        Long currentUserId = currentUserService.getCurrentUserId();
        List<Book> books = bookRepository.findByUserIdAndGenre(currentUserId, genre);
        return books.stream()
                .map(BookResponse::fromDomain)
                .collect(Collectors.toList());
    }
    
    
    public List<BookResponse> findBooksByTitle(String title) {
        // 현재 사용자의 책 중 제목으로 검색
        Long currentUserId = currentUserService.getCurrentUserId();
        List<Book> books = bookRepository.findByUserIdAndTitleContaining(currentUserId, title);
        return books.stream()
                .map(BookResponse::fromDomain)
                .collect(Collectors.toList());
    }
}