package com.bookapp.backend.application.reading;

import com.bookapp.backend.application.reading.dto.ReadingRecordCreateRequest;
import com.bookapp.backend.application.reading.dto.ReadingRecordResponse;
import com.bookapp.backend.application.reading.dto.ReadingRecordUpdateRequest;
import com.bookapp.backend.application.reading.usecase.*;
import com.bookapp.backend.domain.reading.ReadingRecord;
import com.bookapp.backend.domain.reading.ReadingStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReadingApplicationService {
    
    private final CreateReadingRecordUseCase createReadingRecordUseCase;
    private final GetReadingRecordUseCase getReadingRecordUseCase;
    private final UpdateReadingRecordUseCase updateReadingRecordUseCase;
    private final CompleteReadingUseCase completeReadingUseCase;
    private final DeleteReadingRecordUseCase deleteReadingRecordUseCase;

    @Autowired
    public ReadingApplicationService(
            CreateReadingRecordUseCase createReadingRecordUseCase,
            GetReadingRecordUseCase getReadingRecordUseCase,
            UpdateReadingRecordUseCase updateReadingRecordUseCase,
            CompleteReadingUseCase completeReadingUseCase,
            DeleteReadingRecordUseCase deleteReadingRecordUseCase) {
        this.createReadingRecordUseCase = createReadingRecordUseCase;
        this.getReadingRecordUseCase = getReadingRecordUseCase;
        this.updateReadingRecordUseCase = updateReadingRecordUseCase;
        this.completeReadingUseCase = completeReadingUseCase;
        this.deleteReadingRecordUseCase = deleteReadingRecordUseCase;
    }
    
    public ReadingRecordResponse createReadingRecord(ReadingRecordCreateRequest request) {
        return createReadingRecordUseCase.execute(request);
    }
    
    public ReadingRecordResponse findReadingRecordById(Long readingRecordId) {
        ReadingRecord readingRecord = getReadingRecordUseCase.findById(readingRecordId);
        return ReadingRecordResponse.from(readingRecord);
    }
    
    public List<ReadingRecordResponse> findReadingRecordsByUserId(Long userId) {
        List<ReadingRecord> readingRecords = getReadingRecordUseCase.findByUser_Id(userId);
        return readingRecords.stream()
                .map(ReadingRecordResponse::from)
                .collect(Collectors.toList());
    }
    
    public List<ReadingRecordResponse> findReadingRecordsByUserIdAndStatus(Long userId, ReadingStatus status) {
        List<ReadingRecord> readingRecords = getReadingRecordUseCase.findByUser_IdAndStatus(userId, status);
        return readingRecords.stream()
                .map(ReadingRecordResponse::from)
                .collect(Collectors.toList());
    }
    
    public List<ReadingRecordResponse> findReadingRecordsByBookId(Long bookId) {
        List<ReadingRecord> readingRecords = getReadingRecordUseCase.findByBook_Id(bookId);
        return readingRecords.stream()
                .map(ReadingRecordResponse::from)
                .collect(Collectors.toList());
    }
    
    public ReadingRecordResponse updateReadingRecord(Long readingRecordId, ReadingRecordUpdateRequest request) {
        return updateReadingRecordUseCase.execute(readingRecordId, request);
    }
    
    public ReadingRecordResponse completeReading(Long readingRecordId) {
        return completeReadingUseCase.execute(readingRecordId);
    }
    
    public void deleteReadingRecord(Long readingRecordId) {
        deleteReadingRecordUseCase.execute(readingRecordId);
    }
}