import AsyncStorage from '@react-native-async-storage/async-storage';

const DRAFTS_KEY = 'reading_drafts';
const SETTINGS_KEY = 'app_settings';

export const saveDraft = async (bookId: string, content: string): Promise<void> => {
  try {
    const existingDrafts = await getDrafts();
    const updatedDrafts = {
      ...existingDrafts,
      [bookId]: content,
    };
    await AsyncStorage.setItem(DRAFTS_KEY, JSON.stringify(updatedDrafts));
  } catch (error) {
    console.error('임시저장 실패:', error);
    throw error;
  }
};

export const getDraft = async (bookId: string): Promise<string | null> => {
  try {
    const drafts = await getDrafts();
    return drafts[bookId] || null;
  } catch (error) {
    console.error('임시저장 조회 실패:', error);
    return null;
  }
};

export const getDrafts = async (): Promise<Record<string, string>> => {
  try {
    const draftsJson = await AsyncStorage.getItem(DRAFTS_KEY);
    return draftsJson ? JSON.parse(draftsJson) : {};
  } catch (error) {
    console.error('임시저장 목록 조회 실패:', error);
    return {};
  }
};

export const removeDraft = async (bookId: string): Promise<void> => {
  try {
    const existingDrafts = await getDrafts();
    delete existingDrafts[bookId];
    await AsyncStorage.setItem(DRAFTS_KEY, JSON.stringify(existingDrafts));
  } catch (error) {
    console.error('임시저장 삭제 실패:', error);
    throw error;
  }
};

export const saveSettings = async (settings: Record<string, any>): Promise<void> => {
  try {
    await AsyncStorage.setItem(SETTINGS_KEY, JSON.stringify(settings));
  } catch (error) {
    console.error('설정 저장 실패:', error);
    throw error;
  }
};

export const getSettings = async (): Promise<Record<string, any>> => {
  try {
    const settingsJson = await AsyncStorage.getItem(SETTINGS_KEY);
    return settingsJson ? JSON.parse(settingsJson) : {};
  } catch (error) {
    console.error('설정 조회 실패:', error);
    return {};
  }
};