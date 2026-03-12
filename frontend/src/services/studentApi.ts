import api from './api';

type BackendPage<T> = {
  content: T[];
  totalElements: number;
  totalPages: number;
  size: number;
  number: number;
};

type BackendLesson = {
  id?: string;
  title?: string;
  contentType?: string;
  contentUrl?: string;
  contentText?: string;
};

type BackendModule = {
  id?: string;
  title?: string;
  lessons?: BackendLesson[];
};

type BackendCourse = {
  id: string;
  title?: string;
  level?: string;
  enrolledCount?: number;
  modules?: BackendModule[];
  description?: string;
};

type BackendDiscussion = {
  id: string;
  title?: string;
  authorName?: string;
  category?: string;
  replyCount?: number;
};

type BackendNotification = {
  id: string;
  title?: string;
  message?: string;
  type?: string;
  isRead?: boolean;
  createdAt?: string;
};

export type StudentCourse = {
  id: string;
  title: string;
  level: string;
  enrolled: number;
  videos: string[];
  articles: string[];
  description?: string;
};

export type StudentDiscussion = {
  id: string;
  title: string;
  author: string;
  category: string;
  replies: number;
  spam: boolean;
  flagged: boolean;
};

export type StudentNotification = {
  id: string;
  title: string;
  message: string;
  channel: string;
  audience: string;
  status: 'Sent' | 'Scheduled' | 'Draft';
  createdAt: string;
};

function mapCourse(course: BackendCourse): StudentCourse {
  const lessons = (course.modules ?? []).flatMap((module) => module.lessons ?? []);

  const videos = lessons
    .filter((lesson) => (lesson.contentType ?? '').toUpperCase() === 'VIDEO')
    .map((lesson) => lesson.contentUrl || lesson.title || lesson.id || 'Video lesson');

  const articles = lessons
    .filter((lesson) => (lesson.contentType ?? '').toUpperCase() === 'TEXT')
    .map((lesson) => lesson.contentUrl || lesson.title || lesson.id || 'Article lesson');

  return {
    id: course.id,
    title: course.title || 'Untitled Course',
    level: normalizeLevel(course.level),
    enrolled: course.enrolledCount ?? 0,
    videos,
    articles,
    description: course.description,
  };
}

function normalizeLevel(level?: string): string {
  const value = (level || 'BEGINNER').toUpperCase();
  if (value === 'INTERMEDIATE') return 'Intermediate';
  if (value === 'ADVANCED') return 'Advanced';
  return 'Beginner';
}

export async function fetchPublishedCourses(page = 0, size = 50): Promise<StudentCourse[]> {
  const response = await api.get<BackendPage<BackendCourse>>('/courses/published', {
    params: { page, size },
  });

  return (response.data.content ?? []).map(mapCourse);
}

export async function fetchCourseById(courseId: string): Promise<StudentCourse> {
  const response = await api.get<BackendCourse>(`/courses/${courseId}`);
  return mapCourse(response.data);
}

export async function fetchDiscussions(): Promise<StudentDiscussion[]> {
  const response = await api.get<BackendDiscussion[]>('/discussions');
  return (response.data ?? []).map((row) => ({
    id: row.id,
    title: row.title || 'Untitled discussion',
    author: row.authorName || 'Anonymous',
    category: row.category || 'General',
    replies: row.replyCount ?? 0,
    spam: false,
    flagged: false,
  }));
}

export async function createDiscussion(payload: {
  title: string;
  category: string;
  content: string;
  problemId?: string;
}): Promise<StudentDiscussion> {
  const response = await api.post<BackendDiscussion>('/discussions', {
    title: payload.title,
    category: payload.category,
    content: payload.content,
    problemId: payload.problemId,
  });

  return {
    id: response.data.id,
    title: response.data.title || payload.title,
    author: response.data.authorName || 'You',
    category: response.data.category || payload.category,
    replies: response.data.replyCount ?? 0,
    spam: false,
    flagged: false,
  };
}

export async function fetchNotifications(page = 0, size = 50): Promise<StudentNotification[]> {
  const response = await api.get<BackendNotification[]>('/notifications', {
    params: { page, size },
  });

  return (response.data ?? []).map((item) => ({
    id: item.id,
    title: item.title || 'Notification',
    message: item.message || '',
    channel: 'In-App',
    audience: 'Student',
    status: 'Sent',
    createdAt: item.createdAt || new Date().toISOString(),
  }));
}
