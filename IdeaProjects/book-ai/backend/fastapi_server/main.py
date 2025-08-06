#!/usr/bin/env python3
"""
FastAPI AI Analysis Service for Book Application
완벽한 통합 테스트를 위한 실제 FastAPI 서버 구현
"""
import asyncio
import json
import logging
import time
from datetime import datetime
from typing import Dict, Any, Optional
from fastapi import FastAPI, HTTPException, BackgroundTasks
from fastapi.middleware.cors import CORSMiddleware
from pydantic import BaseModel, Field
import uvicorn

# 로깅 설정
logging.basicConfig(level=logging.INFO)
logger = logging.getLogger(__name__)

app = FastAPI(
    title="Book AI Analysis Service",
    description="AI-powered book analysis service for Spring Boot integration testing",
    version="1.0.0",
    docs_url="/docs",
    redoc_url="/redoc"
)

# CORS 설정
app.add_middleware(
    CORSMiddleware,
    allow_origins=["*"],
    allow_credentials=True,
    allow_methods=["*"],
    allow_headers=["*"],
)

# 요청/응답 모델 정의
class AIAnalysisRequest(BaseModel):
    user_id: str = Field(..., description="사용자 ID")
    book_id: str = Field(..., description="도서 ID")
    book_title: str = Field(..., description="도서 제목")
    book_author: str = Field(..., description="도서 저자")
    genre: str = Field(..., description="장르 (FICTION, TECHNOLOGY, SCIENCE, HISTORY, PHILOSOPHY, etc.)")
    reading_content: str = Field(..., description="분석할 읽기 내용")

class HealthResponse(BaseModel):
    status: str = Field(..., description="서비스 상태")
    timestamp: int = Field(..., description="응답 시간 (밀리초)")
    service: str = Field(..., description="서비스 이름")
    version: str = Field(..., description="서비스 버전")

class AnalysisResponse(BaseModel):
    success: bool = Field(..., description="분석 성공 여부")
    message: str = Field(..., description="응답 메시지")
    data: Dict[str, Any] = Field(..., description="분석 결과 데이터")
    timestamp: int = Field(..., description="응답 시간 (밀리초)")

class ErrorResponse(BaseModel):
    success: bool = Field(False, description="처리 성공 여부")
    message: str = Field(..., description="오류 메시지")
    error_code: str = Field(..., description="오류 코드")
    timestamp: int = Field(..., description="오류 발생 시간")

# 서비스 상태 관리
service_status = {
    "healthy": True,
    "maintenance_mode": False,
    "error_simulation": None,
    "response_delay": 0,
    "request_count": 0,
    "error_count": 0
}

# 장르별 분석 템플릿
ANALYSIS_TEMPLATES = {
    "FICTION": {
        "keywords": ["인물", "갈등", "주제", "문체", "서사", "감정"],
        "analysis_focus": "문학적 표현과 인물의 심리 분석",
        "sample_content": "이 소설은 인간의 복잡한 감정과 관계를 섬세하게 그려낸 작품입니다."
    },
    "TECHNOLOGY": {
        "keywords": ["기술", "구현", "방법론", "실무", "개발", "시스템"],
        "analysis_focus": "기술적 내용과 실무 활용성 분석",
        "sample_content": "이 기술서는 최신 개발 트렌드와 실무에 바로 적용할 수 있는 구체적인 방법들을 제시합니다."
    },
    "SCIENCE": {
        "keywords": ["과학", "연구", "이론", "실험", "발견", "원리"],
        "analysis_focus": "과학적 원리와 연구 방법론 분석",
        "sample_content": "과학적 사실을 바탕으로 한 체계적인 설명으로 복잡한 개념을 이해하기 쉽게 전달합니다."
    },
    "HISTORY": {
        "keywords": ["역사", "시대", "인물", "사건", "배경", "영향"],
        "analysis_focus": "역사적 맥락과 시대적 의미 분석",
        "sample_content": "역사적 사실을 통해 현재를 이해할 수 있는 통찰력을 제공하는 의미 있는 작품입니다."
    },
    "PHILOSOPHY": {
        "keywords": ["철학", "사상", "존재", "인식", "윤리", "가치"],
        "analysis_focus": "철학적 사고와 존재론적 의미 분석",
        "sample_content": "깊이 있는 철학적 탐구를 통해 인간 존재의 근본적 문제들을 다루고 있습니다."
    },
    "NON_FICTION": {
        "keywords": ["사실", "정보", "분석", "설명", "근거", "결론"],
        "analysis_focus": "사실적 정보와 논리적 구성 분석",
        "sample_content": "객관적 사실과 데이터를 바탕으로 한 신뢰할 수 있는 정보를 제공합니다."
    }
}

