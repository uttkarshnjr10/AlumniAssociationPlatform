import apiClient from './apiClient';
import type { EventDto, ApiResponse } from '@/types';

export const fetchEvents = async () => {
  const { data } = await apiClient.get<EventDto[]>('/events');
  return data;
};

export const fetchEventById = async (eventId: number) => {
  const { data } = await apiClient.get<EventDto>(`/events/${eventId}`);
  return data;
};

export const createEvent = async (eventData: { title: string; description: string; date: string; time: string; location: string; collegeId: number }, imageFile?: File) => {
  const formData = new FormData();
  Object.entries(eventData).forEach(([key, val]) => formData.append(key, String(val)));
  if (imageFile) formData.append('imageFile', imageFile);
  const { data } = await apiClient.post<EventDto>('/events', formData);
  return data;
};

export const updateEvent = async (eventId: number, eventData: { title: string; description: string; date: string; time: string; location: string; collegeId: number }, imageFile?: File) => {
  const formData = new FormData();
  Object.entries(eventData).forEach(([key, val]) => formData.append(key, String(val)));
  if (imageFile) formData.append('imageFile', imageFile);
  const { data } = await apiClient.put<EventDto>(`/events/${eventId}`, formData);
  return data;
};

export const deleteEvent = async (eventId: number) => {
  await apiClient.delete(`/events/${eventId}`);
};

export const joinEvent = async (eventId: number) => {
  const { data } = await apiClient.post<ApiResponse>(`/events/${eventId}/join`);
  return data;
};

export const leaveEvent = async (eventId: number) => {
  await apiClient.delete(`/events/${eventId}/join`);
};
