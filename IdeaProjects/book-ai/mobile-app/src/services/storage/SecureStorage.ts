import * as Keychain from 'react-native-keychain';

const TOKEN_KEY = 'jwt_token';
const USER_KEY = 'user_data';

export const saveSecureToken = async (token: string): Promise<void> => {
  try {
    await Keychain.setInternetCredentials(TOKEN_KEY, TOKEN_KEY, token);
  } catch (error) {
    console.error('토큰 저장 실패:', error);
    throw error;
  }
};

export const getSecureToken = async (): Promise<string | null> => {
  try {
    const credentials = await Keychain.getInternetCredentials(TOKEN_KEY);
    if (credentials) {
      return credentials.password;
    }
    return null;
  } catch (error) {
    console.error('토큰 조회 실패:', error);
    return null;
  }
};

export const removeSecureToken = async (): Promise<void> => {
  try {
    await Keychain.resetInternetCredentials(TOKEN_KEY);
  } catch (error) {
    console.error('토큰 삭제 실패:', error);
    throw error;
  }
};

export const saveSecureUserData = async (userData: string): Promise<void> => {
  try {
    await Keychain.setInternetCredentials(USER_KEY, USER_KEY, userData);
  } catch (error) {
    console.error('사용자 데이터 저장 실패:', error);
    throw error;
  }
};

export const getSecureUserData = async (): Promise<string | null> => {
  try {
    const credentials = await Keychain.getInternetCredentials(USER_KEY);
    if (credentials) {
      return credentials.password;
    }
    return null;
  } catch (error) {
    console.error('사용자 데이터 조회 실패:', error);
    return null;
  }
};

export const removeSecureUserData = async (): Promise<void> => {
  try {
    await Keychain.resetInternetCredentials(USER_KEY);
  } catch (error) {
    console.error('사용자 데이터 삭제 실패:', error);
    throw error;
  }
};