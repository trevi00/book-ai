package com.bookapp.backend.fixtures;

import com.bookapp.backend.domain.user.User;
import com.bookapp.backend.domain.book.Book;
import com.bookapp.backend.domain.book.Genre;
import com.bookapp.backend.domain.reading.ReadingRecord;
import com.bookapp.backend.domain.reading.ReadingStatus;
import com.bookapp.backend.domain.analysis.AIAnalysis;
import com.bookapp.backend.domain.analysis.AnalysisType;

import java.time.LocalDateTime;

public class TestFixtures {

    public static class Users {
        public static User createTestUser() {
            return User.builder()
                    .id(1L)
                    .email("test@example.com")
                    .password("$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi")
                    .nickname("테스트유저")
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .build();
        }

        public static User createAnotherTestUser() {
            return User.builder()
                    .id(2L)
                    .email("user2@example.com")
                    .password("$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi")
                    .nickname("테스트유저2")
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .build();
        }
    }

    public static class Books {
        public static Book createTechnicalBook() {
            return createTechnicalBook(1L);
        }

        public static Book createTechnicalBook(Long userId) {
            return Book.builder()
                    .id(1L)
                    .title("Clean Code")
                    .author("Robert C. Martin")
                    .isbn("9780132350884")
                    .genre(Genre.TECHNOLOGY)
                    .description("소프트웨어 개발자를 위한 클린 코드 작성법")
                    .userId(userId)
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .build();
        }

        public static Book createLiteratureBook() {
            return createLiteratureBook(1L);
        }

        public static Book createLiteratureBook(Long userId) {
            return Book.builder()
                    .id(2L)
                    .title("1984")
                    .author("George Orwell")
                    .isbn("9780451524935")
                    .genre(Genre.FICTION)
                    .description("조지 오웰의 디스토피아 소설")
                    .userId(userId)
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .build();
        }

        public static Book createBookWithUserId(Long userId) {
            return Book.builder()
                    .title("Test Book")
                    .author("Test Author")
                    .isbn("9780000000000")
                    .genre(Genre.TECHNOLOGY)
                    .description("Test book description")
                    .userId(userId)
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .build();
        }
    }

    public static class ReadingRecords {
        public static ReadingRecord createInProgressReading(User user, Book book) {
            return ReadingRecord.builder()
                    .id(1L)
                    .user(user)
                    .book(book)
                    .content("독서 기록 중입니다...")
                    .status(ReadingStatus.IN_PROGRESS)
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .build();
        }

        public static ReadingRecord createCompletedReading(User user, Book book) {
            return ReadingRecord.builder()
                    .id(2L)
                    .user(user)
                    .book(book)
                    .content("책을 완독했습니다. 많은 것을 배웠습니다.")
                    .status(ReadingStatus.COMPLETED)
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .build();
        }
    }

    public static class AIAnalyses {
        public static AIAnalysis createTechnicalAnalysis(ReadingRecord readingRecord) {
            return AIAnalysis.builder()
                    .analysisId("analysis-1")
                    .userId(readingRecord.getUserId())
                    .bookId(readingRecord.getBookId())
                    .analysisType(AnalysisType.TECHNICAL_SUMMARY)
                    .content("기술서적 분석 결과입니다...")
                    .createdAt(LocalDateTime.now())
                    .build();
        }

        public static AIAnalysis createLiteratureAnalysis(ReadingRecord readingRecord) {
            return AIAnalysis.builder()
                    .analysisId("analysis-2")
                    .userId(readingRecord.getUserId())
                    .bookId(readingRecord.getBookId())
                    .analysisType(AnalysisType.LITERATURE_ANALYSIS)
                    .content("문학 작품 분석 결과입니다...")
                    .createdAt(LocalDateTime.now())
                    .build();
        }
    }
}