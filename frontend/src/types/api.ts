export interface BackendUser {
  id: string;
  email: string;
  username: string;
  firstName?: string;
  lastName?: string;
  profilePictureUrl?: string;
  role: 'STUDENT' | 'ADMIN' | string;
  problemsSolved?: number;
  rank?: number;
  totalPoints?: number;
}

export interface AuthResponse {
  token: string;
  refreshToken?: string;
  user: BackendUser;
}

export interface RegisterRequest {
  email: string;
  username: string;
  password: string;
  firstName: string;
  lastName: string;
}

export interface RegisterAdminRequest extends RegisterRequest {
  adminCode: string;
}
