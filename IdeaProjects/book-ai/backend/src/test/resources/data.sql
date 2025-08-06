-- Test data for integration tests (H2 compatible)

-- Users 
INSERT INTO users (id, email, password, nickname, created_at, updated_at) VALUES
(1, 'test@example.com', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2uheWG/igi.', 'testuser', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(2, 'test2@example.com', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2uheWG/igi.', 'testuser2', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Books
INSERT INTO books (id, title, author, isbn, genre, description, content, user_id, created_at, updated_at) VALUES
(1, '1984', 'George Orwell', '9788937460773', 'FICTION', 'Dystopian novel about totalitarian society', 'Full book content here...', 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(2, 'Clean Code', 'Robert C. Martin', '9788966260959', 'TECHNOLOGY', 'Book about clean code methodology', 'Full book content here...', 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(3, 'Harry Potter', 'J.K. Rowling', '9788983920775', 'FICTION', 'Adventure story of wizard Harry Potter', 'Full book content here...', 2, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Reading Records
INSERT INTO reading_records (id, user_id, book_id, content, status, created_at, updated_at) VALUES
(1, 1, 1, 'This book shows the horror of totalitarian society. Big Brother surveillance system was very impressive.', 'COMPLETED', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(2, 1, 2, 'Learned about the importance of clean code. Functions should be small and have meaningful names.', 'COMPLETED', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(3, 2, 3, 'Harry Potter magical world is very interesting. The Hogwarts school setting is well built.', 'IN_PROGRESS', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- AI Analyses
INSERT INTO ai_analyses (analysis_id, user_id, book_id, analysis_type, content, created_at) VALUES
('analysis_001', 1, 1, 'LITERATURE_ANALYSIS', '1984 is a dystopian novel that vividly depicts the oppressive nature of totalitarian society. The absolute power symbolized by Big Brother and constant surveillance completely control individual freedom and thought. In particular, through the concepts of "thought police" and "doublethink", it shows how power invades even the individual inner self. This contains a warning message about surveillance systems and media manipulation in modern society, providing still valid criticism.', CURRENT_TIMESTAMP),
('analysis_002', 1, 2, 'TECHNICAL_SUMMARY', 'Clean Code is a must-read book that emphasizes the importance of code quality in software development. Key concepts include: 1) Functions should be small and do only one thing. 2) Variable and function names should clearly reveal intent. 3) Code itself should be explanatory rather than comments. 4) Emphasizes the importance of writing test code. 5) Continuous improvement through refactoring is necessary. Through these principles, you can write code that is easy to maintain and extensible.', CURRENT_TIMESTAMP);