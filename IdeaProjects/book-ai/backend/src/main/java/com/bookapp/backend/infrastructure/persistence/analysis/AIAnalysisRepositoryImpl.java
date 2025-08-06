package com.bookapp.backend.infrastructure.persistence.analysis;

import com.bookapp.backend.domain.analysis.AIAnalysis;
import com.bookapp.backend.domain.analysis.AIAnalysisRepository;
import com.bookapp.backend.domain.analysis.AnalysisType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class AIAnalysisRepositoryImpl implements AIAnalysisRepository {

    private final AIAnalysisJpaRepository jpaRepository;

    @Autowired
    public AIAnalysisRepositoryImpl(AIAnalysisJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public AIAnalysis save(AIAnalysis analysis) {
        AIAnalysisEntity entity = AIAnalysisEntity.fromDomain(analysis);
        AIAnalysisEntity savedEntity = jpaRepository.save(entity);
        return savedEntity.toDomain();
    }

    @Override
    public Optional<AIAnalysis> findById(String analysisId) {
        return jpaRepository.findById(analysisId)
                .map(AIAnalysisEntity::toDomain);
    }

    @Override
    public List<AIAnalysis> findByUser_Id(Long userId) {
        return jpaRepository.findByUserIdOrderByCreatedAtDesc(userId)
                .stream()
                .map(AIAnalysisEntity::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<AIAnalysis> findByBook_Id(Long bookId) {
        return jpaRepository.findByBookIdOrderByCreatedAtDesc(bookId)
                .stream()
                .map(AIAnalysisEntity::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<AIAnalysis> findByUserIdAndAnalysisType(Long userId, AnalysisType analysisType) {
        return jpaRepository.findByUserIdAndAnalysisTypeOrderByCreatedAtDesc(userId, analysisType)
                .stream()
                .map(AIAnalysisEntity::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteById(String analysisId) {
        jpaRepository.deleteById(analysisId);
    }

    @Override
    public boolean existsById(String analysisId) {
        return jpaRepository.existsById(analysisId);
    }
}