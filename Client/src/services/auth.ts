import apiClient from './apiClient';
import type { LoginResponse, UserSummaryDto } from '@/types';

export const loginUser = async (credentials: { email: string; password: string }): Promise<LoginResponse> => {
  const { data } = await apiClient.post<LoginResponse>('/auth/login', credentials);
  return data;
};

export const logoutUser = async () => {
  await apiClient.post('/auth/logout');
};

export const fetchCurrentUser = async (): Promise<UserSummaryDto> => {
  const { data } = await apiClient.get<UserSummaryDto>('/auth/me');
  return data;
};
