package com.bookapp.backend.domain.analysis;

import java.util.List;
import java.util.Optional;

public interface AIAnalysisRepository {
    AIAnalysis save(AIAnalysis analysis);
    Optional<AIAnalysis> findById(String analysisId);
    List<AIAnalysis> findByUser_Id(Long userId);
    List<AIAnalysis> findByBook_Id(Long bookId);
    List<AIAnalysis> findByUserIdAndAnalysisType(Long userId, AnalysisType analysisType);
    void deleteById(String analysisId);
    boolean existsById(String analysisId);
}