def generate_ai_analysis(request: AIAnalysisRequest) -> str:
    """AI 분석 결과 생성"""
    genre = request.genre.upper()
    template = ANALYSIS_TEMPLATES.get(genre, ANALYSIS_TEMPLATES["NON_FICTION"])
    
    # 내용 길이에 따른 분석 조정
    content_length = len(request.reading_content)
    
    if content_length == 0:
        return "분석할 내용이 제공되지 않았습니다. 더 많은 내용을 입력해 주세요."
    
    if content_length < 50:
        return f"'{request.book_title}'의 내용이 너무 짧습니다. {template['analysis_focus']}을 위해서는 더 많은 텍스트가 필요합니다."
    
    if content_length > 5000:
        analysis_detail = "매우 상세한 분석"
        depth_comment = "풍부한 내용을 바탕으로 한 깊이 있는"
    elif content_length > 1000:
        analysis_detail = "상세한 분석"
        depth_comment = "충분한 내용을 통한"
    else:
        analysis_detail = "기본 분석"
        depth_comment = "제공된 내용을 바탕으로 한"
    
    # 특수 문자 처리 확인
    has_special_chars = any(char in request.reading_content for char in ['"', "'", "@", "{", "}", "[", "]"])
    special_char_comment = " 특수 문자와 기호들이 적절히 처리되어" if has_special_chars else ""
    
    # 분석 결과 생성
    analysis = f"""
『{request.book_title}』({request.book_author} 저) - {genre} 장르 {analysis_detail}

{depth_comment} 분석 결과, 이 작품은 {template['sample_content']}

주요 특징:
• {template['analysis_focus']}이 잘 드러나 있습니다
• {', '.join(template['keywords'][:3])}과 같은 핵심 요소들이 체계적으로 다뤄졌습니다
• 텍스트 길이 {content_length}자로 적절한 분량의 내용이 제공되었습니다{special_char_comment}

종합 평가: 이 책은 {genre.lower()} 분야에서 독자들에게 유익한 통찰을 제공할 수 있는 가치 있는 작품으로 평가됩니다.
""".strip()
    
    return analysis

@app.get("/", response_model=Dict[str, str])
async def root():
    """루트 엔드포인트"""
    service_status["request_count"] += 1
    return {
        "service": "Book AI Analysis Service",
        "status": "running",
        "version": "1.0.0",
        "timestamp": str(datetime.now())
    }

@app.get("/api/v1/health/", response_model=HealthResponse)
async def health_check():
    """건강 상태 확인 엔드포인트"""
    service_status["request_count"] += 1
    
    # 오류 시뮬레이션 확인
    if service_status["error_simulation"] == "503":
        service_status["error_count"] += 1
        raise HTTPException(status_code=503, detail="Service temporarily unavailable")
    
    if service_status["error_simulation"] == "timeout":
        await asyncio.sleep(30)  # 30초 지연
    
    # 응답 지연 시뮬레이션
    if service_status["response_delay"] > 0:
        await asyncio.sleep(service_status["response_delay"])
    
    # 점검 모드 확인
    if service_status["maintenance_mode"]:
        raise HTTPException(
            status_code=503,
            detail={
                "error": "서버가 점검 중입니다. 잠시 후 다시 시도해주세요.",
                "error_code": "MAINTENANCE_MODE",
                "estimated_completion": "2024-12-25T15:00:00Z"
            }
        )
    
    # 건강하지 않은 상태 시뮬레이션
    if not service_status["healthy"]:
        raise HTTPException(status_code=503, detail="Service unhealthy")
    
    return HealthResponse(
        status="healthy",
        timestamp=int(time.time() * 1000),
        service="book-ai-fastapi",
        version="1.0.0"
    )

