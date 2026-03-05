import apiClient from './apiClient';
import type { CollegeDto } from '@/types';

export const registerCollege = async (data: {
  collegeName: string;
  address: string;
  contactPersonName: string;
  contactEmail: string;
  contactPhone: string;
  adminName: string;
  adminEmail: string;
  adminPassword: string;
}) => {
  const { data: result } = await apiClient.post<CollegeDto>('/colleges/register', data);
  return result;
};
