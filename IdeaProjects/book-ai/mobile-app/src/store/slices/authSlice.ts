import { createSlice, createAsyncThunk, PayloadAction } from '@reduxjs/toolkit';
import { AuthState, User, LoginRequest, RegisterRequest } from '@/types/auth';
import { authApi } from '@/services/api/auth';
import { saveSecureToken, saveSecureUserData, removeSecureToken, removeSecureUserData } from '@/services/storage/SecureStorage';

const initialState: AuthState = {
  user: null,
  isAuthenticated: false,
  isLoading: false,
  error: null,
};

// 비동기 액션들
export const loginUser = createAsyncThunk(
  'auth/login',
  async (credentials: LoginRequest, { rejectWithValue }) => {
    try {
      const response = await authApi.login(credentials);
      if (response.success && response.data) {
        // JWT 토큰 저장 (실제 구현에서는 응답에 토큰이 포함되어야 함)
        await saveSecureToken('mock-jwt-token'); // 실제로는 response.token
        await saveSecureUserData(JSON.stringify(response.data));
        return response.data;
      }
      throw new Error(response.message || '로그인 실패');
    } catch (error: any) {
      return rejectWithValue(error.message || '로그인 중 오류가 발생했습니다');
    }
  }
);

export const registerUser = createAsyncThunk(
  'auth/register',
  async (userData: RegisterRequest, { rejectWithValue }) => {
    try {
      const response = await authApi.register(userData);
      if (response.success && response.data) {
        await saveSecureToken('mock-jwt-token'); // 실제로는 response.token
        await saveSecureUserData(JSON.stringify(response.data));
        return response.data;
      }
      throw new Error(response.message || '회원가입 실패');
    } catch (error: any) {
      return rejectWithValue(error.message || '회원가입 중 오류가 발생했습니다');
    }
  }
);

export const logoutUser = createAsyncThunk(
  'auth/logout',
  async (_, { rejectWithValue }) => {
    try {
      await authApi.logout();
      await removeSecureToken();
      await removeSecureUserData();
    } catch (error: any) {
      // 로그아웃은 실패해도 로컬 데이터는 제거
      await removeSecureToken();
      await removeSecureUserData();
      return rejectWithValue(error.message || '로그아웃 중 오류가 발생했습니다');
    }
  }
);

const authSlice = createSlice({
  name: 'auth',
  initialState,
  reducers: {
    clearError: (state) => {
      state.error = null;
    },
    setUser: (state, action: PayloadAction<User>) => {
      state.user = action.payload;
      state.isAuthenticated = true;
    },
    clearAuth: (state) => {
      state.user = null;
      state.isAuthenticated = false;
      state.error = null;
    },
  },
  extraReducers: (builder) => {
    builder
      // 로그인
      .addCase(loginUser.pending, (state) => {
        state.isLoading = true;
        state.error = null;
      })
      .addCase(loginUser.fulfilled, (state, action) => {
        state.isLoading = false;
        state.user = action.payload;
        state.isAuthenticated = true;
        state.error = null;
      })
      .addCase(loginUser.rejected, (state, action) => {
        state.isLoading = false;
        state.error = action.payload as string;
      })
      // 회원가입
      .addCase(registerUser.pending, (state) => {
        state.isLoading = true;
        state.error = null;
      })
      .addCase(registerUser.fulfilled, (state, action) => {
        state.isLoading = false;
        state.user = action.payload;
        state.isAuthenticated = true;
        state.error = null;
      })
      .addCase(registerUser.rejected, (state, action) => {
        state.isLoading = false;
        state.error = action.payload as string;
      })
      // 로그아웃
      .addCase(logoutUser.fulfilled, (state) => {
        state.user = null;
        state.isAuthenticated = false;
        state.error = null;
      });
  },
});

export const { clearError, setUser, clearAuth } = authSlice.actions;
export default authSlice.reducer;