@app.post("/api/v1/analysis/generate", response_model=AnalysisResponse)
async def generate_analysis(request: AIAnalysisRequest):
    """AI 분석 생성 엔드포인트"""
    service_status["request_count"] += 1
    
    try:
        # 요청 유효성 검사
        if not request.user_id or not request.book_id:
            service_status["error_count"] += 1
            raise HTTPException(
                status_code=400,
                detail={
                    "success": False,
                    "message": "필수 필드가 누락되었습니다: user_id, book_id",
                    "error_code": "INVALID_REQUEST",
                    "timestamp": int(time.time() * 1000)
                }
            )
        
        # 오류 시뮬레이션 확인
        if service_status["error_simulation"] == "400":
            service_status["error_count"] += 1
            raise HTTPException(
                status_code=400,
                detail={
                    "success": False,
                    "message": "필수 필드가 누락되었습니다: reading_content",
                    "error_code": "INVALID_REQUEST",
                    "timestamp": int(time.time() * 1000)
                }
            )
        
        if service_status["error_simulation"] == "500":
            service_status["error_count"] += 1
            raise HTTPException(
                status_code=500,
                detail={
                    "success": False,
                    "message": "내부 서버 오류가 발생했습니다",
                    "error_code": "INTERNAL_ERROR",
                    "timestamp": int(time.time() * 1000)
                }
            )
        
        if service_status["error_simulation"] == "timeout":
            await asyncio.sleep(30)  # 30초 지연으로 타임아웃 유발
        
        # 응답 지연 시뮬레이션
        if service_status["response_delay"] > 0:
            await asyncio.sleep(service_status["response_delay"])
        
        # AI 분석 수행
        start_time = time.time()
        analysis_content = generate_ai_analysis(request)
        processing_time = int((time.time() - start_time) * 1000)
        
        # 분석 결과 반환
        return AnalysisResponse(
            success=True,
            message="분석이 성공적으로 완료되었습니다",
            data={
                "content": analysis_content,
                "analysis_type": "SUMMARY",
                "confidence": 0.95,
                "processing_time_ms": processing_time,
                "word_count": len(analysis_content.split()),
                "genre": request.genre,
                "book_info": {
                    "title": request.book_title,
                    "author": request.book_author,
                    "user_id": request.user_id,
                    "book_id": request.book_id
                }
            },
            timestamp=int(time.time() * 1000)
        )
        
    except HTTPException:
        raise
    except Exception as e:
        service_status["error_count"] += 1
        logger.error(f"Unexpected error in analysis generation: {str(e)}")
        raise HTTPException(
            status_code=500,
            detail={
                "success": False,
                "message": f"예상치 못한 오류가 발생했습니다: {str(e)}",
                "error_code": "UNEXPECTED_ERROR",
                "timestamp": int(time.time() * 1000)
            }
        )

@app.get("/api/v1/status", response_model=Dict[str, Any])
async def get_service_status():
    """서비스 상태 조회"""
    return {
        "service_status": service_status,
        "uptime": time.time(),
        "current_time": datetime.now().isoformat()
    }

@app.post("/api/v1/admin/simulate-error")
async def simulate_error(error_type: str, duration: Optional[int] = None):
    """테스트를 위한 오류 시뮬레이션"""
    if error_type == "reset":
        service_status["error_simulation"] = None
        service_status["healthy"] = True
        service_status["maintenance_mode"] = False
        service_status["response_delay"] = 0
        return {"message": "All simulations reset"}
    
    if error_type == "503":
        service_status["error_simulation"] = "503"
        service_status["healthy"] = False
    elif error_type == "500":
        service_status["error_simulation"] = "500"
    elif error_type == "400":
        service_status["error_simulation"] = "400"
    elif error_type == "timeout":
        service_status["error_simulation"] = "timeout"
    elif error_type == "maintenance":
        service_status["maintenance_mode"] = True
    elif error_type == "delay":
        service_status["response_delay"] = duration or 2
    
    return {"message": f"Error simulation '{error_type}' activated"}

@app.post("/api/v1/admin/set-delay")
async def set_response_delay(delay_seconds: float):
    """응답 지연 설정"""
    service_status["response_delay"] = delay_seconds
    return {"message": f"Response delay set to {delay_seconds} seconds"}

if __name__ == "__main__":
    print("Starting FastAPI AI Analysis Service for Integration Testing")
    print("Service Features:")
    print("   - AI-powered book analysis")
    print("   - Health check endpoints")
    print("   - Error simulation for testing")
    print("   - Multi-genre support")
    print("   - Comprehensive request/response validation")
    print("   - Performance monitoring")
    print("\nAvailable endpoints:")
    print("   - GET  /api/v1/health/ - Health check")
    print("   - POST /api/v1/analysis/generate - AI analysis")
    print("   - GET  /api/v1/status - Service status")
    print("   - POST /api/v1/admin/simulate-error - Error simulation")
    print("   - GET  /docs - API documentation")
    print("\nReady for Spring Boot integration testing!")
    
    uvicorn.run(
        "main:app",
        host="127.0.0.1",
        port=8000,
        reload=True,
        log_level="info",
        access_log=True
    )