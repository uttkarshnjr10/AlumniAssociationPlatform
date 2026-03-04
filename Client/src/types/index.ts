export interface UserSummaryDto {
  id: number;
  name: string;
  email: string;
  role: 'admin' | 'alumnus' | 'student';
  status: 'active' | 'inactive';
  profilePictureUrl: string | null;
}

export interface UserProfileDto {
  id: number;
  name: string;
  email: string;
  role: string;
  status: string;
  bio: string | null;
  graduationYear: number | null;
  department: string | null;
  profilePictureUrl: string | null;
  collegeId: number;
  collegeName: string;
  followersCount: number;
  followingCount: number;
  isFollowedByCurrentUser: boolean;
}

export interface PostDto {
  id: number;
  content: string;
  imageUrl: string | null;
  createdAt: string;
  author: UserSummaryDto;
  likeCount: number;
  commentCount: number;
  isLikedByCurrentUser: boolean;
  collegeId: number;
}

export interface CommentDto {
  id: number;
  text: string;
  createdAt: string;
  author: UserSummaryDto;
  postId: number;
}

export interface EventDto {
  id: number;
  title: string;
  description: string;
  date: string;
  time: string;
  location: string;
  imageUrl: string | null;
  createdAt: string;
  createdBy: UserSummaryDto;
  attendees: UserSummaryDto[];
  collegeId: number;
  isAttending: boolean;
}

export interface CollegeDto {
  id: number;
  name: string;
  address: string;
  status: string;
  adminName: string;
  adminEmail: string;
}

export interface ApiResponse {
  success: boolean;
  message: string;
}

export interface LoginResponse {
  accessToken: string;
  user: UserSummaryDto;
}
