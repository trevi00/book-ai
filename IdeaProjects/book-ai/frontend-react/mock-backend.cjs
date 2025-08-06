// Mock backend server for integration testing
const express = require('express');
const cors = require('cors');
const app = express();
const port = 8080;

// In-memory storage
let users = [];
let books = [];
let idCounter = 1;

app.use(cors());
app.use(express.json());

// Helper functions
const generateToken = (userId, email) => {
  return `jwt-token-${userId}-${Date.now()}`;
};

const findUserByEmail = (email) => {
  return users.find(user => user.email === email);
};

// Health check
app.get('/api/health', (req, res) => {
  res.json({ status: 'healthy', service: 'Book AI Backend' });
});

// Auth endpoints
app.post('/api/auth/register', (req, res) => {
  try {
    const { email, password, nickname } = req.body;
    
    // Check if user already exists
    if (findUserByEmail(email)) {
      return res.status(400).json({
        path: '/auth/register',
        success: false,
        message: '이미 존재하는 이메일입니다',
        error: 'Bad Request'
      });
    }
    
    // Create new user
    const newUser = {
      id: idCounter++,
      email,
      nickname,
      password, // In real app, this would be hashed
      createdAt: new Date().toISOString()
    };
    users.push(newUser);
    
    // Generate token
    const token = generateToken(newUser.id, newUser.email);
    
    // Response
    const userResponse = {
      id: newUser.id,
      email: newUser.email,
      nickname: newUser.nickname
    };
    
    res.status(201).json({
      path: '/auth/register',
      success: true,
      message: '회원가입이 완료되었습니다',
      data: {
        user: userResponse,
        token,
        tokenType: 'Bearer'
      }
    });
  } catch (error) {
    res.status(500).json({
      path: '/auth/register',
      success: false,
      message: '서버 오류가 발생했습니다',
      error: 'Internal Server Error'
    });
  }
});

app.post('/api/auth/login', (req, res) => {
  try {
    const { email, password } = req.body;
    
    // Find user
    const user = findUserByEmail(email);
    if (!user || user.password !== password) {
      return res.status(401).json({
        path: '/auth/login',
        success: false,
        message: '이메일 또는 비밀번호가 잘못되었습니다',
        error: 'Unauthorized'
      });
    }
    
    // Generate token
    const token = generateToken(user.id, user.email);
    
    // Response
    const userResponse = {
      id: user.id,
      email: user.email,
      nickname: user.nickname
    };
    
    res.json({
      path: '/auth/login',
      success: true,
      message: '로그인이 완료되었습니다',
      data: {
        user: userResponse,
        token,
        tokenType: 'Bearer'
      }
    });
  } catch (error) {
    res.status(500).json({
      path: '/auth/login',
      success: false,
      message: '서버 오류가 발생했습니다',
      error: 'Internal Server Error'
    });
  }
});

// Books endpoints (requires authentication)
const authenticateToken = (req, res, next) => {
  const authHeader = req.headers['authorization'];
  const token = authHeader && authHeader.split(' ')[1];
  
  if (!token) {
    return res.status(401).json({
      path: req.path,
      success: false,
      message: '인증 토큰이 필요합니다',
      error: 'Unauthorized'
    });
  }
  
  // Simple token validation (in real app, this would be JWT verification)
  if (!token.startsWith('jwt-token-')) {
    return res.status(403).json({
      path: req.path,
      success: false,
      message: '유효하지 않은 토큰입니다',
      error: 'Forbidden'
    });
  }
  
  // Extract user ID from token
  const userId = parseInt(token.split('-')[2]);
  req.userId = userId;
  next();
};

app.get('/api/books', authenticateToken, (req, res) => {
  const userBooks = books.filter(book => book.userId === req.userId);
  res.json({
    path: '/books',
    success: true,
    message: '도서 목록을 성공적으로 조회했습니다',
    data: userBooks
  });
});

app.post('/api/books', authenticateToken, (req, res) => {
  try {
    const { title, author, content } = req.body;
    
    const newBook = {
      id: idCounter++,
      title,
      author,
      content,
      userId: req.userId,
      createdAt: new Date().toISOString(),
      updatedAt: new Date().toISOString()
    };
    
    books.push(newBook);
    
    res.status(201).json({
      path: '/books',
      success: true,
      message: '도서가 성공적으로 생성되었습니다',
      data: newBook
    });
  } catch (error) {
    res.status(500).json({
      path: '/books',
      success: false,
      message: '서버 오류가 발생했습니다',
      error: 'Internal Server Error'
    });
  }
});

// Start server
app.listen(port, () => {
  console.log(`Mock backend server running at http://localhost:${port}`);
  console.log('Available endpoints:');
  console.log('- GET  /api/health');
  console.log('- POST /api/auth/register');
  console.log('- POST /api/auth/login');
  console.log('- GET  /api/books (requires auth)');
  console.log('- POST /api/books (requires auth)');
});

module.exports = app;