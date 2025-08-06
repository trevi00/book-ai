package com.bookapp.backend.web.analysis;

import com.bookapp.backend.application.analysis.AnalysisApplicationService;
import com.bookapp.backend.application.analysis.dto.AnalysisRequest;
import com.bookapp.backend.application.analysis.dto.AnalysisResponse;
import com.bookapp.backend.application.analysis.dto.DirectAnalysisRequest;
import com.bookapp.backend.domain.analysis.AnalysisType;
import com.bookapp.backend.web.common.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/analyses")
public class AnalysisController {

    private final AnalysisApplicationService analysisApplicationService;

    @Autowired
    public AnalysisController(AnalysisApplicationService analysisApplicationService) {
        this.analysisApplicationService = analysisApplicationService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<AnalysisResponse>> generateAnalysis(
            @Valid @RequestBody AnalysisRequest request) {
        try {
            AnalysisResponse response = analysisApplicationService.generateAnalysis(request);
            return ResponseEntity.ok(ApiResponse.success(response, "분석이 완료되었습니다"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("AI 분석 생성 중 오류가 발생했습니다: " + e.getMessage()));
        }
    }

    @PostMapping("/direct")
    public ResponseEntity<ApiResponse<AnalysisResponse>> generateDirectAnalysis(
            @Valid @RequestBody DirectAnalysisRequest request) {
        try {
            AnalysisResponse response = analysisApplicationService.generateDirectAnalysis(request);
            return ResponseEntity.ok(ApiResponse.success(response, "직접 분석이 완료되었습니다"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("AI 직접 분석 중 오류가 발생했습니다: " + e.getMessage()));
        }
    }

    @GetMapping("/{analysisId}")
    public ResponseEntity<ApiResponse<AnalysisResponse>> getAnalysisById(
            @PathVariable String analysisId) {
        try {
            AnalysisResponse response = analysisApplicationService.getAnalysisById(analysisId);
            return ResponseEntity.ok(ApiResponse.success(response));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(e.getMessage()));
        }
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<ApiResponse<List<AnalysisResponse>>> getAnalysesByUserId(
            @PathVariable Long userId) {
        List<AnalysisResponse> responses = analysisApplicationService.getAnalysesByUserId(userId);
        return ResponseEntity.ok(ApiResponse.success(responses));
    }

    @GetMapping("/book/{bookId}")
    public ResponseEntity<ApiResponse<List<AnalysisResponse>>> getAnalysesByBookId(
            @PathVariable Long bookId) {
        List<AnalysisResponse> responses = analysisApplicationService.getAnalysesByBookId(bookId);
        return ResponseEntity.ok(ApiResponse.success(responses));
    }

    @GetMapping("/user/{userId}/type/{analysisType}")
    public ResponseEntity<ApiResponse<List<AnalysisResponse>>> getAnalysesByUserIdAndType(
            @PathVariable Long userId,
            @PathVariable AnalysisType analysisType) {
        List<AnalysisResponse> responses = analysisApplicationService.getAnalysesByUserIdAndType(userId, analysisType);
        return ResponseEntity.ok(ApiResponse.success(responses));
    }

    @DeleteMapping("/{analysisId}")
    public ResponseEntity<ApiResponse<Void>> deleteAnalysis(
            @PathVariable String analysisId) {
        try {
            analysisApplicationService.deleteAnalysis(analysisId);
            return ResponseEntity.ok(ApiResponse.success(null));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(e.getMessage()));
        }
    }
}