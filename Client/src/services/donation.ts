import apiClient from './apiClient';
import type { ApiResponse } from '@/types';

export const processDonation = async (data: { amount: number; message?: string; paymentMethod: string }) => {
  const { data: result } = await apiClient.post<ApiResponse>('/donations', data);
  return result;
};
