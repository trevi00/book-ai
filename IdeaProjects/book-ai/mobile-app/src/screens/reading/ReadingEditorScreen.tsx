import React, { useState, useEffect } from 'react';
import {
  View,
  Text,
  TextInput,
  TouchableOpacity,
  StyleSheet,
  ScrollView,
  Alert,
  ActivityIndicator,
  KeyboardAvoidingView,
  Platform,
} from 'react-native';
import { NativeStackScreenProps } from '@react-navigation/native-stack';
import { ReadingStackParamList } from '@/types/navigation';
import { useAppDispatch, useAppSelector } from '@/store';
import { 
  createReading, 
  updateReading, 
  fetchReadingById,
  saveDraft,
  removeDraft,
  clearError 
} from '@/store/slices/readingSlice';
import { fetchBooks } from '@/store/slices/bookSlice';
import { Book } from '@/types/book';
import Icon from 'react-native-vector-icons/MaterialIcons';

type Props = NativeStackScreenProps<ReadingStackParamList, 'ReadingEditor'>;

const ReadingEditorScreen: React.FC<Props> = ({ route, navigation }) => {
  const { readingId, bookId } = route.params;
  const [content, setContent] = useState('');
  const [selectedBook, setSelectedBook] = useState<Book | null>(null);
  const [showBookSelector, setShowBookSelector] = useState(false);
  const [lastSavedAt, setLastSavedAt] = useState<Date | null>(null);
  
  const dispatch = useAppDispatch();
  const { currentReading, drafts, isLoading, error } = useAppSelector((state) => state.reading);
  const { books } = useAppSelector((state) => state.book);
  const { user } = useAppSelector((state) => state.auth);

  const isEditing = !!readingId;
  const draftKey = readingId ? `reading-${readingId}` : `new-reading-${bookId || 'no-book'}`;

  useEffect(() => {
    if (isEditing) {
      loadReading();
    } else {
      loadBooks();
      if (bookId) {
        const book = books.find(b => b.id === bookId);
        if (book) {
          setSelectedBook(book);
        }
      }
    }
    
    // 드래프트 로드
    const savedDraft = drafts[draftKey];
    if (savedDraft && !isEditing) {
      setContent(savedDraft);
    }
  }, [readingId, bookId]);

  useEffect(() => {
    if (error) {
      Alert.alert('오류', error);
      dispatch(clearError());
    }
  }, [error]);

  // 자동 저장 (5초마다)
  useEffect(() => {
    const timer = setInterval(() => {
      if (content.trim() && !isEditing) {
        dispatch(saveDraft({ key: draftKey, content }));
        setLastSavedAt(new Date());
      }
    }, 5000);

    return () => clearInterval(timer);
  }, [content, draftKey, isEditing]);

  const loadReading = async () => {
    if (!readingId) return;
    
    try {
      await dispatch(fetchReadingById(readingId)).unwrap();
    } catch (error) {
      // 에러는 useEffect에서 처리
    }
  };

  const loadBooks = async () => {
    try {
      await dispatch(fetchBooks()).unwrap();
    } catch (error) {
      // 에러는 useEffect에서 처리
    }
  };

  useEffect(() => {
    if (currentReading && isEditing) {
      setContent(currentReading.content);
      setSelectedBook(currentReading.book);
    }
  }, [currentReading, isEditing]);

  const handleSave = async () => {
    if (!content.trim()) {
      Alert.alert('오류', '독서 내용을 입력해주세요.');
      return;
    }

    if (!isEditing && !selectedBook) {
      Alert.alert('오류', '도서를 선택해주세요.');
      return;
    }

    if (!user?.id) {
      Alert.alert('오류', '사용자 정보를 찾을 수 없습니다.');
      return;
    }

    try {
      if (isEditing && readingId) {
        await dispatch(updateReading({
          id: readingId,
          data: { content: content.trim() }
        })).unwrap();
        Alert.alert('저장 완료', '독서 기록이 수정되었습니다.');
      } else if (selectedBook) {
        await dispatch(createReading({
          userId: user.id,
          bookId: selectedBook.id,
          content: content.trim(),
        })).unwrap();
        
        // 드래프트 제거
        dispatch(removeDraft(draftKey));
        
        Alert.alert('저장 완료', '새로운 독서 기록이 생성되었습니다.');
      }
      
      navigation.goBack();
    } catch (error: any) {
      Alert.alert('저장 실패', error);
    }
  };

  const handleBookSelect = (book: Book) => {
    setSelectedBook(book);
    setShowBookSelector(false);
  };

  const renderBookSelector = () => {
    if (!showBookSelector) return null;

    return (
      <View style={styles.bookSelectorOverlay}>
        <View style={styles.bookSelectorModal}>
          <View style={styles.modalHeader}>
            <Text style={styles.modalTitle}>도서 선택</Text>
            <TouchableOpacity onPress={() => setShowBookSelector(false)}>
              <Icon name="close" size={24} color="#666" />
            </TouchableOpacity>
          </View>
          <ScrollView style={styles.bookList}>
            {books.map((book) => (
              <TouchableOpacity
                key={book.id}
                style={styles.bookItem}
                onPress={() => handleBookSelect(book)}
              >
                <Text style={styles.bookItemTitle}>{book.title}</Text>
                <Text style={styles.bookItemAuthor}>{book.author}</Text>
              </TouchableOpacity>
            ))}
          </ScrollView>
        </View>
      </View>
    );
  };

  return (
    <KeyboardAvoidingView
      style={styles.container}
      behavior={Platform.OS === 'ios' ? 'padding' : 'height'}
    >
      <View style={styles.header}>
        <View style={styles.headerLeft}>
          <TouchableOpacity onPress={() => navigation.goBack()}>
            <Icon name="arrow-back" size={24} color="#007AFF" />
          </TouchableOpacity>
          <Text style={styles.headerTitle}>
            {isEditing ? '독서 기록 수정' : '새 독서 기록'}
          </Text>
        </View>
        <TouchableOpacity
          style={[styles.saveButton, (!content.trim() || isLoading) && styles.saveButtonDisabled]}
          onPress={handleSave}
          disabled={!content.trim() || isLoading}
        >
          {isLoading ? (
            <ActivityIndicator size="small" color="#007AFF" />
          ) : (
            <Text style={styles.saveButtonText}>저장</Text>
          )}
        </TouchableOpacity>
      </View>

      {!isEditing && (
        <TouchableOpacity
          style={styles.bookSelector}
          onPress={() => setShowBookSelector(true)}
        >
          <Icon name="library-books" size={20} color="#666" />
          <Text style={styles.bookSelectorText}>
            {selectedBook ? `${selectedBook.title} - ${selectedBook.author}` : '도서를 선택하세요'}
          </Text>
          <Icon name="expand-more" size={20} color="#666" />
        </TouchableOpacity>
      )}

      <View style={styles.editorContainer}>
        <Text style={styles.editorLabel}>독서 내용</Text>
        <TextInput
          style={styles.editor}
          multiline
          numberOfLines={20}
          placeholder="독서하면서 느낀 점, 중요한 내용, 인상 깊었던 구절 등을 자유롭게 작성해보세요..."
          value={content}
          onChangeText={setContent}
          textAlignVertical="top"
          maxLength={10000}
        />
        <View style={styles.editorFooter}>
          <Text style={styles.charCount}>{content.length}/10,000</Text>
          {lastSavedAt && !isEditing && (
            <Text style={styles.autoSaveText}>
              자동 저장됨: {lastSavedAt.toLocaleTimeString('ko-KR')}
            </Text>
          )}
        </View>
      </View>

      {renderBookSelector()}
    </KeyboardAvoidingView>
  );
};

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: '#f5f5f5',
  },
  header: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    alignItems: 'center',
    paddingHorizontal: 16,
    paddingVertical: 12,
    backgroundColor: '#fff',
    borderBottomWidth: 1,
    borderBottomColor: '#e0e0e0',
  },
  headerLeft: {
    flexDirection: 'row',
    alignItems: 'center',
    flex: 1,
  },
  headerTitle: {
    fontSize: 18,
    fontWeight: 'bold',
    color: '#333',
    marginLeft: 12,
  },
  saveButton: {
    paddingHorizontal: 16,
    paddingVertical: 8,
    borderRadius: 8,
    backgroundColor: '#f0f0f0',
  },
  saveButtonDisabled: {
    opacity: 0.5,
  },
  saveButtonText: {
    fontSize: 16,
    color: '#007AFF',
    fontWeight: '600',
  },
  bookSelector: {
    flexDirection: 'row',
    alignItems: 'center',
    backgroundColor: '#fff',
    paddingHorizontal: 16,
    paddingVertical: 12,
    marginBottom: 1,
    borderBottomWidth: 1,
    borderBottomColor: '#e0e0e0',
  },
  bookSelectorText: {
    flex: 1,
    marginLeft: 8,
    fontSize: 16,
    color: '#333',
  },
  editorContainer: {
    flex: 1,
    backgroundColor: '#fff',
    margin: 16,
    borderRadius: 12,
    padding: 16,
  },
  editorLabel: {
    fontSize: 16,
    fontWeight: 'bold',
    color: '#333',
    marginBottom: 12,
  },
  editor: {
    flex: 1,
    fontSize: 16,
    color: '#333',
    lineHeight: 24,
    textAlignVertical: 'top',
  },
  editorFooter: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    alignItems: 'center',
    paddingTop: 12,
    borderTopWidth: 1,
    borderTopColor: '#f0f0f0',
  },
  charCount: {
    fontSize: 12,
    color: '#999',
  },
  autoSaveText: {
    fontSize: 12,
    color: '#666',
  },
  bookSelectorOverlay: {
    position: 'absolute',
    top: 0,
    left: 0,
    right: 0,
    bottom: 0,
    backgroundColor: 'rgba(0, 0, 0, 0.5)',
    justifyContent: 'center',
    alignItems: 'center',
  },
  bookSelectorModal: {
    backgroundColor: '#fff',
    borderRadius: 12,
    margin: 20,
    maxHeight: '70%',
    width: '90%',
  },
  modalHeader: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    alignItems: 'center',
    padding: 16,
    borderBottomWidth: 1,
    borderBottomColor: '#e0e0e0',
  },
  modalTitle: {
    fontSize: 18,
    fontWeight: 'bold',
    color: '#333',
  },
  bookList: {
    maxHeight: 400,
  },
  bookItem: {
    padding: 16,
    borderBottomWidth: 1,
    borderBottomColor: '#f0f0f0',
  },
  bookItemTitle: {
    fontSize: 16,
    fontWeight: 'bold',
    color: '#333',
    marginBottom: 4,
  },
  bookItemAuthor: {
    fontSize: 14,
    color: '#666',
  },
});

export default ReadingEditorScreen;