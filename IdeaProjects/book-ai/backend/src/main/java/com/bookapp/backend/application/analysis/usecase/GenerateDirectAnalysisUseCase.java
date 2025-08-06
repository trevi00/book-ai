package com.bookapp.backend.application.analysis.usecase;

import com.bookapp.backend.domain.analysis.AIAnalysis;
import com.bookapp.backend.domain.analysis.AnalysisType;
import com.bookapp.backend.domain.analysis.AIAnalysisRepository;
import com.bookapp.backend.domain.book.Book;
import com.bookapp.backend.domain.book.BookRepository;
import com.bookapp.backend.infrastructure.external.ai.AIAnalysisRequest;
import com.bookapp.backend.infrastructure.external.ai.FastAPIServiceClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * 도서 내용을 직접 분석하는 Use Case
 * 독서 기록 없이 바로 AI 분석을 수행
 */
@Service
@Transactional
public class GenerateDirectAnalysisUseCase {

    private static final Logger log = LoggerFactory.getLogger(GenerateDirectAnalysisUseCase.class);
    
    private final BookRepository bookRepository;
    private final AIAnalysisRepository analysisRepository;
    private final FastAPIServiceClient fastAPIServiceClient;

    @Autowired
    public GenerateDirectAnalysisUseCase(
            BookRepository bookRepository,
            AIAnalysisRepository analysisRepository,
            FastAPIServiceClient fastAPIServiceClient) {
        this.bookRepository = bookRepository;
        this.analysisRepository = analysisRepository;
        this.fastAPIServiceClient = fastAPIServiceClient;
    }

    /**
     * 도서 내용을 직접 분석하여 AI 분석 결과를 생성
     * 
     * @param bookId 도서 ID
     * @param content 분석할 내용
     * @param analysisType 분석 타입
     * @return 생성된 AI 분석 결과
     */
    public AIAnalysis execute(Long bookId, String content, AnalysisType analysisType) {
        log.info("도서 직접 분석 시작 - bookId: {}, analysisType: {}", bookId, analysisType);

        // 1. 도서 존재 확인
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new IllegalArgumentException("도서를 찾을 수 없습니다: " + bookId));

        // 2. AI 분석 요청 생성
        AIAnalysisRequest aiRequest = new AIAnalysisRequest(
                "direct_analysis_" + UUID.randomUUID().toString(),
                "direct_book_" + bookId,
                book.getTitle(),
                book.getAuthor(),
                book.getGenre().toString(),
                content
        );

        // 3. AI 서비스 호출
        log.info("AI 서비스 호출 시작");
        String analysisResult;
        try {
            analysisResult = fastAPIServiceClient.generateAnalysis(aiRequest);
            log.info("AI 분석 완료 - 결과 길이: {} characters", analysisResult.length());
        } catch (Exception e) {
            log.error("AI 분석 중 오류 발생", e);
            throw new RuntimeException("AI 분석 생성에 실패했습니다: " + e.getMessage(), e);
        }

        // 4. AI 분석 도메인 객체 생성
        AIAnalysis analysis = AIAnalysis.builder()
                .analysisId(UUID.randomUUID().toString())
                .userId(book.getUserId())
                .bookId(bookId)
                .analysisType(analysisType)
                .content(analysisResult)
                .createdAt(LocalDateTime.now())
                .build();

        // 5. 분석 결과 저장
        AIAnalysis savedAnalysis = analysisRepository.save(analysis);
        log.info("AI 분석 저장 완료 - analysisId: {}", savedAnalysis.getAnalysisId());

        return savedAnalysis;
    }
}