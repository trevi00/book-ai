import React, { useEffect } from 'react';
import { StatusBar } from 'react-native';
import { Provider } from 'react-redux';
import { store } from '@/store';
import { useAppDispatch } from '@/store';
import { checkAuthStatus } from '@/store/slices/authSlice';
import AppNavigator from '@/navigation/AppNavigator';

const AppContent: React.FC = () => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    // 앱 시작 시 인증 상태 확인
    dispatch(checkAuthStatus());
  }, [dispatch]);

  return (
    <>
      <StatusBar barStyle="light-content" backgroundColor="#007AFF" />
      <AppNavigator />
    </>
  );
};

const App: React.FC = () => {
  return (
    <Provider store={store}>
      <AppContent />
    </Provider>
  );
};

export default App;