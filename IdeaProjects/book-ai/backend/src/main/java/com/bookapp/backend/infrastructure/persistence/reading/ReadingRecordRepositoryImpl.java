package com.bookapp.backend.infrastructure.persistence.reading;

import com.bookapp.backend.domain.reading.ReadingRecord;
import com.bookapp.backend.domain.reading.ReadingRecordRepository;
import com.bookapp.backend.domain.reading.ReadingStatus;
import com.bookapp.backend.infrastructure.persistence.user.UserJpaRepository;
import com.bookapp.backend.infrastructure.persistence.book.BookJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class ReadingRecordRepositoryImpl implements ReadingRecordRepository {
    
    private final ReadingRecordJpaRepository readingRecordJpaRepository;
    private final UserJpaRepository userJpaRepository;
    private final BookJpaRepository bookJpaRepository;
    
    @Override
    public ReadingRecord save(ReadingRecord readingRecord) {
        // 관리되는 엔티티를 조회
        var userEntity = userJpaRepository.findById(readingRecord.getUserId())
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다"));
        var bookEntity = bookJpaRepository.findById(readingRecord.getBookId())
                .orElseThrow(() -> new RuntimeException("책을 찾을 수 없습니다"));
        
        // ReadingRecordEntity 직접 생성
        ReadingRecordEntity entity = ReadingRecordEntity.builder()
                .id(readingRecord.getId())
                .user(userEntity)
                .book(bookEntity)
                .content(readingRecord.getContent())
                .status(readingRecord.getStatus())
                .createdAt(readingRecord.getCreatedAt())
                .updatedAt(readingRecord.getUpdatedAt())
                .build();
        
        ReadingRecordEntity savedEntity = readingRecordJpaRepository.save(entity);
        return savedEntity.toDomain();
    }
    
    @Override
    public Optional<ReadingRecord> findById(Long id) {
        return readingRecordJpaRepository.findById(id)
                .map(ReadingRecordEntity::toDomain);
    }
    
    @Override
    public List<ReadingRecord> findByUser_Id(Long userId) {
        return readingRecordJpaRepository.findByUser_Id(userId)
                .stream()
                .map(ReadingRecordEntity::toDomain)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<ReadingRecord> findByUser_IdAndStatus(Long userId, ReadingStatus status) {
        return readingRecordJpaRepository.findByUser_IdAndStatus(userId, status)
                .stream()
                .map(ReadingRecordEntity::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<ReadingRecord> findByBook_Id(Long bookId) {
        return readingRecordJpaRepository.findByBook_IdOrderByCreatedAtDesc(bookId)
                .stream()
                .map(ReadingRecordEntity::toDomain)
                .collect(Collectors.toList());
    }
    
    @Override
    public Optional<ReadingRecord> findByUser_IdAndBook_Id(Long userId, Long bookId) {
        return readingRecordJpaRepository.findByUser_IdAndBook_Id(userId, bookId)
                .map(ReadingRecordEntity::toDomain);
    }

    @Override
    public boolean existsById(Long id) {
        return readingRecordJpaRepository.existsById(id);
    }
    
    @Override
    public void deleteById(Long id) {
        readingRecordJpaRepository.deleteById(id);
    }
}