import { createSlice, createAsyncThunk, PayloadAction } from '@reduxjs/toolkit';
import { ReadingState, ReadingRecord, ReadingRecordCreateRequest, ReadingRecordUpdateRequest } from '@/types/reading';
import { readingApi } from '@/services/api/reading';

const initialState: ReadingState = {
  readings: [],
  currentReading: null,
  drafts: {},
  isLoading: false,
  error: null,
};

// 비동기 액션들
export const fetchReadings = createAsyncThunk(
  'reading/fetchReadings',
  async (_, { rejectWithValue }) => {
    try {
      const response = await readingApi.getAllReadings();
      if (response.success && response.data) {
        return response.data;
      }
      throw new Error(response.message || '독서 기록 조회 실패');
    } catch (error: any) {
      return rejectWithValue(error.message || '독서 기록 조회 중 오류가 발생했습니다');
    }
  }
);

export const fetchReadingById = createAsyncThunk(
  'reading/fetchReadingById',
  async (id: number, { rejectWithValue }) => {
    try {
      const response = await readingApi.getReadingById(id);
      if (response.success && response.data) {
        return response.data;
      }
      throw new Error(response.message || '독서 기록 조회 실패');
    } catch (error: any) {
      return rejectWithValue(error.message || '독서 기록 조회 중 오류가 발생했습니다');
    }
  }
);

export const createReading = createAsyncThunk(
  'reading/createReading',
  async (readingData: ReadingRecordCreateRequest, { rejectWithValue }) => {
    try {
      const response = await readingApi.createReading(readingData);
      if (response.success && response.data) {
        return response.data;
      }
      throw new Error(response.message || '독서 기록 생성 실패');
    } catch (error: any) {
      return rejectWithValue(error.message || '독서 기록 생성 중 오류가 발생했습니다');
    }
  }
);

export const updateReading = createAsyncThunk(
  'reading/updateReading',
  async ({ id, data }: { id: number; data: ReadingRecordUpdateRequest }, { rejectWithValue }) => {
    try {
      const response = await readingApi.updateReading(id, data);
      if (response.success && response.data) {
        return response.data;
      }
      throw new Error(response.message || '독서 기록 업데이트 실패');
    } catch (error: any) {
      return rejectWithValue(error.message || '독서 기록 업데이트 중 오류가 발생했습니다');
    }
  }
);

export const completeReading = createAsyncThunk(
  'reading/completeReading',
  async (id: number, { rejectWithValue }) => {
    try {
      const response = await readingApi.completeReading(id);
      if (response.success && response.data) {
        return response.data;
      }
      throw new Error(response.message || '독서 완료 처리 실패');
    } catch (error: any) {
      return rejectWithValue(error.message || '독서 완료 처리 중 오류가 발생했습니다');
    }
  }
);

export const deleteReading = createAsyncThunk(
  'reading/deleteReading',
  async (id: number, { rejectWithValue }) => {
    try {
      const response = await readingApi.deleteReading(id);
      if (response.success) {
        return id;
      }
      throw new Error(response.message || '독서 기록 삭제 실패');
    } catch (error: any) {
      return rejectWithValue(error.message || '독서 기록 삭제 중 오류가 발생했습니다');
    }
  }
);

const readingSlice = createSlice({
  name: 'reading',
  initialState,
  reducers: {
    clearError: (state) => {
      state.error = null;
    },
    setCurrentReading: (state, action: PayloadAction<ReadingRecord | null>) => {
      state.currentReading = action.payload;
    },
    saveDraft: (state, action: PayloadAction<{ key: string; content: string }>) => {
      state.drafts[action.payload.key] = action.payload.content;
    },
    removeDraft: (state, action: PayloadAction<string>) => {
      delete state.drafts[action.payload];
    },
    clearDrafts: (state) => {
      state.drafts = {};
    },
    clearReadings: (state) => {
      state.readings = [];
      state.currentReading = null;
      state.error = null;
    },
  },
  extraReducers: (builder) => {
    builder
      // 독서 기록 목록 조회
      .addCase(fetchReadings.pending, (state) => {
        state.isLoading = true;
        state.error = null;
      })
      .addCase(fetchReadings.fulfilled, (state, action) => {
        state.isLoading = false;
        state.readings = action.payload;
        state.error = null;
      })
      .addCase(fetchReadings.rejected, (state, action) => {
        state.isLoading = false;
        state.error = action.payload as string;
      })
      // 독서 기록 상세 조회
      .addCase(fetchReadingById.pending, (state) => {
        state.isLoading = true;
        state.error = null;
      })
      .addCase(fetchReadingById.fulfilled, (state, action) => {
        state.isLoading = false;
        state.currentReading = action.payload;
        state.error = null;
      })
      .addCase(fetchReadingById.rejected, (state, action) => {
        state.isLoading = false;
        state.error = action.payload as string;
      })
      // 독서 기록 생성
      .addCase(createReading.pending, (state) => {
        state.isLoading = true;
        state.error = null;
      })
      .addCase(createReading.fulfilled, (state, action) => {
        state.isLoading = false;
        state.readings.push(action.payload);
        state.currentReading = action.payload;
        state.error = null;
      })
      .addCase(createReading.rejected, (state, action) => {
        state.isLoading = false;
        state.error = action.payload as string;
      })
      // 독서 기록 업데이트
      .addCase(updateReading.pending, (state) => {
        state.isLoading = true;
        state.error = null;
      })
      .addCase(updateReading.fulfilled, (state, action) => {
        state.isLoading = false;
        const index = state.readings.findIndex(r => r.id === action.payload.id);
        if (index !== -1) {
          state.readings[index] = action.payload;
        }
        if (state.currentReading?.id === action.payload.id) {
          state.currentReading = action.payload;
        }
        state.error = null;
      })
      .addCase(updateReading.rejected, (state, action) => {
        state.isLoading = false;
        state.error = action.payload as string;
      })
      // 독서 완료
      .addCase(completeReading.pending, (state) => {
        state.isLoading = true;
        state.error = null;
      })
      .addCase(completeReading.fulfilled, (state, action) => {
        state.isLoading = false;
        const index = state.readings.findIndex(r => r.id === action.payload.id);
        if (index !== -1) {
          state.readings[index] = action.payload;
        }
        if (state.currentReading?.id === action.payload.id) {
          state.currentReading = action.payload;
        }
        state.error = null;
      })
      .addCase(completeReading.rejected, (state, action) => {
        state.isLoading = false;
        state.error = action.payload as string;
      })
      // 독서 기록 삭제
      .addCase(deleteReading.pending, (state) => {
        state.isLoading = true;
        state.error = null;
      })
      .addCase(deleteReading.fulfilled, (state, action) => {
        state.isLoading = false;
        state.readings = state.readings.filter(r => r.id !== action.payload);
        if (state.currentReading?.id === action.payload) {
          state.currentReading = null;
        }
        state.error = null;
      })
      .addCase(deleteReading.rejected, (state, action) => {
        state.isLoading = false;
        state.error = action.payload as string;
      });
  },
});

export const {
  clearError,
  setCurrentReading,
  saveDraft,
  removeDraft,
  clearDrafts,
  clearReadings,
} = readingSlice.actions;

export default readingSlice.reducer;