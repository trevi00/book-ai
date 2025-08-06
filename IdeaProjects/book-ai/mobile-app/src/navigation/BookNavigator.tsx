import React from 'react';
import { createNativeStackNavigator } from '@react-navigation/native-stack';
import { BookStackParamList } from '@/types/navigation';
import BookListScreen from '@/screens/book/BookListScreen';
import BookDetailScreen from '@/screens/book/BookDetailScreen';
import AddBookScreen from '@/screens/book/AddBookScreen';

const Stack = createNativeStackNavigator<BookStackParamList>();

const BookNavigator: React.FC = () => {
  return (
    <Stack.Navigator
      initialRouteName="BookList"
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
        name="BookList" 
        component={BookListScreen}
        options={{ title: '도서 목록' }}
      />
      <Stack.Screen 
        name="BookDetail" 
        component={BookDetailScreen}
        options={{ title: '도서 상세' }}
      />
      <Stack.Screen 
        name="AddBook" 
        component={AddBookScreen}
        options={{ title: '도서 등록' }}
      />
    </Stack.Navigator>
  );
};

export default BookNavigator;