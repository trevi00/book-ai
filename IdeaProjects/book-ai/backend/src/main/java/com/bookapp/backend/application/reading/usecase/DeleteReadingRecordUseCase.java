package com.bookapp.backend.application.reading.usecase;

import com.bookapp.backend.domain.reading.ReadingRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class DeleteReadingRecordUseCase {

    private final ReadingRecordRepository readingRecordRepository;

    @Autowired
    public DeleteReadingRecordUseCase(ReadingRecordRepository readingRecordRepository) {
        this.readingRecordRepository = readingRecordRepository;
    }

    public void execute(Long id) {
        if (!readingRecordRepository.existsById(id)) {
            throw new IllegalArgumentException("독서 기록을 찾을 수 없습니다: " + id);
        }
        
        readingRecordRepository.deleteById(id);
    }
}