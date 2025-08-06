import { apiClient } from './client';
import { ApiResponse } from '@/types/api';
import { User, LoginRequest, RegisterRequest } from '@/types/auth';

export const authApi = {
  login: async (credentials: LoginRequest): Promise<ApiResponse<User>> => {
    return apiClient.post<User>('/auth/login', credentials);
  },

  register: async (userData: RegisterRequest): Promise<ApiResponse<User>> => {
    return apiClient.post<User>('/auth/register', userData);
  },

  logout: async (): Promise<ApiResponse<void>> => {
    return apiClient.post<void>('/auth/logout');
  },

  refreshToken: async (): Promise<ApiResponse<{ token: string }>> => {
    return apiClient.post<{ token: string }>('/auth/refresh');
  },
};