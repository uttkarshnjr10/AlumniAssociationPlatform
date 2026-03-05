import apiClient from './apiClient';
import type { UserSummaryDto } from '@/types';

export const fetchManagedUsers = async () => {
  const { data } = await apiClient.get<UserSummaryDto[]>('/admin/users');
  return data;
};

export const addManagedUser = async (userData: { name: string; email: string; password: string; role: string }) => {
  const { data } = await apiClient.post<UserSummaryDto>('/admin/users', userData);
  return data;
};

export const removeManagedUser = async (userId: number) => {
  await apiClient.delete(`/admin/users/${userId}`);
};

export const changeUserStatus = async (userId: number, status: 'active' | 'inactive') => {
  const { data } = await apiClient.patch<UserSummaryDto>(`/admin/users/${userId}/status`, { status });
  return data;
};

export const removeEventByAdmin = async (eventId: number) => {
  await apiClient.delete(`/admin/events/${eventId}`);
};
