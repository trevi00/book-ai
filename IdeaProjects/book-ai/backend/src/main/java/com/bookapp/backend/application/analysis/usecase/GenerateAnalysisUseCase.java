package com.bookapp.backend.application.analysis.usecase;

import com.bookapp.backend.domain.analysis.AIAnalysis;
import com.bookapp.backend.domain.analysis.AIAnalysisRepository;
import com.bookapp.backend.domain.analysis.AnalysisType;
import com.bookapp.backend.domain.book.Book;
import com.bookapp.backend.domain.book.BookRepository;
import com.bookapp.backend.domain.reading.ReadingRecord;
import com.bookapp.backend.domain.reading.ReadingRecordRepository;
import com.bookapp.backend.domain.reading.ReadingStatus;
import com.bookapp.backend.infrastructure.external.ai.AIServiceClient;
import com.bookapp.backend.infrastructure.external.ai.AIAnalysisRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class GenerateAnalysisUseCase {

    private final AIAnalysisRepository analysisRepository;
    private final ReadingRecordRepository readingRepository;
    private final BookRepository bookRepository;
    private final AIServiceClient aiServiceClient;

    @Autowired
    public GenerateAnalysisUseCase(
            AIAnalysisRepository analysisRepository,
            ReadingRecordRepository readingRepository,
            BookRepository bookRepository,
            AIServiceClient aiServiceClient) {
        this.analysisRepository = analysisRepository;
        this.readingRepository = readingRepository;
        this.bookRepository = bookRepository;
        this.aiServiceClient = aiServiceClient;
    }

    public AIAnalysis execute(Long readingRecordId, AnalysisType analysisType) {
        // 독서 기록 조회
        ReadingRecord readingRecord = readingRepository.findById(readingRecordId)
                .orElseThrow(() -> new IllegalArgumentException("독서 기록을 찾을 수 없습니다: " + readingRecordId));

        // 완료된 독서 기록인지 확인
        if (readingRecord.getStatus() != ReadingStatus.COMPLETED) {
            throw new IllegalStateException("완료된 독서 기록만 분석할 수 있습니다");
        }

        // 독서 내용이 있는지 확인
        if (readingRecord.getContent() == null || readingRecord.getContent().trim().isEmpty()) {
            throw new IllegalArgumentException("분석할 독서 내용이 없습니다");
        }

        // 도서 정보 조회
        Book book = bookRepository.findById(readingRecord.getBookId())
                .orElseThrow(() -> new IllegalArgumentException("도서를 찾을 수 없습니다: " + readingRecord.getBookId()));

        // AI 서비스 요청 생성
        AIAnalysisRequest aiRequest = new AIAnalysisRequest(
                        readingRecord.getUserId().toString(),
                        book.getId().toString(),
                        book.getTitle(),
                        book.getAuthor(),
                        book.getGenre().name(),
                        readingRecord.getContent()
                );

        // AI 분석 수행
        String analysisContent = aiServiceClient.generateAnalysis(aiRequest);

        // AI 분석 결과 저장
        AIAnalysis analysis = AIAnalysis.builder()
                .analysisId(java.util.UUID.randomUUID().toString())
                .userId(readingRecord.getUserId())
                .bookId(book.getId())
                .analysisType(analysisType)
                .content(analysisContent)
                .createdAt(java.time.LocalDateTime.now())
                .build();
        
        analysis.validate();

        return analysisRepository.save(analysis);
    }
}