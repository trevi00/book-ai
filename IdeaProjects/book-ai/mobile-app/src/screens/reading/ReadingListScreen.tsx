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
import { ReadingStackParamList } from '@/types/navigation';
import { useAppDispatch, useAppSelector } from '@/store';
import { fetchReadings, clearError } from '@/store/slices/readingSlice';
import { ReadingRecord, ReadingStatus } from '@/types/reading';
import Icon from 'react-native-vector-icons/MaterialIcons';

type Props = NativeStackScreenProps<ReadingStackParamList, 'ReadingList'>;

const ReadingListScreen: React.FC<Props> = ({ navigation }) => {
  const [refreshing, setRefreshing] = useState(false);
  const [filter, setFilter] = useState<'all' | ReadingStatus>('all');
  
  const dispatch = useAppDispatch();
  const { readings, isLoading, error } = useAppSelector((state) => state.reading);

  useEffect(() => {
    loadReadings();
  }, []);

  useEffect(() => {
    if (error) {
      Alert.alert('오류', error);
      dispatch(clearError());
    }
  }, [error]);

  const loadReadings = async () => {
    try {
      await dispatch(fetchReadings()).unwrap();
    } catch (error) {
      // 에러는 useEffect에서 처리
    }
  };

  const handleRefresh = async () => {
    setRefreshing(true);
    await loadReadings();
    setRefreshing(false);
  };

  const handleReadingPress = (reading: ReadingRecord) => {
    navigation.navigate('ReadingDetail', { readingId: reading.id });
  };

  const handleCreateReading = () => {
    navigation.navigate('ReadingEditor', {});
  };

  const filteredReadings = readings.filter((reading) => {
    if (filter === 'all') return true;
    return reading.status === filter;
  });

  const renderFilterButton = (filterType: 'all' | ReadingStatus, label: string, count: number) => (
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

  const renderReadingItem = ({ item }: { item: ReadingRecord }) => (
    <TouchableOpacity style={styles.readingItem} onPress={() => handleReadingPress(item)}>
      <View style={styles.readingInfo}>
        <Text style={styles.bookTitle}>{item.book.title}</Text>
        <Text style={styles.bookAuthor}>{item.book.author}</Text>
        <View style={styles.readingMeta}>
          <View
            style={[
              styles.statusTag,
              item.status === ReadingStatus.COMPLETED && styles.completedTag,
            ]}
          >
            <Text
              style={[
                styles.statusText,
                item.status === ReadingStatus.COMPLETED && styles.completedText,
              ]}
            >
              {item.status === ReadingStatus.IN_PROGRESS ? '읽는 중' : '완료됨'}
            </Text>
          </View>
          <Text style={styles.readingDate}>
            {new Date(item.updatedAt).toLocaleDateString('ko-KR')}
          </Text>
        </View>
        {item.content && (
          <Text style={styles.contentPreview} numberOfLines={2}>
            {item.content}
          </Text>
        )}
      </View>
      <Icon name="chevron-right" size={24} color="#999" />
    </TouchableOpacity>
  );

  const renderEmptyComponent = () => (
    <View style={styles.emptyContainer}>
      <Icon name="book" size={64} color="#ccc" />
      <Text style={styles.emptyText}>독서 기록이 없습니다</Text>
      <Text style={styles.emptySubtext}>새로운 독서 기록을 시작해보세요</Text>
    </View>
  );

  const allCount = readings.length;
  const inProgressCount = readings.filter(r => r.status === ReadingStatus.IN_PROGRESS).length;
  const completedCount = readings.filter(r => r.status === ReadingStatus.COMPLETED).length;

  return (
    <View style={styles.container}>
      <View style={styles.filterContainer}>
        {renderFilterButton('all', '전체', allCount)}
        {renderFilterButton(ReadingStatus.IN_PROGRESS, '읽는 중', inProgressCount)}
        {renderFilterButton(ReadingStatus.COMPLETED, '완료', completedCount)}
      </View>

      <FlatList
        data={filteredReadings}
        renderItem={renderReadingItem}
        keyExtractor={(item) => item.id.toString()}
        refreshControl={<RefreshControl refreshing={refreshing} onRefresh={handleRefresh} />}
        ListEmptyComponent={!isLoading ? renderEmptyComponent : null}
        contentContainerStyle={
          filteredReadings.length === 0 ? styles.emptyListContainer : styles.listContainer
        }
      />

      <TouchableOpacity style={styles.fab} onPress={handleCreateReading}>
        <Icon name="add" size={24} color="#fff" />
      </TouchableOpacity>
    </View>
  );
};

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: '#f5f5f5',
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
  readingItem: {
    backgroundColor: '#fff',
    borderRadius: 12,
    padding: 16,
    marginBottom: 12,
    flexDirection: 'row',
    alignItems: 'center',
    shadowColor: '#000',
    shadowOffset: { width: 0, height: 2 },
    shadowOpacity: 0.1,
    shadowRadius: 4,
    elevation: 3,
  },
  readingInfo: {
    flex: 1,
  },
  bookTitle: {
    fontSize: 16,
    fontWeight: 'bold',
    color: '#333',
    marginBottom: 4,
  },
  bookAuthor: {
    fontSize: 14,
    color: '#666',
    marginBottom: 8,
  },
  readingMeta: {
    flexDirection: 'row',
    alignItems: 'center',
    justifyContent: 'space-between',
    marginBottom: 8,
  },
  statusTag: {
    backgroundColor: '#FFF3E0',
    paddingHorizontal: 8,
    paddingVertical: 4,
    borderRadius: 12,
  },
  completedTag: {
    backgroundColor: '#E8F5E8',
  },
  statusText: {
    fontSize: 12,
    color: '#F57C00',
    fontWeight: '500',
  },
  completedText: {
    color: '#2E7D32',
  },
  readingDate: {
    fontSize: 12,
    color: '#999',
  },
  contentPreview: {
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
  },
  fab: {
    position: 'absolute',
    bottom: 24,
    right: 24,
    width: 56,
    height: 56,
    borderRadius: 28,
    backgroundColor: '#007AFF',
    justifyContent: 'center',
    alignItems: 'center',
    shadowColor: '#000',
    shadowOffset: { width: 0, height: 4 },
    shadowOpacity: 0.3,
    shadowRadius: 8,
    elevation: 8,
  },
});

export default ReadingListScreen;