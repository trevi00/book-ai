package com.bookapp.backend.application.reading.usecase;

import com.bookapp.backend.application.reading.dto.ReadingRecordResponse;
import com.bookapp.backend.domain.reading.ReadingRecord;
import com.bookapp.backend.domain.reading.ReadingRecordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class CompleteReadingUseCase {
    
    private final ReadingRecordRepository readingRecordRepository;
    
    public ReadingRecordResponse execute(Long readingRecordId) {
        ReadingRecord readingRecord = findReadingRecordById(readingRecordId);
        
        // 이미 완료된 경우 현재 상태 반환 (에러 대신 idempotent 처리)
        if (readingRecord.isCompleted()) {
            return ReadingRecordResponse.from(readingRecord);
        }
        
        readingRecord.complete();
        
        ReadingRecord completedRecord = readingRecordRepository.save(readingRecord);
        return ReadingRecordResponse.from(completedRecord);
    }
    
    private ReadingRecord findReadingRecordById(Long readingRecordId) {
        return readingRecordRepository.findById(readingRecordId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 독서 기록입니다"));
    }
}