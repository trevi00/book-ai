import { createSlice, createAsyncThunk, PayloadAction } from '@reduxjs/toolkit';
import { AnalysisState, AIAnalysis, AnalysisType } from '@/types/analysis';
import { analysisApi } from '@/services/api/analysis';

const initialState: AnalysisState = {
  analyses: [],
  currentAnalysis: null,
  isGenerating: false,
  error: null,
};

// 비동기 액션들
export const fetchAnalyses = createAsyncThunk(
  'analysis/fetchAnalyses',
  async (_, { rejectWithValue }) => {
    try {
      const response = await analysisApi.getAllAnalyses();
      if (response.success && response.data) {
        return response.data;
      }
      throw new Error(response.message || '분석 결과 조회 실패');
    } catch (error: any) {
      return rejectWithValue(error.message || '분석 결과 조회 중 오류가 발생했습니다');
    }
  }
);

export const fetchAnalysisById = createAsyncThunk(
  'analysis/fetchAnalysisById',
  async (analysisId: string, { rejectWithValue }) => {
    try {
      const response = await analysisApi.getAnalysisById(analysisId);
      if (response.success && response.data) {
        return response.data;
      }
      throw new Error(response.message || '분석 결과 조회 실패');
    } catch (error: any) {
      return rejectWithValue(error.message || '분석 결과 조회 중 오류가 발생했습니다');
    }
  }
);

export const generateAnalysis = createAsyncThunk(
  'analysis/generateAnalysis',
  async ({ readingId, analysisType }: { readingId: string; analysisType: AnalysisType }, { rejectWithValue }) => {
    try {
      const response = await analysisApi.generateAnalysis(readingId, analysisType);
      if (response.success && response.data) {
        return response.data;
      }
      throw new Error(response.message || 'AI 분석 생성 실패');
    } catch (error: any) {
      return rejectWithValue(error.message || 'AI 분석 생성 중 오류가 발생했습니다');
    }
  }
);

export const deleteAnalysis = createAsyncThunk(
  'analysis/deleteAnalysis',
  async (analysisId: string, { rejectWithValue }) => {
    try {
      const response = await analysisApi.deleteAnalysis(analysisId);
      if (response.success) {
        return analysisId;
      }
      throw new Error(response.message || '분석 결과 삭제 실패');
    } catch (error: any) {
      return rejectWithValue(error.message || '분석 결과 삭제 중 오류가 발생했습니다');
    }
  }
);

const analysisSlice = createSlice({
  name: 'analysis',
  initialState,
  reducers: {
    clearError: (state) => {
      state.error = null;
    },
    setCurrentAnalysis: (state, action: PayloadAction<AIAnalysis | null>) => {
      state.currentAnalysis = action.payload;
    },
    clearAnalyses: (state) => {
      state.analyses = [];
      state.currentAnalysis = null;
      state.error = null;
    },
  },
  extraReducers: (builder) => {
    builder
      // 분석 결과 목록 조회
      .addCase(fetchAnalyses.pending, (state) => {
        state.error = null;
      })
      .addCase(fetchAnalyses.fulfilled, (state, action) => {
        state.analyses = action.payload;
        state.error = null;
      })
      .addCase(fetchAnalyses.rejected, (state, action) => {
        state.error = action.payload as string;
      })
      // 분석 결과 상세 조회
      .addCase(fetchAnalysisById.pending, (state) => {
        state.error = null;
      })
      .addCase(fetchAnalysisById.fulfilled, (state, action) => {
        state.currentAnalysis = action.payload;
        state.error = null;
      })
      .addCase(fetchAnalysisById.rejected, (state, action) => {
        state.error = action.payload as string;
      })
      // AI 분석 생성
      .addCase(generateAnalysis.pending, (state) => {
        state.isGenerating = true;
        state.error = null;
      })
      .addCase(generateAnalysis.fulfilled, (state, action) => {
        state.isGenerating = false;
        state.analyses.unshift(action.payload); // 최신 분석 결과를 맨 앞에 추가
        state.currentAnalysis = action.payload;
        state.error = null;
      })
      .addCase(generateAnalysis.rejected, (state, action) => {
        state.isGenerating = false;
        state.error = action.payload as string;
      })
      // 분석 결과 삭제
      .addCase(deleteAnalysis.fulfilled, (state, action) => {
        state.analyses = state.analyses.filter(a => a.analysisId !== action.payload);
        if (state.currentAnalysis?.analysisId === action.payload) {
          state.currentAnalysis = null;
        }
        state.error = null;
      })
      .addCase(deleteAnalysis.rejected, (state, action) => {
        state.error = action.payload as string;
      });
  },
});

export const {
  clearError,
  setCurrentAnalysis,
  clearAnalyses,
} = analysisSlice.actions;

export default analysisSlice.reducer;