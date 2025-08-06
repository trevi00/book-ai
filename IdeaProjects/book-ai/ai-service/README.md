# Book AI Analysis Service

독서 기록을 기반으로 AI 분석을 제공하는 FastAPI 서비스입니다.

## 기능

- 문학 소설 독서 기록 분석 (감정 분석, 작품 해석, 추천 도서)
- 기술서적 독서 기록 정리 (핵심 개념, 코드 예시, 학습 가이드)
- RAG(Retrieval Augmented Generation)를 통한 컨텍스트 기반 분석
- OpenAI GPT 모델을 활용한 고품질 분석 생성

## 기술 스택

- **Framework**: FastAPI
- **AI Model**: OpenAI GPT-3.5-turbo
- **Vector Database**: ChromaDB
- **Embeddings**: OpenAI text-embedding-ada-002

## 설치 및 실행

### 로컬 개발 환경

1. 의존성 설치
```bash
pip install -r requirements.txt
```

2. 환경 변수 설정
```bash
cp .env.example .env
# .env 파일에서 OPENAI_API_KEY 설정
```

3. 서비스 실행
```bash
python -m app.main
```

### Docker 실행

1. Docker 이미지 빌드
```bash
docker build -t book-ai-service .
```

2. 컨테이너 실행
```bash
docker run -d -p 8001:8001 --env-file .env book-ai-service
```

## API 엔드포인트

### 분석 생성
```
POST /api/v1/analysis/generate
```

**요청 예시:**
```json
{
  "user_id": "1",
  "book_id": "1",
  "book_title": "Clean Code",
  "book_author": "Robert C. Martin",
  "genre": "TECHNICAL",
  "reading_content": "클린 코드의 중요성에 대해 학습했습니다..."
}
```

### 헬스체크
```
GET /api/v1/health/
```

## 환경 변수

- `OPENAI_API_KEY`: OpenAI API 키 (필수)
- `CHROMA_PERSIST_DIRECTORY`: ChromaDB 저장 경로 (기본값: ./data/vector_db)
- `API_PORT`: API 서버 포트 (기본값: 8001)
- `LOG_LEVEL`: 로그 레벨 (기본값: INFO)

## 개발 도구

- API 문서: http://localhost:8001/docs
- Redoc 문서: http://localhost:8001/redoc