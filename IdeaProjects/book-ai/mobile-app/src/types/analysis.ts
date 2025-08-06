export enum AnalysisType {
  LITERATURE_ANALYSIS = 'LITERATURE_ANALYSIS',
  TECHNICAL_SUMMARY = 'TECHNICAL_SUMMARY',
}

export interface AIAnalysis {
  analysisId: string;
  userId: string;
  bookId: string;
  analysisType: AnalysisType;
  content: string;
  createdAt: string;
}

export interface AnalysisState {
  analyses: AIAnalysis[];
  currentAnalysis: AIAnalysis | null;
  isGenerating: boolean;
  error: string | null;
}