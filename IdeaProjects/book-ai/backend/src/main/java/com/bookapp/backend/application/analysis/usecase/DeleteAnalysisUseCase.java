package com.bookapp.backend.application.analysis.usecase;

import com.bookapp.backend.domain.analysis.AIAnalysisRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class DeleteAnalysisUseCase {

    private final AIAnalysisRepository analysisRepository;

    @Autowired
    public DeleteAnalysisUseCase(AIAnalysisRepository analysisRepository) {
        this.analysisRepository = analysisRepository;
    }

    public void execute(String analysisId) {
        if (!analysisRepository.existsById(analysisId)) {
            throw new IllegalArgumentException("분석 결과를 찾을 수 없습니다: " + analysisId);
        }
        
        analysisRepository.deleteById(analysisId);
    }
}