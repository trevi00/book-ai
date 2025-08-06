# Book AI Analysis Service

📚 책 독서 기록 AI 분석 서비스

## 개요

이 서비스는 사용자의 독서 기록을 바탕으로 AI가 책에 대한 심층적인 분석을 제공하는 FastAPI 기반 서비스입니다.

## 주요 기능

### 🎯 AI 분석 서비스
- **문학 작품 분석**: 감정 분석, 작품 분석, 배경 지식, 추천 도서
- **기술서적 요약**: 핵심 개념 정리, 코드 예시, 심화 학습 가이드

### 🧠 RAG (Retrieval-Augmented Generation)
- ChromaDB를 이용한 벡터 데이터베이스
- 책 관련 지식 검색 및 컨텍스트 제공

### 🔧 기술 스택
- **Framework**: FastAPI
- **AI**: OpenAI GPT-3.5-turbo
- **Vector DB**: ChromaDB
- **Language**: Python 3.10+

## API 엔드포인트

### 분석 API
```
POST /api/v1/analysis/generate
```
독서 기록을 바탕으로 AI 분석을 생성합니다.

**요청 예시:**
```json
{
    "user_id": "user123",
    "book_id": "book456",
    "book_title": "1984",
    "book_author": "조지 오웰",
    "genre": "LITERATURE",
    "reading_content": "전체주의 사회의 무서움을 보여주는 작품이다. 빅 브라더의 감시 체계가 인상적이었다."
}
```

**응답 예시:**
```json
{
    "success": true,
    "data": {
        "analysis_id": "uuid-string",
        "user_id": "user123",
        "book_id": "book456",
        "analysis_type": "LITERATURE_ANALYSIS",
        "content": "## 감정 분석\n- 작품에서 느끼는 불안감과 경계심...",
        "created_at": "2025-01-25T10:00:00"
    },
    "message": "분석이 완료되었습니다"
}
```

### 분석 조회 API
```
GET /api/v1/analysis/{analysis_id}
```

### 헬스 체크 API
```
GET /api/v1/health/
```

## 설치 및 실행

### 1. 의존성 설치
```bash
pip install -r requirements.txt
```

### 2. 환경 변수 설정
`.env` 파일을 생성하고 다음 변수들을 설정합니다:

```env
# OpenAI Configuration
OPENAI_API_KEY=your_openai_api_key_here

# ChromaDB Configuration
CHROMA_PERSIST_DIRECTORY=./data/vector_db

# Application Configuration
API_PORT=8001
LOG_LEVEL=INFO
ENVIRONMENT=development

# CORS Configuration
CORS_ORIGINS=http://localhost:3000,http://localhost:8080
```

### 3. 서버 실행
```bash
# 개발 모드
python -m uvicorn app.main:app --reload --host 0.0.0.0 --port 8001

# 또는 직접 실행
python app/main.py
```

## 테스트

```bash
# 모든 테스트 실행
python -m pytest tests/ -v

# 특정 테스트 파일 실행
python -m pytest tests/test_main.py -v

# 커버리지와 함께 실행
python -m pytest tests/ --cov=app --cov-report=html
```

## API 문서

서버 실행 후 다음 URL에서 API 문서를 확인할 수 있습니다:

- **Swagger UI**: http://localhost:8001/docs
- **ReDoc**: http://localhost:8001/redoc

## 프로젝트 구조

```
ai-service/
├── app/
│   ├── __init__.py
│   ├── main.py                 # FastAPI 애플리케이션 진입점
│   ├── api/
│   │   └── v1/
│   │       ├── analysis.py     # 분석 API 엔드포인트
│   │       └── health.py       # 헬스체크 엔드포인트
│   ├── core/
│   │   └── config.py          # 설정 관리
│   ├── models/
│   │   ├── analysis.py        # 분석 관련 데이터 모델
│   │   └── response.py        # API 응답 모델
│   └── services/
│       ├── literature_analyzer.py  # 문학 작품 분석기
│       ├── technical_analyzer.py   # 기술서적 분석기
│       ├── openai_service.py       # OpenAI API 서비스
│       └── rag_service.py          # RAG 서비스
├── tests/                     # 테스트 파일들
├── data/                      # 데이터 저장소
├── requirements.txt           # Python 의존성
├── .env                      # 환경 변수 (로컬)
└── README.md                 # 프로젝트 문서
```

## 개발 가이드

### 코드 스타일
```bash
# 코드 포맷팅
black app/ tests/

# Import 정렬
isort app/ tests/

# 린팅
flake8 app/ tests/
```

### 새로운 분석기 추가
1. `app/services/` 디렉토리에 새 분석기 클래스 생성
2. `BaseAnalyzer` 인터페이스 구현
3. `app/api/v1/analysis.py`에서 새 분석기 등록
4. 테스트 코드 작성

## 배포

### Docker 사용 (예정)
```bash
# 이미지 빌드
docker build -t book-ai-service .

# 컨테이너 실행
docker run -p 8001:8001 --env-file .env book-ai-service
```

## 라이센스

MIT License

## 기여

1. Fork the Project
2. Create your Feature Branch (`git checkout -b feature/AmazingFeature`)
3. Commit your Changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the Branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

## 문의

프로젝트에 대한 문의사항이 있으시면 이슈를 생성해주세요.