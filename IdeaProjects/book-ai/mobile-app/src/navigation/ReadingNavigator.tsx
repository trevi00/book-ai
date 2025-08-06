import React from 'react';
import { createNativeStackNavigator } from '@react-navigation/native-stack';
import { ReadingStackParamList } from '@/types/navigation';
import ReadingListScreen from '@/screens/reading/ReadingListScreen';
import ReadingDetailScreen from '@/screens/reading/ReadingDetailScreen';
import ReadingEditorScreen from '@/screens/reading/ReadingEditorScreen';

const Stack = createNativeStackNavigator<ReadingStackParamList>();

const ReadingNavigator: React.FC = () => {
  return (
    <Stack.Navigator
      initialRouteName="ReadingList"
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
        name="ReadingList" 
        component={ReadingListScreen}
        options={{ title: '독서 기록' }}
      />
      <Stack.Screen 
        name="ReadingDetail" 
        component={ReadingDetailScreen}
        options={{ title: '독서 상세' }}
      />
      <Stack.Screen 
        name="ReadingEditor" 
        component={ReadingEditorScreen}
        options={{ title: '독서 작성' }}
      />
    </Stack.Navigator>
  );
};

export default ReadingNavigator;