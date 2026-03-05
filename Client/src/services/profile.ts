import apiClient from './apiClient';
import type { UserProfileDto, UserSummaryDto } from '@/types';

export const fetchUserProfileById = async (userId: number) => {
  const { data } = await apiClient.get<UserProfileDto>(`/users/${userId}`);
  return data;
};

export const updateUserProfile = async (profileData: { name: string; bio?: string; graduationYear?: number; department?: string }) => {
  const { data } = await apiClient.put<UserProfileDto>('/users/me', profileData);
  return data;
};

export const fetchFollowers = async (userId: number) => {
  const { data } = await apiClient.get<UserSummaryDto[]>(`/users/${userId}/followers`);
  return data;
};

export const fetchFollowing = async (userId: number) => {
  const { data } = await apiClient.get<UserSummaryDto[]>(`/users/${userId}/following`);
  return data;
};

export const followUser = async (userId: number) => {
  await apiClient.post(`/users/${userId}/follow`);
};

export const unfollowUser = async (userId: number) => {
  await apiClient.delete(`/users/${userId}/follow`);
};
