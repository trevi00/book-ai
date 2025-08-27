# BookAI - AI 기반 도서 분석 플랫폼

AI가 분석하는 개인화된 독서 인사이트와 체계적인 독서 관리를 제공하는 풀스택 웹 애플리케이션

## 프로젝트 개요

BookAI는 사용자의 독서 기록을 바탕으로 AI가 생성하는 맞춤형 도서 분석 서비스입니다. 장르별 전문 분석기를 통해 문학작품은 깊이 있는 감상평을, 기술서적은 실무 중심의 학습 가이드를 제공합니다.

### 주요 특징

- **장르별 AI 분석**: 문학/기술서별 전문화된 분석 알고리즘
- **스마트 캐싱**: 중복 분석 방지로 효율적인 리소스 활용
- **안전한 인증**: JWT 기반 사용자별 데이터 보호
- **멀티 플랫폼**: 웹, 모바일 앱 지원
- **Clean Architecture**: 유지보수 용이한 모듈화 설계

## 시스템 아키텍처

```
Frontend (React 18 + TypeScript)
            ↓ REST API
Backend (Spring Boot 3.5.4 + Java 21)
            ↓ HTTP API  
AI Service (FastAPI + Python)
            ↓
Database (MySQL 8.0)
```

### 프로젝트 구조

```
book-ai/
├── backend/                 # Spring Boot 백엔드
│   ├── src/main/java/      # Java 소스코드
│   │   └── com.bookapp.backend/
│   │       ├── application/     # 비즈니스 로직
│   │       ├── domain/         # 도메인 모델
│   │       ├── infrastructure/ # 외부 연동
│   │       └── web/           # REST 컨트롤러
│   └── src/test/           # 테스트 코드 (239개)
├── ai-service/             # FastAPI AI 서비스
│   └── working_fastapi.py  # AI 분석 엔진
├── frontend-react/         # React 프론트엔드
├── mobile-app/            # React Native 모바일
└── docs/                  # 프로젝트 문서
```

## 설치 및 실행

### 사전 요구사항

- Java 21
- Node.js 16+
- Python 3.10+
- MySQL 8.0
- OpenAI API 키

### 1. 데이터베이스 설정

MySQL 서버를 실행하고 데이터베이스를 생성합니다:

```bash
# Docker로 MySQL 실행
docker run -d --name mysql-bookai \
  -e MYSQL_ROOT_PASSWORD=1846 \
  -e MYSQL_DATABASE=book_ai_db \
  -p 3306:3306 mysql:8.0

# 또는 로컬 MySQL에서 데이터베이스 생성
mysql -u root -p
CREATE DATABASE book_ai_db;
```

### 2. 백엔드 실행 (Spring Boot)

```bash
cd backend

# 환경변수 설정 (Windows)
set DB_HOST=localhost
set DB_PORT=3306
set DB_NAME=book_ai_db
set DB_USERNAME=root
set DB_PASSWORD=1846
set JWT_SECRET=your_very_long_and_secure_jwt_secret_key_at_least_256_bits_long_for_hs512_algorithm
set AI_SERVICE_BASE_URL=http://localhost:8000

# 또는 Linux/Mac
export DB_PASSWORD=1846
export JWT_SECRET=your_very_long_and_secure_jwt_secret_key_at_least_256_bits_long_for_hs512_algorithm

# 애플리케이션 실행
./gradlew bootRun
```

백엔드가 http://localhost:8080 에서 실행됩니다.

### 3. AI 서비스 실행 (FastAPI)

```bash
cd ai-service

# Python 의존성 설치
pip install fastapi uvicorn openai python-multipart

# OpenAI API 키 설정 (Windows)
set OPENAI_API_KEY=your_openai_api_key_here

# 또는 Linux/Mac
export OPENAI_API_KEY=your_openai_api_key_here

# AI 서비스 실행
python working_fastapi.py
```

AI 서비스가 http://localhost:8000 에서 실행됩니다.
API 문서는 http://localhost:8000/docs 에서 확인할 수 있습니다.

### 4. 프론트엔드 실행 (React)

