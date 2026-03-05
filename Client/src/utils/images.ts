import { API_BASE_URL } from '@/services/apiClient';

export const getPostImageUrl = (filename: string) =>
  `${API_BASE_URL}/posts/uploads/${filename}`;

export const getEventImageUrl = (filename: string) =>
  `${API_BASE_URL}/events/uploads/${filename}`;

export const getProfileImageUrl = (url: string | null) => {
  if (!url) return null;
  if (url.startsWith('http')) return url;
  return `${API_BASE_URL}${url}`;
};
