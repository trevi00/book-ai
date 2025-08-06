import { NavigatorScreenParams } from '@react-navigation/native';

export type RootStackParamList = {
  Auth: NavigatorScreenParams<AuthStackParamList>;
  Main: NavigatorScreenParams<MainTabParamList>;
};

export type AuthStackParamList = {
  Login: undefined;
  Register: undefined;
};

export type MainTabParamList = {
  BookStack: NavigatorScreenParams<BookStackParamList>;
  ReadingStack: NavigatorScreenParams<ReadingStackParamList>;
  AnalysisStack: NavigatorScreenParams<AnalysisStackParamList>;
  Profile: undefined;
};

export type BookStackParamList = {
  BookList: undefined;
  BookDetail: { bookId: number };
  AddBook: undefined;
};

export type ReadingStackParamList = {
  ReadingList: undefined;
  ReadingDetail: { readingId: number };
  ReadingEditor: { readingId?: number; bookId?: number };
};

export type AnalysisStackParamList = {
  AnalysisList: undefined;
  AnalysisDetail: { analysisId: string };
};