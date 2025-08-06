export interface ApiResponse<T = any> {
  success: boolean;
  data?: T;
  message?: string;
  errorCode?: string;
}

export interface ApiError {
  message: string;
  errorCode?: string;
  status?: number;
}