package com.bookapp.backend.application.analysis;

import com.bookapp.backend.application.analysis.dto.AnalysisRequest;
import com.bookapp.backend.application.analysis.dto.AnalysisResponse;
import com.bookapp.backend.application.analysis.dto.DirectAnalysisRequest;
import com.bookapp.backend.application.analysis.usecase.DeleteAnalysisUseCase;
import com.bookapp.backend.application.analysis.usecase.GenerateAnalysisUseCase;
import com.bookapp.backend.application.analysis.usecase.GenerateDirectAnalysisUseCase;
import com.bookapp.backend.application.analysis.usecase.GetAnalysisUseCase;
import com.bookapp.backend.domain.analysis.AIAnalysis;
import com.bookapp.backend.domain.analysis.AnalysisType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AnalysisApplicationService {

    private final GenerateAnalysisUseCase generateAnalysisUseCase;
    private final GenerateDirectAnalysisUseCase generateDirectAnalysisUseCase;
    private final GetAnalysisUseCase getAnalysisUseCase;
    private final DeleteAnalysisUseCase deleteAnalysisUseCase;

    @Autowired
    public AnalysisApplicationService(
            GenerateAnalysisUseCase generateAnalysisUseCase,
            GenerateDirectAnalysisUseCase generateDirectAnalysisUseCase,
            GetAnalysisUseCase getAnalysisUseCase,
            DeleteAnalysisUseCase deleteAnalysisUseCase) {
        this.generateAnalysisUseCase = generateAnalysisUseCase;
        this.generateDirectAnalysisUseCase = generateDirectAnalysisUseCase;
        this.getAnalysisUseCase = getAnalysisUseCase;
        this.deleteAnalysisUseCase = deleteAnalysisUseCase;
    }

    public AnalysisResponse generateAnalysis(AnalysisRequest request) {
        AIAnalysis analysis = generateAnalysisUseCase.execute(
                request.getReadingRecordId(),
                request.getAnalysisType()
        );
        return AnalysisResponse.fromDomain(analysis);
    }

    /**
     * 도서 내용을 직접 분석하는 메서드
     * 독서 기록 없이 바로 AI 분석을 수행
     */
    public AnalysisResponse generateDirectAnalysis(DirectAnalysisRequest request) {
        AIAnalysis analysis = generateDirectAnalysisUseCase.execute(
                request.getBookId(),
                request.getContent(),
                request.getAnalysisType()
        );
        return AnalysisResponse.fromDomain(analysis);
    }

    public AnalysisResponse getAnalysisById(String analysisId) {
        AIAnalysis analysis = getAnalysisUseCase.findById(analysisId);
        return AnalysisResponse.fromDomain(analysis);
    }

    public List<AnalysisResponse> getAnalysesByUserId(Long userId) {
        List<AIAnalysis> analyses = getAnalysisUseCase.findByUser_Id(userId);
        return analyses.stream()
                .map(AnalysisResponse::fromDomain)
                .collect(Collectors.toList());
    }

    public List<AnalysisResponse> getAnalysesByBookId(Long bookId) {
        List<AIAnalysis> analyses = getAnalysisUseCase.findByBook_Id(bookId);
        return analyses.stream()
                .map(AnalysisResponse::fromDomain)
                .collect(Collectors.toList());
    }

    public List<AnalysisResponse> getAnalysesByUserIdAndType(Long userId, AnalysisType analysisType) {
        List<AIAnalysis> analyses = getAnalysisUseCase.findByUserIdAndType(userId, analysisType);
        return analyses.stream()
                .map(AnalysisResponse::fromDomain)
                .collect(Collectors.toList());
    }

    public void deleteAnalysis(String analysisId) {
        deleteAnalysisUseCase.execute(analysisId);
    }
}