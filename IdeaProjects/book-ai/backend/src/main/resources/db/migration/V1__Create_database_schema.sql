-- Book AI Platform Database Schema
-- 윈도우 네이티브 환경 MySQL 8.0+

-- V1을 대체하는 통합 스키마

-- 1. users 테이블
CREATE TABLE users (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    nickname VARCHAR(100) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- 2. books 테이블
CREATE TABLE books (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    title VARCHAR(500) NOT NULL,
    author VARCHAR(200) NOT NULL,
    isbn VARCHAR(13) UNIQUE,
    genre ENUM('TECHNICAL', 'LITERATURE') NOT NULL,
    description TEXT,
    content LONGTEXT,
    user_id BIGINT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    
    -- Foreign Key Constraints
    CONSTRAINT fk_books_user_id 
        FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

-- 3. reading_records 테이블
CREATE TABLE reading_records (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    book_id BIGINT NOT NULL,
    content LONGTEXT NOT NULL,
    status ENUM('IN_PROGRESS', 'COMPLETED') DEFAULT 'IN_PROGRESS',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    
    -- Foreign Key Constraints
    CONSTRAINT fk_reading_records_user_id 
        FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    CONSTRAINT fk_reading_records_book_id 
        FOREIGN KEY (book_id) REFERENCES books(id) ON DELETE CASCADE
);

-- 4. ai_analyses 테이블
CREATE TABLE ai_analyses (
    analysis_id VARCHAR(36) PRIMARY KEY,
    user_id BIGINT NOT NULL,
    book_id BIGINT NOT NULL,
    analysis_type ENUM('LITERATURE_ANALYSIS', 'TECHNICAL_SUMMARY') NOT NULL,
    content TEXT NOT NULL,
    created_at TIMESTAMP NOT NULL,
    
    -- Foreign Key Constraints
    CONSTRAINT fk_ai_analyses_user_id 
        FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    CONSTRAINT fk_ai_analyses_book_id 
        FOREIGN KEY (book_id) REFERENCES books(id) ON DELETE CASCADE
);

-- 인덱스 생성 (성능 최적화)
-- 이미 생성된 인덱스: PRIMARY KEY, UNIQUE 제약조건

-- 검색 성능을 위한 인덱스
CREATE INDEX idx_books_user_id ON books(user_id);
CREATE INDEX idx_books_user_genre ON books(user_id, genre);
CREATE INDEX idx_books_user_title ON books(user_id, title);
CREATE INDEX idx_reading_records_user_id ON reading_records(user_id);
CREATE INDEX idx_reading_records_book_id ON reading_records(book_id);
CREATE INDEX idx_reading_records_status ON reading_records(status);
CREATE INDEX idx_reading_records_user_status ON reading_records(user_id, status);

-- 복합 인덱스 (자주 함께 조회되는 컬럼들)
CREATE INDEX idx_books_genre_title ON books(genre, title);
CREATE INDEX idx_users_email_created ON users(email, created_at);

-- Full-text search를 위한 인덱스 (책 제목, 저자 검색용)
CREATE FULLTEXT INDEX idx_books_search ON books(title, author, description);

-- 통계 및 분석을 위한 인덱스
CREATE INDEX idx_reading_records_created_at ON reading_records(created_at);
CREATE INDEX idx_ai_analyses_type_created ON ai_analyses(analysis_type, created_at);