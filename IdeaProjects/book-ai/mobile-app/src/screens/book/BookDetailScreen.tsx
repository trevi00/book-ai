import React, { useEffect } from 'react';
import {
  View,
  Text,
  ScrollView,
  TouchableOpacity,
  StyleSheet,
  Alert,
} from 'react-native';
import { NativeStackScreenProps } from '@react-navigation/native-stack';
import { BookStackParamList } from '@/types/navigation';
import { useAppDispatch, useAppSelector } from '@/store';
import { fetchBookById, clearError } from '@/store/slices/bookSlice';
import { Genre } from '@/types/book';
import Icon from 'react-native-vector-icons/MaterialIcons';

type Props = NativeStackScreenProps<BookStackParamList, 'BookDetail'>;

const BookDetailScreen: React.FC<Props> = ({ route, navigation }) => {
  const { bookId } = route.params;
  const dispatch = useAppDispatch();
  const { selectedBook, isLoading, error } = useAppSelector((state) => state.book);

  useEffect(() => {
    loadBookDetail();
  }, [bookId]);

  useEffect(() => {
    if (error) {
      Alert.alert('오류', error);
      dispatch(clearError());
    }
  }, [error]);

  const loadBookDetail = async () => {
    try {
      await dispatch(fetchBookById(bookId)).unwrap();
    } catch (error) {
      // 에러는 useEffect에서 처리
    }
  };

  const handleStartReading = () => {
    if (selectedBook) {
      // ReadingEditor로 이동 (새로운 독서 기록 시작)
      navigation.navigate('ReadingEditor', { bookId: selectedBook.id });
    }
  };

  if (isLoading || !selectedBook) {
    return (
      <View style={styles.loadingContainer}>
        <Text>로딩 중...</Text>
      </View>
    );
  }

  return (
    <ScrollView style={styles.container}>
      <View style={styles.header}>
        <View style={styles.bookInfo}>
          <Text style={styles.title}>{selectedBook.title}</Text>
          <Text style={styles.author}>{selectedBook.author}</Text>
          <View style={styles.metaInfo}>
            <View style={[styles.genreTag, selectedBook.genre === Genre.TECHNICAL && styles.technicalTag]}>
              <Text style={[styles.genreText, selectedBook.genre === Genre.TECHNICAL && styles.technicalText]}>
                {selectedBook.genre === Genre.LITERATURE ? '문학' : '기술'}
              </Text>
            </View>
            <Text style={styles.date}>
              등록일: {new Date(selectedBook.createdAt).toLocaleDateString('ko-KR')}
            </Text>
          </View>
        </View>
      </View>

      {selectedBook.description && (
        <View style={styles.section}>
          <Text style={styles.sectionTitle}>책 소개</Text>
          <Text style={styles.description}>{selectedBook.description}</Text>
        </View>
      )}

      <View style={styles.section}>
        <Text style={styles.sectionTitle}>독서 시작하기</Text>
        <Text style={styles.sectionDescription}>
          이 책의 독서 기록을 시작하고 AI 분석을 받아보세요.
        </Text>
        <TouchableOpacity style={styles.startButton} onPress={handleStartReading}>
          <Icon name="book" size={20} color="#fff" style={styles.buttonIcon} />
          <Text style={styles.startButtonText}>독서 시작</Text>
        </TouchableOpacity>
      </View>

      <View style={styles.section}>
        <Text style={styles.sectionTitle}>도서 정보</Text>
        <View style={styles.infoGrid}>
          <View style={styles.infoItem}>
            <Text style={styles.infoLabel}>제목</Text>
            <Text style={styles.infoValue}>{selectedBook.title}</Text>
          </View>
          <View style={styles.infoItem}>
            <Text style={styles.infoLabel}>저자</Text>
            <Text style={styles.infoValue}>{selectedBook.author}</Text>
          </View>
          <View style={styles.infoItem}>
            <Text style={styles.infoLabel}>장르</Text>
            <Text style={styles.infoValue}>
              {selectedBook.genre === Genre.LITERATURE ? '문학' : '기술서'}
            </Text>
          </View>
          <View style={styles.infoItem}>
            <Text style={styles.infoLabel}>등록일</Text>
            <Text style={styles.infoValue}>
              {new Date(selectedBook.createdAt).toLocaleDateString('ko-KR')}
            </Text>
          </View>
        </View>
      </View>
    </ScrollView>
  );
};

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: '#f5f5f5',
  },
  loadingContainer: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
  },
  header: {
    backgroundColor: '#fff',
    padding: 20,
    marginBottom: 16,
  },
  bookInfo: {
    alignItems: 'center',
  },
  title: {
    fontSize: 24,
    fontWeight: 'bold',
    color: '#333',
    textAlign: 'center',
    marginBottom: 8,
  },
  author: {
    fontSize: 18,
    color: '#666',
    marginBottom: 16,
  },
  metaInfo: {
    flexDirection: 'row',
    alignItems: 'center',
    gap: 16,
  },
  genreTag: {
    backgroundColor: '#E3F2FD',
    paddingHorizontal: 12,
    paddingVertical: 6,
    borderRadius: 16,
  },
  technicalTag: {
    backgroundColor: '#FFF3E0',
  },
  genreText: {
    fontSize: 14,
    color: '#1976D2',
    fontWeight: '500',
  },
  technicalText: {
    color: '#F57C00',
  },
  date: {
    fontSize: 14,
    color: '#999',
  },
  section: {
    backgroundColor: '#fff',
    padding: 20,
    marginBottom: 16,
  },
  sectionTitle: {
    fontSize: 18,
    fontWeight: 'bold',
    color: '#333',
    marginBottom: 12,
  },
  sectionDescription: {
    fontSize: 14,
    color: '#666',
    marginBottom: 16,
    lineHeight: 20,
  },
  description: {
    fontSize: 16,
    color: '#333',
    lineHeight: 24,
  },
  startButton: {
    backgroundColor: '#007AFF',
    borderRadius: 12,
    paddingVertical: 16,
    flexDirection: 'row',
    justifyContent: 'center',
    alignItems: 'center',
  },
  buttonIcon: {
    marginRight: 8,
  },
  startButtonText: {
    color: '#fff',
    fontSize: 16,
    fontWeight: 'bold',
  },
  infoGrid: {
    gap: 16,
  },
  infoItem: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    alignItems: 'center',
    paddingVertical: 8,
    borderBottomWidth: 1,
    borderBottomColor: '#f0f0f0',
  },
  infoLabel: {
    fontSize: 16,
    color: '#666',
  },
  infoValue: {
    fontSize: 16,
    color: '#333',
    fontWeight: '500',
    flex: 1,
    textAlign: 'right',
  },
});

export default BookDetailScreen;