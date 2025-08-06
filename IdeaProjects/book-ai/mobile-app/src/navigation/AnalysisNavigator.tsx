import React from 'react';
import { createNativeStackNavigator } from '@react-navigation/native-stack';
import { AnalysisStackParamList } from '@/types/navigation';
import AnalysisListScreen from '@/screens/analysis/AnalysisListScreen';
import AnalysisDetailScreen from '@/screens/analysis/AnalysisDetailScreen';

const Stack = createNativeStackNavigator<AnalysisStackParamList>();

const AnalysisNavigator: React.FC = () => {
  return (
    <Stack.Navigator
      initialRouteName="AnalysisList"
      screenOptions={{
        headerStyle: {
          backgroundColor: '#007AFF',
        },
        headerTintColor: '#fff',
        headerTitleStyle: {
          fontWeight: 'bold',
        },
      }}
    >
      <Stack.Screen 
        name="AnalysisList" 
        component={AnalysisListScreen}
        options={{ title: 'AI 분석 결과' }}
      />
      <Stack.Screen 
        name="AnalysisDetail" 
        component={AnalysisDetailScreen}
        options={{ title: '분석 상세' }}
      />
    </Stack.Navigator>
  );
};

export default AnalysisNavigator;