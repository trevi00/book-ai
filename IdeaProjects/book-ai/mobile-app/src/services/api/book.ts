import { apiClient } from './client';
import { ApiResponse } from '@/types/api';
import { Book, BookCreateRequest, Genre } from '@/types/book';

export const bookApi = {
  getAllBooks: async (): Promise<ApiResponse<Book[]>> => {
    return apiClient.get<Book[]>('/books');
  },

  getBookById: async (id: number): Promise<ApiResponse<Book>> => {
    return apiClient.get<Book>(`/books/${id}`);
  },

  getBooksByGenre: async (genre: Genre): Promise<ApiResponse<Book[]>> => {
    return apiClient.get<Book[]>(`/books/genre/${genre}`);
  },

  searchBooks: async (title: string): Promise<ApiResponse<Book[]>> => {
    return apiClient.get<Book[]>(`/books/search?title=${encodeURIComponent(title)}`);
  },

  createBook: async (bookData: BookCreateRequest): Promise<ApiResponse<Book>> => {
    return apiClient.post<Book>('/books', bookData);
  },
};