```bash
cd frontend-react

# Node.js 의존성 설치
npm install

# 개발 서버 실행
npm run dev
```

프론트엔드가 http://localhost:5173 에서 실행됩니다.

### 5. 전체 시스템 실행 순서

1. **MySQL 데이터베이스 실행** (포트 3306)
2. **AI 서비스 실행** (포트 8000)
3. **백엔드 API 실행** (포트 8080)
4. **프론트엔드 실행** (포트 5173)

### 6. 실행 확인

각 서비스가 정상 실행되었는지 확인:

```bash
# 백엔드 헬스체크
curl http://localhost:8080/api/health

# AI 서비스 헬스체크
curl http://localhost:8000/api/v1/health/

# 웹 애플리케이션 접속
open http://localhost:5173
```

## 핵심 기능

### 도서 관리
- 도서 등록/수정/삭제 (ISBN 선택사항)
- 제목/저자 기반 검색
- 장르별 필터링
- 개인 도서관 관리

### AI 분석 시스템

#### 문학작품 분석
- 작품 세계 탐구 (세계관, 인물 심리, 상징)
- 문학적 기법과 스타일 분석
- 현대적 의미와 연결점
- 추천 도서 큐레이션

#### 기술서적 분석  
- 핵심 개념의 실무적 가치 평가
- 실습 가이드 및 코드 예시
- 기술 생태계 연관성 분석
- 커리어 성장 로드맵 제시

### 독서 기록 관리
- 상세한 독서 노트 작성
- 진도 추적 및 완독 관리
- 개인화된 독서 통계
- 독서 히스토리 관리

## 기술 스택

### Backend
- **Framework**: Spring Boot 3.5.4
- **Language**: Java 21
- **Security**: Spring Security + JWT
- **Database**: MySQL 8.0, Spring Data JPA
- **Testing**: JUnit 5, Mockito, TestContainers
- **Build**: Gradle

### AI Service  
- **Framework**: FastAPI
- **Language**: Python 3.10
- **AI Model**: OpenAI GPT-3.5-turbo
- **Vector DB**: ChromaDB (준비 중)

### Frontend
- **Framework**: React 18
- **Language**: TypeScript
- **Build**: Vite
- **Styling**: Tailwind CSS
- **State**: Zustand
- **Testing**: Vitest, React Testing Library

### Mobile
- **Framework**: React Native 0.72
- **Navigation**: React Navigation 6
- **State**: Redux Toolkit
- **Storage**: AsyncStorage, Keychain

## API 문서

### 주요 엔드포인트

#### 인증
```
POST /api/auth/register - 회원가입
POST /api/auth/login    - 로그인
```

#### 도서 관리
```
GET    /api/books       - 도서 목록 조회
POST   /api/books       - 도서 등록
GET    /api/books/{id}  - 도서 상세 조회  
PUT    /api/books/{id}  - 도서 정보 수정
DELETE /api/books/{id}  - 도서 삭제
```

#### 분석
```
POST /api/analysis/generate - AI 분석 생성
GET  /api/analysis/{id}     - 분석 결과 조회
```

## 테스트 실행

```bash
# 백엔드 테스트
cd backend && ./gradlew test

# 프론트엔드 테스트  
cd frontend-react && npm test

# AI 서비스 테스트
cd ai-service && python -m pytest
```

총 269개 테스트 (100% 성공률)

## 문제 해결

### 자주 발생하는 문제

1. **MySQL 연결 실패**
   - MySQL 서비스가 실행 중인지 확인
   - 포트 3306이 사용 중인지 확인
   - 환경변수 DB_PASSWORD가 올바른지 확인

2. **OpenAI API 에러**
   - OPENAI_API_KEY 환경변수가 설정되었는지 확인
   - API 키가 유효한지 확인
   - OpenAI 계정에 충분한 크레딧이 있는지 확인

3. **포트 충돌**
   - 다른 서비스가 8080, 8000, 3306 포트를 사용하고 있는지 확인
   - 필요시 application.properties에서 포트 변경

4. **JWT 토큰 에러**
   - JWT_SECRET 환경변수가 충분히 긴지 확인 (256비트 이상)
