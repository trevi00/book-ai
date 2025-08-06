#!/usr/bin/env python3
from fastapi import FastAPI, HTTPException
from fastapi.middleware.cors import CORSMiddleware
from pydantic import BaseModel, Field
from openai import OpenAI
from enum import Enum
from datetime import datetime
import uuid
import uvicorn
import logging

# 로깅 설정
logging.basicConfig(level=logging.INFO)
logger = logging.getLogger(__name__)

# OpenAI 클라이언트 초기화
OPENAI_CLIENT = OpenAI(api_key="your_openai_api_key_here")

logger.info("OpenAI client initialized successfully")

# FastAPI 앱 생성
app = FastAPI(
    title="Book AI Analysis Service",
    description="책 독서 기록 AI 분석 서비스",
    version="1.0.0"
)

# CORS 설정
app.add_middleware(
    CORSMiddleware,
    allow_origins=["*"],
    allow_credentials=True,
    allow_methods=["*"],
    allow_headers=["*"],
)

# 데이터 모델
class Genre(str, Enum):
    TECHNICAL = "TECHNICAL"
    LITERATURE = "LITERATURE"

class AnalysisType(str, Enum):
    LITERATURE_ANALYSIS = "LITERATURE_ANALYSIS"
    TECHNICAL_SUMMARY = "TECHNICAL_SUMMARY"

class AnalysisRequest(BaseModel):
    user_id: str = Field(alias="user_id")
    book_id: str = Field(alias="book_id")
    book_title: str = Field(alias="book_title")
    book_author: str = Field(alias="book_author")
    genre: Genre
    reading_content: str = Field(alias="reading_content")
    
    class Config:
        populate_by_name = True

class AnalysisResponse(BaseModel):
    analysis_id: str
    user_id: str
    book_id: str
    analysis_type: AnalysisType
    content: str
    created_at: datetime

class APIResponse(BaseModel):
    success: bool
    data: AnalysisResponse = None
    message: str = None
    error_code: str = None

# 헬스체크 엔드포인트
@app.get("/api/v1/health/")
async def health_check():
    return {
        "success": True,
        "data": {
            "status": "UP",
            "timestamp": datetime.now().isoformat(),
            "service": "book-ai-analysis-service"
        },
        "message": "서비스가 정상 작동 중입니다",
        "error_code": None
    }

