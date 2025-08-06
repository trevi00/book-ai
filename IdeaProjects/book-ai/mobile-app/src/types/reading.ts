import { User } from './auth';
import { Book } from './book';

export enum ReadingStatus {
  IN_PROGRESS = 'IN_PROGRESS',
  COMPLETED = 'COMPLETED',
}

export interface ReadingRecord {
  id: number;
  user: User;
  book: Book;
  content: string;
  status: ReadingStatus;
  createdAt: string;
  updatedAt: string;
}

export interface ReadingRecordCreateRequest {
  userId: number;
  bookId: number;
  content?: string;
}

export interface ReadingRecordUpdateRequest {
  content: string;
}

export interface ReadingState {
  readings: ReadingRecord[];
  currentReading: ReadingRecord | null;
  drafts: Record<string, string>;
  isLoading: boolean;
  error: string | null;
}