# Spring Boot Profile Configuration Guide

이 문서는 Spring Boot 애플리케이션의 프로필별 설정과 사용법을 설명합니다.

## 📋 프로필 개요

| 프로필 | 데이터베이스 | 용도 | 설정 파일 |
|--------|-------------|------|-----------|
| `default` | H2 (In-Memory) | 로컬 개발 | `application.properties` |
| `staging` | MySQL | 스테이징 환경 | `application-staging.properties` |
| `prod` | MySQL | 운영 환경 | `application-prod.properties` |
| `test` | MySQL | 테스트 환경 | `application-test.properties` |

## 🚀 실행 방법

### 1. 개발 환경 (H2 Database)
```bash
# 기본 프로필 (H2)
./gradlew bootRun

# 또는 명시적으로 default 프로필 지정
./gradlew bootRun --args="--spring.profiles.active=default"

# H2 콘솔 접속: http://localhost:8080/api/h2-console
# JDBC URL: jdbc:h2:mem:book_ai_db
# Username: sa
# Password: (비어있음)
```

### 2. 스테이징 환경 (MySQL)
```bash
./gradlew bootRun --args="--spring.profiles.active=staging"
```

### 3. 운영 환경 (MySQL)
```bash
./gradlew bootRun --args="--spring.profiles.active=prod"
```

### 4. JAR 파일 실행
```bash
# 개발 환경
java -jar book-ai-backend-0.0.1-SNAPSHOT.jar

# 스테이징 환경
java -jar book-ai-backend-0.0.1-SNAPSHOT.jar --spring.profiles.active=staging

# 운영 환경
java -jar book-ai-backend-0.0.1-SNAPSHOT.jar --spring.profiles.active=prod
```

## 🔧 환경 변수 설정

### 운영/스테이징 환경 환경 변수

```bash
# 데이터베이스 설정
export DB_HOST=your-mysql-host
export DB_PORT=3306
export DB_NAME=book_ai_db
export DB_USERNAME=your-username
export DB_PASSWORD=your-password

# JWT 설정
export JWT_SECRET_KEY=your-very-secure-secret-key
export JWT_EXPIRATION_TIME=86400000

# AI 서비스 설정
export AI_SERVICE_URL=http://your-ai-service:8001
export AI_SERVICE_TIMEOUT=60000
```

### Docker 환경에서 실행
```bash
docker run -d \
  -p 8080:8080 \
  -e SPRING_PROFILES_ACTIVE=prod \
  -e DB_HOST=mysql-server \
  -e DB_NAME=book_ai_db \
  -e DB_USERNAME=root \
  -e DB_PASSWORD=your-password \
  -e JWT_SECRET_KEY=your-secret-key \
  book-ai-backend:latest
```

## 🗄️ 데이터베이스 설정 세부사항

### H2 (개발 환경)
- **타입**: In-Memory 데이터베이스
- **특징**: 
  - 애플리케이션 재시작 시 데이터 초기화
  - DDL: `create-drop` (자동 스키마 생성/삭제)
  - Flyway 비활성화
  - H2 Console 활성화
- **장점**: 빠른 개발, 별도 DB 설치 불필요

### MySQL (스테이징/운영)
- **특징**:
  - 영구 데이터 저장
  - DDL: `validate` (운영), `update` (스테이징)
  - Flyway 활성화 (DB 마이그레이션)
  - Connection Pool 최적화
- **장점**: 운영 환경과 동일한 DB 엔진

## 📊 로깅 레벨

| 프로필 | 애플리케이션 | SQL | Spring Security |
|--------|-------------|-----|-----------------|
| `default` | DEBUG | ON | DEBUG |
| `staging` | DEBUG | ON | INFO |
| `prod` | INFO | OFF | WARN |
| `test` | INFO | WARN | WARN |

## 🧪 테스트 실행

```bash
# 모든 테스트 실행 (MySQL 사용)
./gradlew test

# 특정 테스트 클래스만 실행
./gradlew test --tests "com.bookapp.backend.integration.*"
```

## 🔒 보안 고려사항

### 개발 환경
- H2 Console이 활성화되어 있으므로 운영 환경에서는 사용 금지
- 기본 JWT 시크릿 키 사용 (운영 환경에서는 반드시 변경)

### 운영 환경
- 모든 민감한 정보는 환경 변수로 관리
- H2 Console 비활성화
- SQL 로깅 비활성화
- 강력한 JWT 시크릿 키 사용

## 📂 설정 파일 위치

```
src/main/resources/
├── application.properties              # 기본 (H2)
├── application-staging.properties      # 스테이징 (MySQL)
├── application-prod.properties         # 운영 (MySQL)
└── db/migration/                      # Flyway 마이그레이션 파일

src/test/resources/
├── application-test.properties         # 테스트 (MySQL)
└── data.sql                           # 테스트 데이터
```

## 🚨 문제 해결

### H2 Console 접속 불가
- URL 확인: `http://localhost:8080/api/h2-console`
- JDBC URL: `jdbc:h2:mem:book_ai_db`

### MySQL 연결 실패
- MySQL 서버 실행 확인
- 환경 변수 설정 확인
- 방화벽 설정 확인

### Flyway 마이그레이션 오류
- 데이터베이스 스키마 버전 확인
- 마이그레이션 파일 순서 확인
- 수동으로 Flyway 기준선 설정: `spring.flyway.baseline-on-migrate=true`