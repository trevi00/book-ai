package com.bookapp.backend.domain.reading;

import java.util.List;
import java.util.Optional;

public interface ReadingRecordRepository {
    ReadingRecord save(ReadingRecord readingRecord);
    Optional<ReadingRecord> findById(Long id);
    List<ReadingRecord> findByUser_Id(Long userId);
    List<ReadingRecord> findByUser_IdAndStatus(Long userId, ReadingStatus status);
    List<ReadingRecord> findByBook_Id(Long bookId);
    Optional<ReadingRecord> findByUser_IdAndBook_Id(Long userId, Long bookId);
    boolean existsById(Long id);
    void deleteById(Long id);
}