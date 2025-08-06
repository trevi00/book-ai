package com.bookapp.backend.infrastructure.persistence.reading;

import com.bookapp.backend.domain.reading.ReadingStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReadingRecordJpaRepository extends JpaRepository<ReadingRecordEntity, Long> {
    
    @Query("SELECT rr FROM ReadingRecordEntity rr WHERE rr.user.id = :userId")
    List<ReadingRecordEntity> findByUser_Id(@Param("userId") Long userId);
    
    @Query("SELECT rr FROM ReadingRecordEntity rr WHERE rr.user.id = :userId AND rr.status = :status")
    List<ReadingRecordEntity> findByUser_IdAndStatus(@Param("userId") Long userId, @Param("status") ReadingStatus status);
    
    @Query("SELECT rr FROM ReadingRecordEntity rr WHERE rr.user.id = :userId AND rr.book.id = :bookId")
    Optional<ReadingRecordEntity> findByUser_IdAndBook_Id(@Param("userId") Long userId, @Param("bookId") Long bookId);

    @Query("SELECT rr FROM ReadingRecordEntity rr WHERE rr.book.id = :bookId ORDER BY rr.createdAt DESC")
    List<ReadingRecordEntity> findByBook_IdOrderByCreatedAtDesc(@Param("bookId") Long bookId);
}