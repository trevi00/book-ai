package com.bookapp.backend.application.reading.usecase;

import com.bookapp.backend.application.reading.dto.ReadingRecordResponse;
import com.bookapp.backend.application.reading.dto.ReadingRecordUpdateRequest;
import com.bookapp.backend.domain.reading.ReadingRecord;
import com.bookapp.backend.domain.reading.ReadingRecordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class UpdateReadingRecordUseCase {
    
    private final ReadingRecordRepository readingRecordRepository;
    
    public ReadingRecordResponse execute(Long readingRecordId, ReadingRecordUpdateRequest request) {
        ReadingRecord readingRecord = findReadingRecordById(readingRecordId);
        
        readingRecord.updateContent(request.getContent());
        
        ReadingRecord updatedRecord = readingRecordRepository.save(readingRecord);
        return ReadingRecordResponse.from(updatedRecord);
    }
    
    private ReadingRecord findReadingRecordById(Long readingRecordId) {
        return readingRecordRepository.findById(readingRecordId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 독서 기록입니다"));
    }
}