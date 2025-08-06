import React, { useEffect, useState } from 'react';
import {
  View,
  Text,
  FlatList,
  TouchableOpacity,
  StyleSheet,
  RefreshControl,
  Alert,
} from 'react-native';
import { NativeStackScreenProps } from '@react-navigation/native-stack';
import { AnalysisStackParamList } from '@/types/navigation';
import { useAppDispatch, useAppSelector } from '@/store';
import { fetchAnalyses, clearError } from '@/store/slices/analysisSlice';
import { AIAnalysis, AnalysisType } from '@/types/analysis';
import Icon from 'react-native-vector-icons/MaterialIcons';

type Props = NativeStackScreenProps<AnalysisStackParamList, 'AnalysisList'>;

const AnalysisListScreen: React.FC<Props> = ({ navigation }) => {
  const [refreshing, setRefreshing] = useState(false);
  const [filter, setFilter] = useState<'all' | AnalysisType>('all');
  
  const dispatch = useAppDispatch();
  const { analyses, isGenerating, error } = useAppSelector((state) => state.analysis);

  useEffect(() => {
    loadAnalyses();
  }, []);

  useEffect(() => {
    if (error) {
      Alert.alert('오류', error);
      dispatch(clearError());
    }
  }, [error]);

  const loadAnalyses = async () => {
    try {
      await dispatch(fetchAnalyses()).unwrap();
    } catch (error) {
      // 에러는 useEffect에서 처리
    }
  };

  const handleRefresh = async () => {
    setRefreshing(true);
    await loadAnalyses();
    setRefreshing(false);
  };

  const handleAnalysisPress = (analysis: AIAnalysis) => {
    navigation.navigate('AnalysisDetail', { analysisId: analysis.analysisId });
  };

  const filteredAnalyses = analyses.filter((analysis) => {
    if (filter === 'all') return true;
    return analysis.analysisType === filter;
  });

  const renderFilterButton = (
    filterType: 'all' | AnalysisType, 
    label: string, 
    count: number
  ) => (
    <TouchableOpacity
      key={filterType}
      style={[
        styles.filterButton,
        filter === filterType && styles.filterButtonActive,
      ]}
      onPress={() => setFilter(filterType)}
    >
      <Text
        style={[
          styles.filterButtonText,
          filter === filterType && styles.filterButtonTextActive,
        ]}
      >
        {label} ({count})
      </Text>
    </TouchableOpacity>
  );

  const renderAnalysisItem = ({ item }: { item: AIAnalysis }) => (
    <TouchableOpacity style={styles.analysisItem} onPress={() => handleAnalysisPress(item)}>
      <View style={styles.analysisInfo}>
        <View style={styles.analysisHeader}>
          <View
            style={[
              styles.typeTag,
              item.analysisType === AnalysisType.TECHNICAL_SUMMARY && styles.technicalTag,
            ]}
          >
            <Text
              style={[
                styles.typeText,
                item.analysisType === AnalysisType.TECHNICAL_SUMMARY && styles.technicalText,
              ]}
            >
              {item.analysisType === AnalysisType.LITERATURE_ANALYSIS ? '문학 분석' : '기술 요약'}
            </Text>
          </View>
          <Text style={styles.analysisDate}>
            {new Date(item.createdAt).toLocaleDateString('ko-KR')}
          </Text>
        </View>
        
        <Text style={styles.analysisPreview} numberOfLines={3}>
          {item.content}
        </Text>
      </View>
      <Icon name="chevron-right" size={24} color="#999" />
    </TouchableOpacity>
  );

  const renderEmptyComponent = () => (
    <View style={styles.emptyContainer}>
      <Icon name="psychology" size={64} color="#ccc" />
      <Text style={styles.emptyText}>AI 분석 결과가 없습니다</Text>
      <Text style={styles.emptySubtext}>
        독서를 완료한 후 AI 분석을 요청해보세요
      </Text>
    </View>
  );

  const allCount = analyses.length;
  const literatureCount = analyses.filter(a => a.analysisType === AnalysisType.LITERATURE_ANALYSIS).length;
  const technicalCount = analyses.filter(a => a.analysisType === AnalysisType.TECHNICAL_SUMMARY).length;

  return (
    <View style={styles.container}>
      {isGenerating && (
        <View style={styles.generatingBanner}>
          <Icon name="psychology" size={20} color="#FF9800" />
          <Text style={styles.generatingText}>AI 분석을 생성하는 중...</Text>
        </View>
      )}

      <View style={styles.filterContainer}>
        {renderFilterButton('all', '전체', allCount)}
        {renderFilterButton(AnalysisType.LITERATURE_ANALYSIS, '문학', literatureCount)}
        {renderFilterButton(AnalysisType.TECHNICAL_SUMMARY, '기술', technicalCount)}
      </View>

      <FlatList
        data={filteredAnalyses}
        renderItem={renderAnalysisItem}
        keyExtractor={(item) => item.analysisId}
        refreshControl={<RefreshControl refreshing={refreshing} onRefresh={handleRefresh} />}
        ListEmptyComponent={renderEmptyComponent}
        contentContainerStyle={
          filteredAnalyses.length === 0 ? styles.emptyListContainer : styles.listContainer
        }
      />
    </View>
  );
};

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: '#f5f5f5',
  },
  generatingBanner: {
    backgroundColor: '#FFF3E0',
    flexDirection: 'row',
    alignItems: 'center',
    paddingHorizontal: 16,
    paddingVertical: 12,
    borderBottomWidth: 1,
    borderBottomColor: '#e0e0e0',
  },
  generatingText: {
    marginLeft: 8,
    fontSize: 14,
    color: '#F57C00',
    fontWeight: '500',
  },
  filterContainer: {
    backgroundColor: '#fff',
    flexDirection: 'row',
    paddingHorizontal: 16,
    paddingVertical: 12,
    borderBottomWidth: 1,
    borderBottomColor: '#e0e0e0',
  },
  filterButton: {
    flex: 1,
    paddingVertical: 8,
    paddingHorizontal: 12,
    marginHorizontal: 4,
    borderRadius: 20,
    backgroundColor: '#f8f8f8',
    alignItems: 'center',
  },
  filterButtonActive: {
    backgroundColor: '#007AFF',
  },
  filterButtonText: {
    fontSize: 14,
    color: '#666',
    fontWeight: '500',
  },
  filterButtonTextActive: {
    color: '#fff',
  },
  listContainer: {
    padding: 16,
  },
  emptyListContainer: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
  },
  analysisItem: {
    backgroundColor: '#fff',
    borderRadius: 12,
    padding: 16,
    marginBottom: 12,
    flexDirection: 'row',
    alignItems: 'flex-start',
    shadowColor: '#000',
    shadowOffset: { width: 0, height: 2 },
    shadowOpacity: 0.1,
    shadowRadius: 4,
    elevation: 3,
  },
  analysisInfo: {
    flex: 1,
  },
  analysisHeader: {
    flexDirection: 'row',
    alignItems: 'center',
    justifyContent: 'space-between',
    marginBottom: 8,
  },
  typeTag: {
    backgroundColor: '#E3F2FD',
    paddingHorizontal: 8,
    paddingVertical: 4,
    borderRadius: 12,
  },
  technicalTag: {
    backgroundColor: '#FFF3E0',
  },
  typeText: {
    fontSize: 12,
    color: '#1976D2',
    fontWeight: '500',
  },
  technicalText: {
    color: '#F57C00',
  },
  analysisDate: {
    fontSize: 12,
    color: '#999',
  },
  analysisPreview: {
    fontSize: 14,
    color: '#666',
    lineHeight: 20,
  },
  emptyContainer: {
    alignItems: 'center',
    paddingVertical: 48,
  },
  emptyText: {
    fontSize: 18,
    color: '#666',
    marginTop: 16,
    marginBottom: 8,
  },
  emptySubtext: {
    fontSize: 14,
    color: '#999',
    textAlign: 'center',
    paddingHorizontal: 32,
  },
});

export default AnalysisListScreen;