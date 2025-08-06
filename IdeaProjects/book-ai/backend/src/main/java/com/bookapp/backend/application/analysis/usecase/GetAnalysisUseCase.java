package com.bookapp.backend.application.analysis.usecase;

import com.bookapp.backend.domain.analysis.AIAnalysis;
import com.bookapp.backend.domain.analysis.AIAnalysisRepository;
import com.bookapp.backend.domain.analysis.AnalysisType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class GetAnalysisUseCase {

    private final AIAnalysisRepository analysisRepository;

    @Autowired
    public GetAnalysisUseCase(AIAnalysisRepository analysisRepository) {
        this.analysisRepository = analysisRepository;
    }

    public AIAnalysis findById(String analysisId) {
        return analysisRepository.findById(analysisId)
                .orElseThrow(() -> new IllegalArgumentException("분석 결과를 찾을 수 없습니다: " + analysisId));
    }

    public List<AIAnalysis> findByUser_Id(Long userId) {
        return analysisRepository.findByUser_Id(userId);
    }

    public List<AIAnalysis> findByBook_Id(Long bookId) {
        return analysisRepository.findByBook_Id(bookId);
    }

    public List<AIAnalysis> findByUserIdAndType(Long userId, AnalysisType analysisType) {
        return analysisRepository.findByUserIdAndAnalysisType(userId, analysisType);
    }
}