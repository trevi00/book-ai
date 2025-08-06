import React from 'react';
import { createBottomTabNavigator } from '@react-navigation/bottom-tabs';
import { MainTabParamList } from '@/types/navigation';
import BookNavigator from './BookNavigator';
import ReadingNavigator from './ReadingNavigator';
import AnalysisNavigator from './AnalysisNavigator';
import ProfileScreen from '@/screens/profile/ProfileScreen';
import Icon from 'react-native-vector-icons/MaterialIcons';

const Tab = createBottomTabNavigator<MainTabParamList>();

const MainNavigator: React.FC = () => {
  return (
    <Tab.Navigator
      screenOptions={({ route }) => ({
        tabBarIcon: ({ focused, color, size }) => {
          let iconName = '';

          switch (route.name) {
            case 'BookStack':
              iconName = 'library-books';
              break;
            case 'ReadingStack':
              iconName = 'book';
              break;
            case 'AnalysisStack':
              iconName = 'analytics';
              break;
            case 'Profile':
              iconName = 'person';
              break;
          }

          return <Icon name={iconName} size={size} color={color} />;
        },
        tabBarActiveTintColor: '#007AFF',
        tabBarInactiveTintColor: 'gray',
        headerShown: false,
      })}
    >
      <Tab.Screen 
        name="BookStack" 
        component={BookNavigator}
        options={{ tabBarLabel: '도서' }}
      />
      <Tab.Screen 
        name="ReadingStack" 
        component={ReadingNavigator}
        options={{ tabBarLabel: '독서' }}
      />
      <Tab.Screen 
        name="AnalysisStack" 
        component={AnalysisNavigator}
        options={{ tabBarLabel: '분석' }}
      />
      <Tab.Screen 
        name="Profile" 
        component={ProfileScreen}
        options={{ tabBarLabel: '프로필' }}
      />
    </Tab.Navigator>
  );
};

export default MainNavigator;