# AI 분석 생성 엔드포인트
@app.post("/api/v1/analysis/generate", response_model=dict)
async def generate_analysis(request: AnalysisRequest):
    """독서 기록 AI 분석 생성"""
    try:
        logger.info(f"분석 요청 수신: 사용자 {request.user_id}, 책 {request.book_title}")
        
        # 장르에 따른 프롬프트 생성
        if request.genre == Genre.LITERATURE:
            prompt = f"""
다음은 사용자의 독서 기록입니다:

책 제목: {request.book_title}
저자: {request.book_author}
독서 기록: {request.reading_content}

이 문학 작품에 대한 심도 있는 분석을 다음 구조로 작성해주세요:

## 📖 독서 여정 분석
- 독서 기록에서 드러나는 독자의 관점과 감정 변화
- 작품이 독자에게 미친 구체적인 영향과 깨달음
- 개인적 경험과 연결되는 부분들

## 🎭 작품 핵심 분석
- 주요 주제와 메시지의 현대적 해석
- 인물의 심리적 동기와 갈등 구조
- 상징과 은유의 의미와 효과
- 서사 구조와 문학적 기법의 특징

## 🌍 문화적 맥락
- 작품이 쓰인 시대적 배경과 사회적 의미
- 보편적 인간 경험으로서의 가치
- 현대 독자들이 공감할 수 있는 지점

## 💡 통찰과 성찰
- 이 작품을 통해 얻을 수 있는 인생의 지혜
- 독자의 세계관 확장에 기여하는 부분
- 일상에 적용할 수 있는 교훈

## 📚 연관 독서 가이드
- 비슷한 주제의 추천 작품 3권 (이유와 함께)
- 저자의 다른 작품 중 읽어볼 만한 책
- 관련 장르나 문학사적 맥락의 작품들

각 섹션은 구체적이고 실용적인 내용으로 작성하되, 독자의 지적 호기심을 자극하고 더 깊이 있는 독서를 유도하는 방향으로 작성해주세요.
"""
            analysis_type = AnalysisType.LITERATURE_ANALYSIS
        else:  # TECHNICAL
            prompt = f"""
다음은 사용자의 기술서적 독서 기록입니다:

책 제목: {request.book_title}
저자: {request.book_author}
독서 기록: {request.reading_content}

이 기술서적에 대한 실무 중심의 종합 분석을 다음 구조로 작성해주세요:

## 🔑 핵심 개념 마스터리
- 독서 기록에서 언급된 핵심 기술 개념들의 정확한 이해도 평가
- 각 개념이 실무에서 해결하는 구체적인 문제들
- 기존 방식 대비 이 기술/방법론의 장단점
- 학습자가 놓치기 쉬운 중요한 디테일들

## 💻 실무 적용 전략
- 현재 프로젝트에 바로 적용할 수 있는 구체적인 방법
- 도입 시 예상되는 어려움과 해결 방안
- 팀 단위 도입 시 고려사항과 단계별 접근법
- 성과 측정 방법과 개선 지표

## 🛠️ 실전 코드 가이드
- 책의 이론을 바탕으로 한 실제 구현 예시
- 일반적인 실수와 디버깅 팁
- 성능 최적화 포인트
- 확장성과 유지보수성을 고려한 설계 원칙

## 🚀 커리어 성장 로드맵
- 이 기술 영역에서의 전문성 발전 방향
- 관련 기술 스택과의 시너지 효과
- 업계 트렌드와 미래 전망
- 포트폴리오 프로젝트 아이디어

## 📖 학습 리소스 큐레이션
- 심화 학습을 위한 추천 도서 3권 (난이도별)
- 실습 중심의 온라인 코스와 문서
- 커뮤니티와 실무진들이 활용하는 레퍼런스
- 최신 업데이트와 베스트 프랙티스를 확인할 수 있는 소스

각 섹션은 즉시 실무에 활용할 수 있는 실용적 내용으로 구성하되, 개발자의 기술적 성장과 문제 해결 능력 향상에 직접 도움이 되도록 작성해주세요.
"""
            analysis_type = AnalysisType.TECHNICAL_SUMMARY
        
        # OpenAI API 호출
        logger.info("OpenAI API 호출 시작")
        response = OPENAI_CLIENT.chat.completions.create(
            model="gpt-3.5-turbo",
            messages=[
                {"role": "system", "content": "당신은 책 분석 전문가입니다. 사용자의 독서 기록을 바탕으로 통찰력 있는 분석을 제공합니다."},
                {"role": "user", "content": prompt}
            ],
            max_tokens=1500,
            temperature=0.7
        )
        
        analysis_content = response.choices[0].message.content.strip()
        logger.info(f"OpenAI API 호출 성공, 분석 길이: {len(analysis_content)}")
        
        # 분석 결과 응답 생성
        analysis_response = AnalysisResponse(
            analysis_id=str(uuid.uuid4()),
            user_id=request.user_id,
            book_id=request.book_id,
            analysis_type=analysis_type,
            content=analysis_content,
            created_at=datetime.now()
        )
        
        logger.info(f"분석 완료: {analysis_response.analysis_id}")
        
        return {
            "success": True,
            "data": analysis_response.dict(),
            "message": "분석이 완료되었습니다",
            "error_code": None
        }
        
    except Exception as e:
        logger.error(f"분석 생성 실패: {str(e)}")
        raise HTTPException(status_code=500, detail=f"분석 생성 중 오류가 발생했습니다: {str(e)}")

# 루트 엔드포인트
@app.get("/")
async def root():
    return {
        "service": "Book AI Analysis Service",
        "version": "1.0.0",
        "status": "running",
        "port": 8000
    }

if __name__ == "__main__":
    logger.info("FastAPI 서버 시작 - 포트 8000")
    uvicorn.run(app, host="0.0.0.0", port=8000)