export enum Genre {
  TECHNICAL = 'TECHNICAL',
  LITERATURE = 'LITERATURE',
}

export interface Book {
  id: number;
  title: string;
  author: string;
  isbn?: string;
  genre: Genre;
  description?: string;
  createdAt: string;
  updatedAt: string;
}

export interface BookCreateRequest {
  title: string;
  author: string;
  isbn?: string;
  genre: Genre;
  description?: string;
}

export interface BookState {
  books: Book[];
  selectedBook: Book | null;
  isLoading: boolean;
  error: string | null;
}