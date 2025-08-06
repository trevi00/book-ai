package com.bookapp.backend.web.book;

import com.bookapp.backend.application.book.BookApplicationService;
import com.bookapp.backend.application.book.dto.BookCreateRequest;
import com.bookapp.backend.application.book.dto.BookResponse;
import com.bookapp.backend.application.book.dto.BookUpdateRequest;
import com.bookapp.backend.domain.book.Genre;
import com.bookapp.backend.web.common.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/books")
public class BookController {

    private final BookApplicationService bookApplicationService;

    public BookController(BookApplicationService bookApplicationService) {
        this.bookApplicationService = bookApplicationService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<BookResponse> createBook(@Valid @RequestBody BookCreateRequest request) {
        BookResponse response = bookApplicationService.createBook(request);
        return ApiResponse.success(response, "책이 등록되었습니다");
    }

    @GetMapping
    public ApiResponse<List<BookResponse>> getAllBooks() {
        List<BookResponse> books = bookApplicationService.findAllBooks();
        return ApiResponse.success(books);
    }

    @GetMapping("/{id}")
    public ApiResponse<BookResponse> getBook(@PathVariable Long id) {
        BookResponse book = bookApplicationService.findBookById(id);
        return ApiResponse.success(book);
    }

    @PutMapping("/{id}")
    public ApiResponse<BookResponse> updateBook(@PathVariable Long id, @Valid @RequestBody BookUpdateRequest request) {
        BookResponse updatedBook = bookApplicationService.updateBook(id, request);
        return ApiResponse.success(updatedBook, "책이 수정되었습니다");
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ApiResponse<Void> deleteBook(@PathVariable Long id) {
        bookApplicationService.deleteBook(id);
        return ApiResponse.success(null, "책이 삭제되었습니다");
    }

    @GetMapping("/genre/{genre}")
    public ApiResponse<List<BookResponse>> getBooksByGenre(@PathVariable Genre genre) {
        List<BookResponse> books = bookApplicationService.findBooksByGenre(genre);
        return ApiResponse.success(books);
    }

    @GetMapping("/search")
    public ApiResponse<List<BookResponse>> searchBooks(@RequestParam String title) {
        List<BookResponse> books = bookApplicationService.findBooksByTitle(title);
        return ApiResponse.success(books);
    }

}