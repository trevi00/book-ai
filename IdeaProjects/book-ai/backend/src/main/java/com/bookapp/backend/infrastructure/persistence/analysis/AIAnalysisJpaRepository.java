package com.bookapp.backend.infrastructure.persistence.analysis;

import com.bookapp.backend.domain.analysis.AnalysisType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AIAnalysisJpaRepository extends JpaRepository<AIAnalysisEntity, String> {
    List<AIAnalysisEntity> findByUserIdOrderByCreatedAtDesc(Long userId);
    List<AIAnalysisEntity> findByBookIdOrderByCreatedAtDesc(Long bookId);
    
    @Query("SELECT a FROM AIAnalysisEntity a WHERE a.userId = :userId AND a.analysisType = :analysisType ORDER BY a.createdAt DESC")
    List<AIAnalysisEntity> findByUserIdAndAnalysisTypeOrderByCreatedAtDesc(@Param("userId") Long userId, @Param("analysisType") AnalysisType analysisType);
}