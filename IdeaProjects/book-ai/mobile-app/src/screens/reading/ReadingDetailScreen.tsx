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
import { ReadingStackParamList } from '@/types/navigation';
import { useAppDispatch, useAppSelector } from '@/store';
import { fetchReadingById, completeReading, deleteReading, clearError } from '@/store/slices/readingSlice';
import { generateAnalysis } from '@/store/slices/analysisSlice';
import { ReadingStatus } from '@/types/reading';
import { AnalysisType } from '@/types/analysis';
import { Genre } from '@/types/book';
import Icon from 'react-native-vector-icons/MaterialIcons';

type Props = NativeStackScreenProps<ReadingStackParamList, 'ReadingDetail'>;

const ReadingDetailScreen: React.FC<Props> = ({ route, navigation }) => {
  const { readingId } = route.params;
  const dispatch = useAppDispatch();
  const { currentReading, isLoading, error } = useAppSelector((state) => state.reading);
  const { isGenerating } = useAppSelector((state) => state.analysis);

  useEffect(() => {
    loadReadingDetail();
  }, [readingId]);

  useEffect(() => {
    if (error) {
      Alert.alert('오류', error);
      dispatch(clearError());
    }
  }, [error]);

  const loadReadingDetail = async () => {
    try {
      await dispatch(fetchReadingById(readingId)).unwrap();
    } catch (error) {
      // 에러는 useEffect에서 처리
    }
  };

  const handleEdit = () => {
    if (currentReading) {
      navigation.navigate('ReadingEditor', { readingId: currentReading.id });
    }
  };

  const handleComplete = async () => {
    if (!currentReading) return;

    Alert.alert(
      '독서 완료',
      '독서를 완료로 표시하시겠습니까?\n완료 후에는 AI 분석을 요청할 수 있습니다.',
      [
        { text: '취소', style: 'cancel' },
        {
          text: '완료',
          onPress: async () => {
            try {
              await dispatch(completeReading(currentReading.id)).unwrap();
              Alert.alert('성공', '독서가 완료로 표시되었습니다.');
            } catch (error: any) {
              Alert.alert('오류', error);
            }
          },
        },
      ]
    );
  };

  const handleGenerateAnalysis = async () => {
    if (!currentReading) return;

    const analysisType = currentReading.book.genre === Genre.LITERATURE 
      ? AnalysisType.LITERATURE_ANALYSIS 
      : AnalysisType.TECHNICAL_SUMMARY;

    try {
      await dispatch(generateAnalysis({
        readingId: currentReading.id.toString(),
        analysisType,
      })).unwrap();
      
      Alert.alert(
        'AI 분석 완료',
        'AI 분석이 완료되었습니다. 분석 결과를 확인하시겠습니까?',
        [
          { text: '나중에', style: 'cancel' },
          {
            text: '확인',
            onPress: () => {
              // 분석 탭으로 이동
              navigation.navigate('AnalysisList');
            },
          },
        ]
      );
    } catch (error: any) {
      Alert.alert('분석 실패', error);
    }
  };

  const handleDelete = () => {
    if (!currentReading) return;

    Alert.alert(
      '독서 기록 삭제',
      '정말 이 독서 기록을 삭제하시겠습니까?\n삭제된 기록은 복구할 수 없습니다.',
      [
        { text: '취소', style: 'cancel' },
        {
          text: '삭제',
          style: 'destructive',
          onPress: async () => {
            try {
              await dispatch(deleteReading(currentReading.id)).unwrap();
              Alert.alert('삭제 완료', '독서 기록이 삭제되었습니다.');
              navigation.goBack();
            } catch (error: any) {
              Alert.alert('삭제 실패', error);
            }
          },
        },
      ]
    );
  };

  if (isLoading || !currentReading) {
    return (
      <View style={styles.loadingContainer}>
        <Text>로딩 중...</Text>
      </View>
    );
  }

  const isCompleted = currentReading.status === ReadingStatus.COMPLETED;
  const canGenerateAnalysis = isCompleted && currentReading.content.trim().length > 0;

  return (
    <ScrollView style={styles.container}>
      <View style={styles.header}>
        <Text style={styles.bookTitle}>{currentReading.book.title}</Text>
        <Text style={styles.bookAuthor}>{currentReading.book.author}</Text>
        <View style={styles.statusContainer}>
          <View
            style={[
              styles.statusTag,
              isCompleted && styles.completedTag,
            ]}
          >
            <Text
              style={[
                styles.statusText,
                isCompleted && styles.completedText,
              ]}
            >
              {isCompleted ? '완료됨' : '읽는 중'}
            </Text>
          </View>
          <Text style={styles.date}>
            최종 수정: {new Date(currentReading.updatedAt).toLocaleDateString('ko-KR')}
          </Text>
        </View>
      </View>

      <View style={styles.contentSection}>
        <Text style={styles.sectionTitle}>독서 내용</Text>
        {currentReading.content ? (
          <Text style={styles.content}>{currentReading.content}</Text>
        ) : (
          <Text style={styles.emptyContent}>작성된 독서 내용이 없습니다.</Text>
        )}
      </View>

      <View style={styles.actionSection}>
        <Text style={styles.sectionTitle}>작업</Text>
        
        <TouchableOpacity style={styles.actionButton} onPress={handleEdit}>
          <Icon name="edit" size={20} color="#007AFF" />
          <Text style={styles.actionButtonText}>수정하기</Text>
          <Icon name="chevron-right" size={20} color="#999" />
        </TouchableOpacity>

        {!isCompleted && (
          <TouchableOpacity style={styles.actionButton} onPress={handleComplete}>
            <Icon name="check-circle" size={20} color="#4CAF50" />
            <Text style={styles.actionButtonText}>독서 완료</Text>
            <Icon name="chevron-right" size={20} color="#999" />
          </TouchableOpacity>
        )}

        {canGenerateAnalysis && (
          <TouchableOpacity 
            style={[styles.actionButton, isGenerating && styles.actionButtonDisabled]} 
            onPress={handleGenerateAnalysis}
            disabled={isGenerating}
          >
            <Icon name="psychology" size={20} color="#FF9800" />
            <Text style={styles.actionButtonText}>
              {isGenerating ? 'AI 분석 중...' : 'AI 분석 요청'}
            </Text>
            <Icon name="chevron-right" size={20} color="#999" />
          </TouchableOpacity>
        )}

        <TouchableOpacity style={[styles.actionButton, styles.dangerButton]} onPress={handleDelete}>
          <Icon name="delete" size={20} color="#F44336" />
          <Text style={[styles.actionButtonText, styles.dangerButtonText]}>삭제하기</Text>
          <Icon name="chevron-right" size={20} color="#999" />
        </TouchableOpacity>
      </View>

      <View style={styles.infoSection}>
        <Text style={styles.sectionTitle}>정보</Text>
        <View style={styles.infoItem}>
          <Text style={styles.infoLabel}>생성일</Text>
          <Text style={styles.infoValue}>
            {new Date(currentReading.createdAt).toLocaleDateString('ko-KR')}
          </Text>
        </View>
        <View style={styles.infoItem}>
          <Text style={styles.infoLabel}>수정일</Text>
          <Text style={styles.infoValue}>
            {new Date(currentReading.updatedAt).toLocaleDateString('ko-KR')}
          </Text>
        </View>
        <View style={styles.infoItem}>
          <Text style={styles.infoLabel}>상태</Text>
          <Text style={styles.infoValue}>
            {isCompleted ? '완료됨' : '진행 중'}
          </Text>
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
  bookTitle: {
    fontSize: 20,
    fontWeight: 'bold',
    color: '#333',
    marginBottom: 4,
  },
  bookAuthor: {
    fontSize: 16,
    color: '#666',
    marginBottom: 12,
  },
  statusContainer: {
    flexDirection: 'row',
    alignItems: 'center',
    justifyContent: 'space-between',
  },
  statusTag: {
    backgroundColor: '#FFF3E0',
    paddingHorizontal: 12,
    paddingVertical: 6,
    borderRadius: 16,
  },
  completedTag: {
    backgroundColor: '#E8F5E8',
  },
  statusText: {
    fontSize: 14,
    color: '#F57C00',
    fontWeight: '500',
  },
  completedText: {
    color: '#2E7D32',
  },
  date: {
    fontSize: 14,
    color: '#999',
  },
  contentSection: {
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
  content: {
    fontSize: 16,
    color: '#333',
    lineHeight: 24,
  },
  emptyContent: {
    fontSize: 16,
    color: '#999',
    fontStyle: 'italic',
    textAlign: 'center',
    paddingVertical: 20,
  },
  actionSection: {
    backgroundColor: '#fff',
    padding: 20,
    marginBottom: 16,
  },
  actionButton: {
    flexDirection: 'row',
    alignItems: 'center',
    paddingVertical: 16,
    borderBottomWidth: 1,
    borderBottomColor: '#f0f0f0',
  },
  actionButtonDisabled: {
    opacity: 0.6,
  },
  actionButtonText: {
    flex: 1,
    marginLeft: 12,
    fontSize: 16,
    color: '#333',
  },
  dangerButton: {
    borderBottomWidth: 0,
  },
  dangerButtonText: {
    color: '#F44336',
  },
  infoSection: {
    backgroundColor: '#fff',
    padding: 20,
    marginBottom: 16,
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
  },
});

export default ReadingDetailScreen;