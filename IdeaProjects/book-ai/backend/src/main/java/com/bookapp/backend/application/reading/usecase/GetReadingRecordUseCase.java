package com.bookapp.backend.application.reading.usecase;

import com.bookapp.backend.domain.reading.ReadingRecord;
import com.bookapp.backend.domain.reading.ReadingRecordRepository;
import com.bookapp.backend.domain.reading.ReadingStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class GetReadingRecordUseCase {

    private final ReadingRecordRepository readingRecordRepository;

    @Autowired
    public GetReadingRecordUseCase(ReadingRecordRepository readingRecordRepository) {
        this.readingRecordRepository = readingRecordRepository;
    }

    public ReadingRecord findById(Long id) {
        return readingRecordRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("독서 기록을 찾을 수 없습니다: " + id));
    }

    public List<ReadingRecord> findByUser_Id(Long userId) {
        return readingRecordRepository.findByUser_Id(userId);
    }

    public List<ReadingRecord> findByUser_IdAndStatus(Long userId, ReadingStatus status) {
        return readingRecordRepository.findByUser_IdAndStatus(userId, status);
    }

    public List<ReadingRecord> findByBook_Id(Long bookId) {
        return readingRecordRepository.findByBook_Id(bookId);
    }
}