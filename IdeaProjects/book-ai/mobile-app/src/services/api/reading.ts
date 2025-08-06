import { apiClient } from './client';
import { ApiResponse } from '@/types/api';
import { ReadingRecord, ReadingRecordCreateRequest, ReadingRecordUpdateRequest, ReadingStatus } from '@/types/reading';

export const readingApi = {
  getAllReadings: async (): Promise<ApiResponse<ReadingRecord[]>> => {
    return apiClient.get<ReadingRecord[]>('/readings');
  },

  getReadingById: async (id: number): Promise<ApiResponse<ReadingRecord>> => {
    return apiClient.get<ReadingRecord>(`/readings/${id}`);
  },

  createReading: async (data: ReadingRecordCreateRequest): Promise<ApiResponse<ReadingRecord>> => {
    return apiClient.post<ReadingRecord>('/readings', data);
  },

  updateReading: async (id: number, data: ReadingRecordUpdateRequest): Promise<ApiResponse<ReadingRecord>> => {
    return apiClient.put<ReadingRecord>(`/readings/${id}`, data);
  },

  completeReading: async (id: number): Promise<ApiResponse<ReadingRecord>> => {
    return apiClient.put<ReadingRecord>(`/readings/${id}/complete`);
  },

  deleteReading: async (id: number): Promise<ApiResponse<void>> => {
    return apiClient.delete<void>(`/readings/${id}`);
  },

  getReadingsByBook: async (bookId: number): Promise<ApiResponse<ReadingRecord[]>> => {
    return apiClient.get<ReadingRecord[]>(`/readings/book/${bookId}`);
  },

  getReadingsByUser: async (userId: number): Promise<ApiResponse<ReadingRecord[]>> => {
    return apiClient.get<ReadingRecord[]>(`/readings/user/${userId}`);
  },

  getReadingsByUserAndStatus: async (userId: number, status: ReadingStatus): Promise<ApiResponse<ReadingRecord[]>> => {
    return apiClient.get<ReadingRecord[]>(`/readings/user/${userId}/status/${status}`);
  },
};