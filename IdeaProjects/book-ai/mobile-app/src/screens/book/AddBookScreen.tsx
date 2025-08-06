import React, { useState } from 'react';
import {
  View,
  Text,
  TextInput,
  TouchableOpacity,
  StyleSheet,
  ScrollView,
  Alert,
  ActivityIndicator,
} from 'react-native';
import { NativeStackScreenProps } from '@react-navigation/native-stack';
import { BookStackParamList } from '@/types/navigation';
import { useAppDispatch, useAppSelector } from '@/store';
import { createBook, clearError } from '@/store/slices/bookSlice';
import { Genre } from '@/types/book';
import Icon from 'react-native-vector-icons/MaterialIcons';

type Props = NativeStackScreenProps<BookStackParamList, 'AddBook'>;

const AddBookScreen: React.FC<Props> = ({ navigation }) => {
  const [title, setTitle] = useState('');
  const [author, setAuthor] = useState('');
  const [description, setDescription] = useState('');
  const [selectedGenre, setSelectedGenre] = useState<Genre>(Genre.LITERATURE);
  
  const dispatch = useAppDispatch();
  const { isLoading, error } = useAppSelector((state) => state.book);
  const { user } = useAppSelector((state) => state.auth);

  const handleSubmit = async () => {
    if (!title.trim() || !author.trim()) {
      Alert.alert('오류', '제목과 저자를 입력해주세요.');
      return;
    }

    if (!user?.id) {
      Alert.alert('오류', '사용자 정보를 찾을 수 없습니다.');
      return;
    }

    try {
      await dispatch(createBook({
        title: title.trim(),
        author: author.trim(),
        description: description.trim() || undefined,
        genre: selectedGenre,
        userId: user.id,
      })).unwrap();
      
      Alert.alert('성공', '도서가 성공적으로 등록되었습니다.', [
        { text: '확인', onPress: () => navigation.goBack() },
      ]);
    } catch (error: any) {
      Alert.alert('등록 실패', error);
    }
  };

  const renderGenreOption = (genre: Genre, label: string, icon: string) => (
    <TouchableOpacity
      key={genre}
      style={[
        styles.genreOption,
        selectedGenre === genre && styles.genreOptionSelected,
      ]}
      onPress={() => setSelectedGenre(genre)}
    >
      <Icon
        name={icon}
        size={24}
        color={selectedGenre === genre ? '#007AFF' : '#666'}
      />
      <Text
        style={[
          styles.genreOptionText,
          selectedGenre === genre && styles.genreOptionTextSelected,
        ]}
      >
        {label}
      </Text>
      {selectedGenre === genre && (
        <Icon name="check-circle" size={20} color="#007AFF" />
      )}
    </TouchableOpacity>
  );

  return (
    <ScrollView style={styles.container} contentContainerStyle={styles.contentContainer}>
      <View style={styles.form}>
        <View style={styles.inputGroup}>
          <Text style={styles.label}>도서 제목 *</Text>
          <TextInput
            style={styles.input}
            placeholder="도서 제목을 입력하세요"
            value={title}
            onChangeText={setTitle}
            maxLength={200}
          />
        </View>

        <View style={styles.inputGroup}>
          <Text style={styles.label}>저자 *</Text>
          <TextInput
            style={styles.input}
            placeholder="저자명을 입력하세요"
            value={author}
            onChangeText={setAuthor}
            maxLength={100}
          />
        </View>

        <View style={styles.inputGroup}>
          <Text style={styles.label}>장르 선택 *</Text>
          <View style={styles.genreContainer}>
            {renderGenreOption(Genre.LITERATURE, '문학', 'menu-book')}
            {renderGenreOption(Genre.TECHNICAL, '기술서', 'code')}
          </View>
          <Text style={styles.genreHelp}>
            장르에 따라 AI 분석 방식이 달라집니다.
          </Text>
        </View>

        <View style={styles.inputGroup}>
          <Text style={styles.label}>책 소개 (선택사항)</Text>
          <TextInput
            style={[styles.input, styles.textArea]}
            placeholder="책에 대한 간단한 소개를 작성해주세요 (선택사항)"
            value={description}
            onChangeText={setDescription}
            multiline
            numberOfLines={4}
            textAlignVertical="top"
            maxLength={1000}
          />
          <Text style={styles.charCount}>{description.length}/1000</Text>
        </View>

        <TouchableOpacity
          style={[styles.submitButton, isLoading && styles.submitButtonDisabled]}
          onPress={handleSubmit}
          disabled={isLoading}
        >
          {isLoading ? (
            <ActivityIndicator color="#fff" />
          ) : (
            <>
              <Icon name="add" size={20} color="#fff" style={styles.buttonIcon} />
              <Text style={styles.submitButtonText}>도서 등록</Text>
            </>
          )}
        </TouchableOpacity>
      </View>
    </ScrollView>
  );
};

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: '#f5f5f5',
  },
  contentContainer: {
    flexGrow: 1,
  },
  form: {
    padding: 20,
  },
  inputGroup: {
    marginBottom: 24,
  },
  label: {
    fontSize: 16,
    fontWeight: 'bold',
    color: '#333',
    marginBottom: 8,
  },
  input: {
    backgroundColor: '#fff',
    borderRadius: 12,
    paddingHorizontal: 16,
    paddingVertical: 16,
    fontSize: 16,
    borderWidth: 1,
    borderColor: '#e0e0e0',
  },
  textArea: {
    height: 100,
    paddingTop: 16,
  },
  charCount: {
    textAlign: 'right',
    fontSize: 12,
    color: '#999',
    marginTop: 4,
  },
  genreContainer: {
    gap: 12,
  },
  genreOption: {
    backgroundColor: '#fff',
    borderRadius: 12,
    padding: 16,
    flexDirection: 'row',
    alignItems: 'center',
    borderWidth: 2,
    borderColor: '#e0e0e0',
  },
  genreOptionSelected: {
    borderColor: '#007AFF',
    backgroundColor: '#F0F8FF',
  },
  genreOptionText: {
    flex: 1,
    marginLeft: 12,
    fontSize: 16,
    color: '#333',
  },
  genreOptionTextSelected: {
    color: '#007AFF',
    fontWeight: '600',
  },
  genreHelp: {
    fontSize: 14,
    color: '#666',
    marginTop: 8,
    fontStyle: 'italic',
  },
  submitButton: {
    backgroundColor: '#007AFF',
    borderRadius: 12,
    paddingVertical: 16,
    flexDirection: 'row',
    justifyContent: 'center',
    alignItems: 'center',
    marginTop: 20,
  },
  submitButtonDisabled: {
    opacity: 0.7,
  },
  buttonIcon: {
    marginRight: 8,
  },
  submitButtonText: {
    color: '#fff',
    fontSize: 16,
    fontWeight: 'bold',
  },
});

export default AddBookScreen;