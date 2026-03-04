import apiClient from './apiClient';
import type { PostDto, CommentDto } from '@/types';

export const fetchPosts = async (page = 0, size = 10) => {
  const { data } = await apiClient.get<PostDto[]>('/posts', { params: { page, size, sort: 'createdAt,desc' } });
  return data;
};

export const fetchPostById = async (postId: number) => {
  const { data } = await apiClient.get<PostDto>(`/posts/${postId}`);
  return data;
};

export const createPost = async (content: string, imageFile?: File) => {
  const formData = new FormData();
  formData.append('content', content);
  if (imageFile) formData.append('imageFile', imageFile);
  const { data } = await apiClient.post<PostDto>('/posts', formData);
  return data;
};

export const updatePost = async (postId: number, content: string, imageFile?: File) => {
  const formData = new FormData();
  formData.append('content', content);
  if (imageFile) formData.append('imageFile', imageFile);
  const { data } = await apiClient.put<PostDto>(`/posts/${postId}`, formData);
  return data;
};

export const deletePost = async (postId: number) => {
  await apiClient.delete(`/posts/${postId}`);
};

export const likePost = async (postId: number) => {
  await apiClient.post(`/posts/${postId}/like`);
};

export const unlikePost = async (postId: number) => {
  await apiClient.delete(`/posts/${postId}/like`);
};

export const fetchComments = async (postId: number) => {
  const { data } = await apiClient.get<CommentDto[]>(`/posts/${postId}/comments`);
  return data;
};

export const addComment = async (postId: number, text: string) => {
  const { data } = await apiClient.post<CommentDto>(`/posts/${postId}/comments`, { text });
  return data;
};

export const deleteComment = async (commentId: number) => {
  await apiClient.delete(`/comments/${commentId}`);
};
