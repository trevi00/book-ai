import React, { useEffect } from 'react';
import {
  View,
  Text,
  ScrollView,
  TouchableOpacity,
  StyleSheet,
  Alert,
  Share,
} from 'react-native';
import { NativeStackScreenProps } from '@react-navigation/native-stack';
import { AnalysisStackParamList } from '@/types/navigation';
import { useAppDispatch, useAppSelector } from '@/store';
import { fetchAnalysisById, deleteAnalysis, clearError } from '@/store/slices/analysisSlice';
import { AnalysisType } from '@/types/analysis';
import Icon from 'react-native-vector-icons/MaterialIcons';

type Props = NativeStackScreenProps<AnalysisStackParamList, 'AnalysisDetail'>;

const AnalysisDetailScreen: React.FC<Props> = ({ route, navigation }) => {
  const { analysisId } = route.params;
  const dispatch = useAppDispatch();
  const { currentAnalysis, error } = useAppSelector((state) => state.analysis);

  useEffect(() => {
    loadAnalysisDetail();
  }, [analysisId]);

  useEffect(() => {
    if (error) {
      Alert.alert('오류', error);
      dispatch(clearError());
    }
  }, [error]);

  const loadAnalysisDetail = async () => {
    try {
      await dispatch(fetchAnalysisById(analysisId)).unwrap();
    } catch (error) {
      // 에러는 useEffect에서 처리
    }
  };

  const handleShare = async () => {
    if (!currentAnalysis) return;

    try {
      const shareContent = `📚 AI 분석 결과\n\n${currentAnalysis.content}\n\n생성일: ${new Date(currentAnalysis.createdAt).toLocaleDateString('ko-KR')}`;
      
      await Share.share({
        message: shareContent,
        title: 'AI 분석 결과 공유',
      });
    } catch (error: any) {
      Alert.alert('공유 실패', '분석 결과를 공유할 수 없습니다.');
    }
  };

  const handleDelete = () => {
    if (!currentAnalysis) return;

    Alert.alert(
      '분석 결과 삭제',
      '정말 이 분석 결과를 삭제하시겠습니까?\n삭제된 결과는 복구할 수 없습니다.',
      [
        { text: '취소', style: 'cancel' },
        {
          text: '삭제',
          style: 'destructive',
          onPress: async () => {
            try {
              await dispatch(deleteAnalysis(currentAnalysis.analysisId)).unwrap();
              Alert.alert('삭제 완료', '분석 결과가 삭제되었습니다.');
              navigation.goBack();
            } catch (error: any) {
              Alert.alert('삭제 실패', error);
            }
          },
        },
      ]
    );
  };

  if (!currentAnalysis) {
    return (
      <View style={styles.loadingContainer}>
        <Text>로딩 중...</Text>
      </View>
    );
  }

  const isLiteratureAnalysis = currentAnalysis.analysisType === AnalysisType.LITERATURE_ANALYSIS;

  return (
    <View style={styles.container}>
      <ScrollView contentContainerStyle={styles.scrollContent}>
        <View style={styles.header}>
          <View style={styles.typeInfo}>
            <View
              style={[
                styles.typeTag,
                !isLiteratureAnalysis && styles.technicalTag,
              ]}
            >
              <Text
                style={[
                  styles.typeText,
                  !isLiteratureAnalysis && styles.technicalText,
                ]}
              >
                {isLiteratureAnalysis ? '문학 분석' : '기술 요약'}
              </Text>
            </View>
            <Text style={styles.createdAt}>
              생성일: {new Date(currentAnalysis.createdAt).toLocaleDateString('ko-KR')}
            </Text>
          </View>
        </View>

        <View style={styles.contentSection}>
          <Text style={styles.sectionTitle}>분석 결과</Text>
          <Text style={styles.content}>{currentAnalysis.content}</Text>
        </View>

        <View style={styles.actionsSection}>
          <TouchableOpacity style={styles.actionButton} onPress={handleShare}>
            <Icon name="share" size={20} color="#007AFF" />
            <Text style={styles.actionButtonText}>공유하기</Text>
          </TouchableOpacity>

          <TouchableOpacity 
            style={[styles.actionButton, styles.deleteButton]} 
            onPress={handleDelete}
          >
            <Icon name="delete" size={20} color="#F44336" />
            <Text style={[styles.actionButtonText, styles.deleteButtonText]}>삭제하기</Text>
          </TouchableOpacity>
        </View>

        <View style={styles.infoSection}>
          <Text style={styles.sectionTitle}>분석 정보</Text>
          <View style={styles.infoGrid}>
            <View style={styles.infoItem}>
              <Text style={styles.infoLabel}>분석 ID</Text>
              <Text style={styles.infoValue} numberOfLines={1}>
                {currentAnalysis.analysisId}
              </Text>
            </View>
            <View style={styles.infoItem}>
              <Text style={styles.infoLabel}>사용자 ID</Text>
              <Text style={styles.infoValue}>
                {currentAnalysis.userId}
              </Text>
            </View>
            <View style={styles.infoItem}>
              <Text style={styles.infoLabel}>도서 ID</Text>
              <Text style={styles.infoValue}>
                {currentAnalysis.bookId}
              </Text>
            </View>
            <View style={styles.infoItem}>
              <Text style={styles.infoLabel}>분석 유형</Text>
              <Text style={styles.infoValue}>
                {isLiteratureAnalysis ? '문학 분석' : '기술 요약'}
              </Text>
            </View>
            <View style={styles.infoItem}>
              <Text style={styles.infoLabel}>생성일</Text>
              <Text style={styles.infoValue}>
                {new Date(currentAnalysis.createdAt).toLocaleString('ko-KR')}
              </Text>
            </View>
          </View>
        </View>
      </ScrollView>
    </View>
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
  scrollContent: {
    paddingBottom: 20,
  },
  header: {
    backgroundColor: '#fff',
    padding: 20,
    marginBottom: 16,
  },
  typeInfo: {
    flexDirection: 'row',
    alignItems: 'center',
    justifyContent: 'space-between',
  },
  typeTag: {
    backgroundColor: '#E3F2FD',
    paddingHorizontal: 12,
    paddingVertical: 6,
    borderRadius: 16,
  },
  technicalTag: {
    backgroundColor: '#FFF3E0',
  },
  typeText: {
    fontSize: 14,
    color: '#1976D2',
    fontWeight: '600',
  },
  technicalText: {
    color: '#F57C00',
  },
  createdAt: {
    fontSize: 14,
    color: '#666',
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
    marginBottom: 16,
  },
  content: {
    fontSize: 16,
    color: '#333',
    lineHeight: 24,
  },
  actionsSection: {
    backgroundColor: '#fff',
    marginBottom: 16,
  },
  actionButton: {
    flexDirection: 'row',
    alignItems: 'center',
    paddingHorizontal: 20,
    paddingVertical: 16,
    borderBottomWidth: 1,
    borderBottomColor: '#f0f0f0',
  },
  deleteButton: {
    borderBottomWidth: 0,
  },
  actionButtonText: {
    flex: 1,
    marginLeft: 12,
    fontSize: 16,
    color: '#333',
  },
  deleteButtonText: {
    color: '#F44336',
  },
  infoSection: {
    backgroundColor: '#fff',
    padding: 20,
  },
  infoGrid: {
    gap: 12,
  },
  infoItem: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    alignItems: 'flex-start',
    paddingVertical: 8,
    borderBottomWidth: 1,
    borderBottomColor: '#f0f0f0',
  },
  infoLabel: {
    fontSize: 14,
    color: '#666',
    flex: 1,
  },
  infoValue: {
    fontSize: 14,
    color: '#333',
    fontWeight: '500',
    flex: 2,
    textAlign: 'right',
  },
});

export default AnalysisDetailScreen;