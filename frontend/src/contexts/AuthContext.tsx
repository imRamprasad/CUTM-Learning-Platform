import React, { createContext, useContext, useState, ReactNode } from 'react';
import api from '../services/api';
import type { AuthResponse, BackendUser, RegisterAdminRequest, RegisterRequest } from '../types/api';


export type Role = 'STUDENT' | 'ADMIN';

export interface User {
  id: string;
  name: string;
  email: string;
  role: Role;
  avatar?: string;
}

interface AuthContextType {
  user: User | null;
  login: (role: Role) => void;
  loginStudent: (email: string, password: string) => Promise<{ success: boolean; message?: string }>;
  registerStudent: (payload: RegisterRequest) => Promise<{ success: boolean; message?: string }>;
  registerAdmin: (payload: RegisterAdminRequest) => Promise<{ success: boolean; message?: string }>;
  loginAdmin: (email: string, password: string) => Promise<{ success: boolean; message?: string }>;

  logout: () => void;
}

const AuthContext = createContext<AuthContextType | undefined>(undefined);

const AUTH_STORAGE_KEY = 'codelearn.auth.user';
const JWT_STORAGE_KEY = 'jwt_token';

function safeGetItem(key: string): string | null {
  try {
    return window.localStorage.getItem(key);
  } catch {
    return null;
  }
}

function safeSetItem(key: string, value: string): void {
  try {
    window.localStorage.setItem(key, value);
  } catch {
    // Ignore storage write failures (privacy mode, disabled storage, quota exceeded).
  }
}

function safeRemoveItem(key: string): void {
  try {
    window.localStorage.removeItem(key);
  } catch {
    // Ignore storage remove failures.
  }
}

function readPersistedUser(): User | null {
  if (typeof window === 'undefined') {
    return null;
  }
  const raw = safeGetItem(AUTH_STORAGE_KEY);
  if (!raw) {
    return null;
  }
  try {
    return JSON.parse(raw) as User;
  } catch {
    safeRemoveItem(AUTH_STORAGE_KEY);
    return null;
  }
}

function persistUser(user: User | null): void {
  if (typeof window === 'undefined') {
    return;
  }
  if (!user) {
    safeRemoveItem(AUTH_STORAGE_KEY);
    return;
  }
  safeSetItem(AUTH_STORAGE_KEY, JSON.stringify(user));
}

function mapBackendUser(backendUser: BackendUser): User {
  const fullName = [backendUser.firstName, backendUser.lastName]
    .filter(Boolean)
    .join(' ')
    .trim();

  return {
    id: backendUser.id,
    name: fullName || backendUser.username || backendUser.email,
    email: backendUser.email,
    role: backendUser.role === 'ADMIN' ? 'ADMIN' : 'STUDENT',
    avatar: backendUser.profilePictureUrl,
  };
}

function getApiErrorMessage(error: any, fallback: string): string {
  const responseData = error?.response?.data;

  if (typeof responseData?.message === 'string' && responseData.message.trim()) {
    return responseData.message;
  }

  if (typeof responseData === 'string' && responseData.trim()) {
    return responseData;
  }

  if (!error?.response) {
    return 'Cannot reach backend server. Please ensure backend is running on http://localhost:8080.';
  }

  return fallback;
}

export function AuthProvider({ children }: { children: ReactNode }) {
  const [user, setUser] = useState<User | null>(() => readPersistedUser());

  const login = (role: Role) => {
    if (role === 'STUDENT') {
      const studentUser: User = {
        id: '1',
        name: 'Alex Student',
        email: 'alex@example.com',
        role: 'STUDENT',
        avatar: 'https://api.dicebear.com/7.x/avataaars/svg?seed=Alex',
      };
      setUser(studentUser);
      persistUser(studentUser);
    } else {
      // Enforce dedicated credential flow for admin authentication.
      persistUser(null);
      setUser(null);
    }
  };

  const loginStudent = async (email: string, password: string) => {
    try {
      const response = await api.post<AuthResponse>('/auth/login', { email, password });
      const { token, user: backendUser } = response.data;
      const frontendUser = mapBackendUser(backendUser);

      localStorage.setItem(JWT_STORAGE_KEY, token);
      setUser(frontendUser);
      persistUser(frontendUser);

      return { success: true };
    } catch (error: any) {
      return { success: false, message: getApiErrorMessage(error, 'Login failed') };
    }
  };

  const registerStudent = async (payload: RegisterRequest) => {
    try {
      const response = await api.post<AuthResponse>('/auth/register', payload);
      const { token, user: backendUser } = response.data;
      const frontendUser = mapBackendUser(backendUser);

      localStorage.setItem(JWT_STORAGE_KEY, token);
      setUser(frontendUser);
      persistUser(frontendUser);

      return { success: true };
    } catch (error: any) {
      return { success: false, message: getApiErrorMessage(error, 'Registration failed') };
    }
  };

  const registerAdmin = async (payload: RegisterAdminRequest) => {
    try {
      const response = await api.post<AuthResponse>('/auth/register-admin', payload);
      const { token, user: backendUser } = response.data;
      const frontendUser = mapBackendUser(backendUser);

      if (frontendUser.role !== 'ADMIN') {
        return { success: false, message: 'Admin registration did not return an admin account' };
      }

      localStorage.setItem(JWT_STORAGE_KEY, token);
      setUser(frontendUser);
      persistUser(frontendUser);

      return { success: true };
    } catch (error: any) {
      return { success: false, message: getApiErrorMessage(error, 'Admin registration failed') };
    }
  };

  const loginAdmin = async (email: string, password: string) => {
    try {
      const response = await api.post<AuthResponse>('/auth/login', { email, password });
      const { token, user: backendUser } = response.data;
      const frontendUser = mapBackendUser(backendUser);

      if (frontendUser.role !== 'ADMIN') {
        return { success: false, message: 'This account does not have admin access' };
      }

      localStorage.setItem(JWT_STORAGE_KEY, token);
      setUser(frontendUser);
      persistUser(frontendUser);
      return { success: true };
    } catch (error: any) {
      return { success: false, message: getApiErrorMessage(error, 'Login failed') };
    }
  };


  const logout = () => {
    setUser(null);
    persistUser(null);
    localStorage.removeItem(JWT_STORAGE_KEY);
  };

  return (
    <AuthContext.Provider value={{ user, login, loginStudent, registerStudent, registerAdmin, loginAdmin, logout }}>
      {children}
    </AuthContext.Provider>
  );
}

export function useAuth() {
  const context = useContext(AuthContext);
  if (context === undefined) {
    throw new Error('useAuth must be used within an AuthProvider');
  }
  return context;
}
