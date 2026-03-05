import apiClient from './apiClient';
import type { UserSummaryDto } from '@/types';

export const searchUsers = async (query: string) => {
  const { data } = await apiClient.get<UserSummaryDto[]>('/search/users', { params: { q: query } });
  return data;
};
