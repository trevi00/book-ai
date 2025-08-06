import { createSlice, createAsyncThunk, PayloadAction } from '@reduxjs/toolkit';
import { BookState, Book, BookCreateRequest, Genre } from '@/types/book';
import { bookApi } from '@/services/api/book';

const initialState: BookState = {
  books: [],
  selectedBook: null,
  isLoading: false,
  error: null,
};

// 비동기 액션들
export const fetchBooks = createAsyncThunk(
  'book/fetchBooks',
  async (_, { rejectWithValue }) => {
    try {
      const response = await bookApi.getAllBooks();
      if (response.success && response.data) {
        return response.data;
      }
      throw new Error(response.message || '책 목록 조회 실패');
    } catch (error: any) {
      return rejectWithValue(error.message || '책 목록 조회 중 오류가 발생했습니다');
    }
  }
);

export const fetchBookById = createAsyncThunk(
  'book/fetchBookById',
  async (id: number, { rejectWithValue }) => {
    try {
      const response = await bookApi.getBookById(id);
      if (response.success && response.data) {
        return response.data;
      }
      throw new Error(response.message || '책 조회 실패');
    } catch (error: any) {
      return rejectWithValue(error.message || '책 조회 중 오류가 발생했습니다');
    }
  }
);

export const fetchBooksByGenre = createAsyncThunk(
  'book/fetchBooksByGenre',
  async (genre: Genre, { rejectWithValue }) => {
    try {
      const response = await bookApi.getBooksByGenre(genre);
      if (response.success && response.data) {
        return response.data;
      }
      throw new Error(response.message || '장르별 책 조회 실패');
    } catch (error: any) {
      return rejectWithValue(error.message || '장르별 책 조회 중 오류가 발생했습니다');
    }
  }
);

export const searchBooks = createAsyncThunk(
  'book/searchBooks',
  async (title: string, { rejectWithValue }) => {
    try {
      const response = await bookApi.searchBooks(title);
      if (response.success && response.data) {
        return response.data;
      }
      throw new Error(response.message || '책 검색 실패');
    } catch (error: any) {
      return rejectWithValue(error.message || '책 검색 중 오류가 발생했습니다');
    }
  }
);

export const createBook = createAsyncThunk(
  'book/createBook',
  async (bookData: BookCreateRequest, { rejectWithValue }) => {
    try {
      const response = await bookApi.createBook(bookData);
      if (response.success && response.data) {
        return response.data;
      }
      throw new Error(response.message || '책 등록 실패');
    } catch (error: any) {
      return rejectWithValue(error.message || '책 등록 중 오류가 발생했습니다');
    }
  }
);

const bookSlice = createSlice({
  name: 'book',
  initialState,
  reducers: {
    clearError: (state) => {
      state.error = null;
    },
    setSelectedBook: (state, action: PayloadAction<Book | null>) => {
      state.selectedBook = action.payload;
    },
    clearBooks: (state) => {
      state.books = [];
      state.selectedBook = null;
      state.error = null;
    },
  },
  extraReducers: (builder) => {
    builder
      // 책 목록 조회
      .addCase(fetchBooks.pending, (state) => {
        state.isLoading = true;
        state.error = null;
      })
      .addCase(fetchBooks.fulfilled, (state, action) => {
        state.isLoading = false;
        state.books = action.payload;
        state.error = null;
      })
      .addCase(fetchBooks.rejected, (state, action) => {
        state.isLoading = false;
        state.error = action.payload as string;
      })
      // 책 상세 조회
      .addCase(fetchBookById.pending, (state) => {
        state.isLoading = true;
        state.error = null;
      })
      .addCase(fetchBookById.fulfilled, (state, action) => {
        state.isLoading = false;
        state.selectedBook = action.payload;
        state.error = null;
      })
      .addCase(fetchBookById.rejected, (state, action) => {
        state.isLoading = false;
        state.error = action.payload as string;
      })
      // 장르별 책 조회
      .addCase(fetchBooksByGenre.fulfilled, (state, action) => {
        state.isLoading = false;
        state.books = action.payload;
        state.error = null;
      })
      // 책 검색
      .addCase(searchBooks.fulfilled, (state, action) => {
        state.isLoading = false;
        state.books = action.payload;
        state.error = null;
      })
      // 책 생성
      .addCase(createBook.pending, (state) => {
        state.isLoading = true;
        state.error = null;
      })
      .addCase(createBook.fulfilled, (state, action) => {
        state.isLoading = false;
        state.books.push(action.payload);
        state.error = null;
      })
      .addCase(createBook.rejected, (state, action) => {
        state.isLoading = false;
        state.error = action.payload as string;
      });
  },
});

export const { clearError, setSelectedBook, clearBooks } = bookSlice.actions;
export default bookSlice.reducer;