package com.bookapp.backend.application.reading.usecase;

import com.bookapp.backend.application.reading.dto.ReadingRecordCreateRequest;
import com.bookapp.backend.application.reading.dto.ReadingRecordResponse;
import com.bookapp.backend.application.common.CurrentUserService;
import com.bookapp.backend.domain.book.Book;
import com.bookapp.backend.domain.book.BookRepository;
import com.bookapp.backend.domain.reading.ReadingRecord;
import com.bookapp.backend.domain.reading.ReadingRecordRepository;
import com.bookapp.backend.domain.user.User;
import com.bookapp.backend.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 독서 기록 생성 유스케이스
 * 
 * <h3>비즈니스 정책:</h3>
 * <ul>
 *   <li><strong>다중 독서기록 허용:</strong> 한 사용자가 같은 책에 대해 여러 독서기록을 생성할 수 있습니다.</li>
 *   <li><strong>권한 검증:</strong> 사용자는 본인이 소유한 책에만 독서기록을 생성할 수 있습니다.</li>
 *   <li><strong>초기 상태:</strong> 새로 생성된 독서기록은 기본적으로 'IN_PROGRESS' 상태로 설정됩니다.</li>
 * </ul>
 * 
 * <h3>변경 이력:</h3>
 * <ul>
 *   <li>2025-07-30: 중복 독서기록 검증 제거 - 다중 독서기록 허용 정책 적용</li>
 * </ul>
 */
@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class CreateReadingRecordUseCase {
    
    private final ReadingRecordRepository readingRecordRepository;
    private final UserRepository userRepository;
    private final BookRepository bookRepository;
    private final CurrentUserService currentUserService;
    
    public ReadingRecordResponse execute(ReadingRecordCreateRequest request) {
        Long currentUserId = currentUserService.getCurrentUserId();
        
        log.debug("독서 기록 생성 시작 - userId: {}, bookId: {}, content 길이: {}", 
                currentUserId, request.getBookId(), 
                request.getContent() != null ? request.getContent().length() : 0);
        
        try {
            // 독서 내용 검증 (선택적 - 비즈니스 정책에 따라)
            validateReadingContent(request.getContent());
            
            // 현재 사용자 조회
            User user = findUserById(currentUserId);
            
            // 사용자의 책인지 확인하면서 조회
            Book book = bookRepository.findByIdAndUserId(request.getBookId(), currentUserId)
                    .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 책이거나 접근 권한이 없습니다"));
            
            // 다중 독서기록 허용 정책에 따라 중복 검증 생략
            ReadingRecord readingRecord = ReadingRecord.builder()
                    .user(user)
                    .book(book)
                    .content(request.getContent())
                    .build();
            
            ReadingRecord savedRecord = readingRecordRepository.save(readingRecord);
            
            log.info("독서 기록 생성 완료 - userId: {}, bookId: {}, recordId: {}", 
                    currentUserId, book.getId(), savedRecord.getId());
            
            return ReadingRecordResponse.from(savedRecord);
            
        } catch (Exception e) {
            log.error("독서 기록 생성 실패 - userId: {}, bookId: {}, error: {}", 
                    currentUserId, request.getBookId(), e.getMessage(), e);
            throw e;
        }
    }
    
    /**
     * 독서 내용 검증
     * 비즈니스 정책: 빈 내용도 허용 (사용자가 나중에 추가할 수 있음)
     */
    private void validateReadingContent(String content) {
        // 현재는 모든 내용 허용 (null, 빈 문자열 포함)
        // 필요시 여기서 최소 길이나 내용 검증 추가 가능
        
        // 예시: 최대 길이 제한 (필요시 활성화)
        if (content != null && content.length() > 50000) {
            throw new IllegalArgumentException("독서 기록은 50,000자를 초과할 수 없습니다");
        }
    }
    
    private User findUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다"));
    }
}