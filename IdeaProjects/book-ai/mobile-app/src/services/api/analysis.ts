import axios from 'axios';
import { ApiResponse } from '@/types/api';
import { AIAnalysis, AnalysisType } from '@/types/analysis';
import { Genre } from '@/types/book';
import { apiClient } from './client';

interface AnalysisRequest {
  user_id: string;
  book_id: string;
  book_title: string;
  book_author: string;
  genre: Genre;
  reading_content: string;
}

class AnalysisApiClient {
  private baseURL = 'http://localhost:8001/api/v1'; // FastAPI AI Service

  async getAllAnalyses(): Promise<ApiResponse<AIAnalysis[]>> {
    return apiClient.get<AIAnalysis[]>('/analyses');
  }

  async getAnalysisById(analysisId: string): Promise<ApiResponse<AIAnalysis>> {
    return apiClient.get<AIAnalysis>(`/analyses/${analysisId}`);
  }

  async generateAnalysis(readingId: string, analysisType: AnalysisType): Promise<ApiResponse<AIAnalysis>> {
    try {
      const response = await axios.post<ApiResponse<AIAnalysis>>(
        `${this.baseURL}/analysis/generate`,
        {
          reading_id: readingId,
          analysis_type: analysisType,
        },
        {
          headers: {
            'Content-Type': 'application/json',
          },
          timeout: 60000, // AI 분석은 시간이 오래 걸릴 수 있음
        }
      );
      return response.data;
    } catch (error) {
      throw error;
    }
  }

  async deleteAnalysis(analysisId: string): Promise<ApiResponse<void>> {
    return apiClient.delete<void>(`/analyses/${analysisId}`);
  }

  async getAnalysisByReading(readingId: string): Promise<ApiResponse<AIAnalysis[]>> {
    return apiClient.get<AIAnalysis[]>(`/analyses/reading/${readingId}`);
  }

  async healthCheck(): Promise<ApiResponse<any>> {
    try {
      const response = await axios.get<ApiResponse<any>>(
        `${this.baseURL}/health/`
      );
      return response.data;
    } catch (error) {
      throw error;
    }
  }
}

export const analysisApi = new AnalysisApiClient();