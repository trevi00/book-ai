package com.bookapp.backend.web.reading;

import com.bookapp.backend.application.reading.ReadingApplicationService;
import com.bookapp.backend.application.reading.dto.ReadingRecordCreateRequest;
import com.bookapp.backend.application.reading.dto.ReadingRecordResponse;
import com.bookapp.backend.application.reading.dto.ReadingRecordUpdateRequest;
import com.bookapp.backend.domain.reading.ReadingStatus;
import com.bookapp.backend.web.common.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reading-records")
@RequiredArgsConstructor
public class ReadingController {

    private final ReadingApplicationService readingApplicationService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<ReadingRecordResponse> createReadingRecord(@Valid @RequestBody ReadingRecordCreateRequest request) {
        ReadingRecordResponse response = readingApplicationService.createReadingRecord(request);
        return ApiResponse.success(response, "독서 기록이 생성되었습니다");
    }

    @PutMapping("/{id}")
    public ApiResponse<ReadingRecordResponse> updateReadingRecord(
            @PathVariable Long id,
            @Valid @RequestBody ReadingRecordUpdateRequest request) {
        ReadingRecordResponse response = readingApplicationService.updateReadingRecord(id, request);
        return ApiResponse.success(response, "독서 기록이 수정되었습니다");
    }

    @PostMapping("/{id}/complete")
    public ApiResponse<ReadingRecordResponse> completeReading(@PathVariable Long id) {
        ReadingRecordResponse response = readingApplicationService.completeReading(id);
        return ApiResponse.success(response, "독서가 완료되었습니다");
    }

    @GetMapping("/{id}")
    public ApiResponse<ReadingRecordResponse> getReadingRecord(@PathVariable Long id) {
        ReadingRecordResponse response = readingApplicationService.findReadingRecordById(id);
        return ApiResponse.success(response);
    }

    @GetMapping("/user/{userId}")
    public ApiResponse<List<ReadingRecordResponse>> getReadingRecordsByUser(@PathVariable Long userId) {
        List<ReadingRecordResponse> responses = readingApplicationService.findReadingRecordsByUserId(userId);
        return ApiResponse.success(responses);
    }

    @GetMapping("/user/{userId}/status/{status}")
    public ApiResponse<List<ReadingRecordResponse>> getReadingRecordsByUserAndStatus(
            @PathVariable Long userId,
            @PathVariable ReadingStatus status) {
        List<ReadingRecordResponse> responses = readingApplicationService.findReadingRecordsByUserIdAndStatus(userId, status);
        return ApiResponse.success(responses);
    }

    @GetMapping("/book/{bookId}")
    public ApiResponse<List<ReadingRecordResponse>> getReadingRecordsByBook(@PathVariable Long bookId) {
        List<ReadingRecordResponse> responses = readingApplicationService.findReadingRecordsByBookId(bookId);
        return ApiResponse.success(responses);
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteReadingRecord(@PathVariable Long id) {
        readingApplicationService.deleteReadingRecord(id);
        return ApiResponse.success(null, "독서 기록이 삭제되었습니다");
    }
}