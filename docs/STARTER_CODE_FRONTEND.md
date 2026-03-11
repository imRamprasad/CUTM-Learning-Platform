# Starter Code Examples - Frontend (React)

> Complete starter code for core React components

---

## Table of Contents

1. [Project Structure](#project-structure)
2. [API Client Setup](#api-client-setup)
3. [Custom Hooks](#custom-hooks)
4. [Context & Authentication](#context--authentication)
5. [Login Component](#login-component)
6. [Dashboard Component](#dashboard-component)
7. [Problem List Component](#problem-list-component)
8. [Problem Detail Component](#problem-detail-component)
9. [Code Editor Component](#code-editor-component)
10. [Common Components](#common-components)

---

## Project Structure

```
frontend/src/
├── components/
│   ├── auth/
│   │   ├── Login.js
│   │   ├── Register.js
│   │   └── ProtectedRoute.js
│   ├── dashboard/
│   │   ├── StudentDashboard.js
│   │   ├── ActivityOverview.js
│   │   └── RecentActivity.js
│   ├── problems/
│   │   ├── ProblemList.js
│   │   ├── ProblemDetail.js
│   │   └── CodeEditor.js
│   ├── common/
│   │   ├── Navbar.js
│   │   ├── LoadingSpinner.js
│   │   └── Modal.js
│   └── layout/
│       └── StudentLayout.js
├── context/
│   ├── AuthContext.js
│   └── AppContext.js
├── hooks/
│   ├── useAuth.js
│   ├── useApi.js
│   └── usePagination.js
├── services/
│   ├── api.js
│   ├── authService.js
│   ├── problemService.js
│   └── apiClient.js
├── utils/
│   ├── constants.js
│   ├── tokenManager.js
│   └── validators.js
├── styles/
│   ├── variables.css
│   ├── global.css
│   └── animations.css
├── App.js
└── index.js
```

---

## API Client Setup

**File**: `src/services/apiClient.js`

```javascript
import axios from 'axios';
import { getToken, refreshToken, removeToken } from '../utils/tokenManager';

const API_BASE_URL = process.env.REACT_APP_API_URL || 'http://localhost:8080/api/v1';

// Create axios instance
const apiClient = axios.create({
  baseURL: API_BASE_URL,
  timeout: 10000,
  headers: {
    'Content-Type': 'application/json',
  },
});

// Request interceptor - Add auth token
apiClient.interceptors.request.use(
  (config) => {
    const token = getToken();
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
  },
  (error) => Promise.reject(error)
);

// Response interceptor - Handle token expiry
apiClient.interceptors.response.use(
  (response) => response,
  async (error) => {
    const originalRequest = error.config;

    if (error.response?.status === 401 && !originalRequest._retry) {
      originalRequest._retry = true;
      
      try {
        const newToken = await refreshToken();
        originalRequest.headers.Authorization = `Bearer ${newToken}`;
        return apiClient(originalRequest);
      } catch (refreshError) {
        removeToken();
        window.location.href = '/login';
        return Promise.reject(refreshError);
      }
    }
    
    return Promise.reject(error);
  }
);

export default apiClient;
```

**File**: `src/services/api.js`

```javascript
import apiClient from './apiClient';

// Wrapper for consistent error handling
const handleResponse = (response) => {
  if (response.data.success === false) {
    throw new Error(response.data.message || 'An error occurred');
  }
  return response.data;
};

const handleError = (error) => {
  const message = error.response?.data?.message || error.message || 'An error occurred';
  throw new Error(message);
};

export const apiCall = async (method, url, data = null) => {
  try {
    const config = { method, url };
    if (data) config.data = data;
    
    const response = await apiClient(config);
    return handleResponse(response);
  } catch (error) {
    handleError(error);
  }
};
```

---

## Custom Hooks

**File**: `src/hooks/useAuth.js`

```javascript
import { useContext } from 'react';
import { AuthContext } from '../context/AuthContext';

export const useAuth = () => {
  const context = useContext(AuthContext);
  if (!context) {
    throw new Error('useAuth must be used within AuthProvider');
  }
  return context;
};
```

**File**: `src/hooks/useApi.js`

```javascript
import { useState, useCallback } from 'react';
import apiClient from '../services/apiClient';

export const useApi = () => {
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);

  const request = useCallback(async (config) => {
    try {
      setLoading(true);
      setError(null);
      const response = await apiClient(config);
      return response.data;
    } catch (err) {
      setError(err.response?.data?.message || err.message);
      throw err;
    } finally {
      setLoading(false);
    }
  }, []);

  return { request, loading, error };
};
```

**File**: `src/hooks/usePagination.js`

```javascript
import { useState, useCallback } from 'react';

export const usePagination = (initialPage = 1, initialPageSize = 20) => {
  const [currentPage, setCurrentPage] = useState(initialPage);
  const [pageSize, setPageSize] = useState(initialPageSize);
  const [totalPages, setTotalPages] = useState(0);
  const [totalElements, setTotalElements] = useState(0);

  const goToPage = useCallback((page) => {
    setCurrentPage(Math.max(1, Math.min(page, totalPages)));
  }, [totalPages]);

  const nextPage = useCallback(() => {
    goToPage(currentPage + 1);
  }, [currentPage, goToPage]);

  const prevPage = useCallback(() => {
    goToPage(currentPage - 1);
  }, [currentPage, goToPage]);

  const updatePageInfo = useCallback((data) => {
    setTotalPages(data.totalPages);
    setTotalElements(data.totalElements);
  }, []);

  return {
    currentPage,
    pageSize,
    setPageSize,
    goToPage,
    nextPage,
    prevPage,
    totalPages,
    totalElements,
    updatePageInfo,
  };
};
```

---

## Context & Authentication

**File**: `src/context/AuthContext.js`

```javascript
import React, { createContext, useState, useCallback, useEffect } from 'react';
import { setToken, removeToken, getToken } from '../utils/tokenManager';
import apiClient from '../services/apiClient';

export const AuthContext = createContext(null);

export const AuthProvider = ({ children }) => {
  const [user, setUser] = useState(null);
  const [loading, setLoading] = useState(true);
  const [isAuthenticated, setIsAuthenticated] = useState(false);

  // Initialize auth on mount
  useEffect(() => {
    const initAuth = async () => {
      const token = getToken();
      if (token) {
        try {
          // Validate token and get user info
          const response = await apiClient.get('/auth/validate-token');
          setUser(response.data.data);
          setIsAuthenticated(true);
        } catch (error) {
          removeToken();
          setIsAuthenticated(false);
        }
      }
      setLoading(false);
    };

    initAuth();
  }, []);

  const login = useCallback(async (email, password) => {
    try {
      const response = await apiClient.post('/auth/login', { email, password });
      const { token, data } = response.data;
      
      setToken(token);
      setUser(data);
      setIsAuthenticated(true);
      
      return data;
    } catch (error) {
      setIsAuthenticated(false);
      throw error;
    }
  }, []);

  const register = useCallback(async (userData) => {
    try {
      const response = await apiClient.post('/auth/register', userData);
      return response.data;
    } catch (error) {
      throw error;
    }
  }, []);

  const logout = useCallback(() => {
    removeToken();
    setUser(null);
    setIsAuthenticated(false);
  }, []);

  const value = {
    user,
    isAuthenticated,
    loading,
    login,
    register,
    logout,
  };

  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>;
};
```

---

## Login Component

**File**: `src/components/auth/Login.js`

```javascript
import React, { useState } from 'react';
import { useNavigate, Link } from 'react-router-dom';
import { useAuth } from '../../hooks/useAuth';
import LoadingSpinner from '../common/LoadingSpinner';
import '../styles/Auth.css';

const Login = () => {
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(false);
  const navigate = useNavigate();
  const { login } = useAuth();

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');
    setLoading(true);

    try {
      // Validate inputs
      if (!email || !password) {
        throw new Error('Please fill in all fields');
      }

      await login(email, password);
      navigate('/dashboard');
    } catch (err) {
      setError(err.response?.data?.message || err.message || 'Login failed');
    } finally {
      setLoading(false);
    }
  };

  if (loading) return <LoadingSpinner />;

  return (
    <div className="auth-container">
      <div className="auth-box">
        <h1>Login to CodeHub</h1>

        {error && <div className="error-message">{error}</div>}

        <form onSubmit={handleSubmit}>
          <div className="form-group">
            <label htmlFor="email">Email</label>
            <input
              id="email"
              type="email"
              value={email}
              onChange={(e) => setEmail(e.target.value)}
              placeholder="Enter your email"
              disabled={loading}
            />
          </div>

          <div className="form-group">
            <label htmlFor="password">Password</label>
            <input
              id="password"
              type="password"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              placeholder="Enter your password"
              disabled={loading}
            />
          </div>

          <button type="submit" className="btn btn-primary" disabled={loading}>
            {loading ? 'Logging in...' : 'Login'}
          </button>
        </form>

        <div className="auth-links">
          <Link to="/forgot-password">Forgot Password?</Link>
          <Link to="/register">Don't have an account? Register</Link>
        </div>
      </div>
    </div>
  );
};

export default Login;
```

---

## Dashboard Component

**File**: `src/components/dashboard/StudentDashboard.js`

```javascript
import React, { useState, useEffect } from 'react';
import { useAuth } from '../../hooks/useAuth';
import { useApi } from '../../hooks/useApi';
import ActivityOverview from './ActivityOverview';
import RecentActivity from './RecentActivity';
import LoadingSpinner from '../common/LoadingSpinner';
import '../styles/Dashboard.css';

const StudentDashboard = () => {
  const { user } = useAuth();
  const { request, loading } = useApi();
  const [stats, setStats] = useState(null);
  const [recentActivity, setRecentActivity] = useState([]);

  useEffect(() => {
    fetchDashboardData();
  }, []);

  const fetchDashboardData = async () => {
    try {
      // Fetch user stats
      const statsResponse = await request({
        method: 'GET',
        url: `/profile/${user.userId}/stats`,
      });
      setStats(statsResponse.data);

      // Fetch recent activity
      const activityResponse = await request({
        method: 'GET',
        url: `/activity?limit=10`,
      });
      setRecentActivity(activityResponse.data);
    } catch (error) {
      console.error('Failed to fetch dashboard data:', error);
    }
  };

  if (loading) return <LoadingSpinner />;

  return (
    <div className="dashboard-container">
      <div className="dashboard-header">
        <h1>Welcome back, {user?.username}!</h1>
        <p>Here's your learning progress</p>
      </div>

      <div className="dashboard-grid">
        <section className="dashboard-section">
          <ActivityOverview stats={stats} />
        </section>

        <section className="dashboard-section">
          <RecentActivity activities={recentActivity} />
        </section>
      </div>
    </div>
  );
};

export default StudentDashboard;
```

---

## Problem List Component

**File**: `src/components/problems/ProblemList.js`

```javascript
import React, { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import { useApi } from '../../hooks/useApi';
import { usePagination } from '../../hooks/usePagination';
import LoadingSpinner from '../common/LoadingSpinner';
import '../styles/Problems.css';

const ProblemList = () => {
  const [problems, setProblems] = useState([]);
  const [filters, setFilters] = useState({
    difficulty: 'all',
    category: 'all',
  });

  const { request, loading } = useApi();
  const pagination = usePagination();

  useEffect(() => {
    fetchProblems();
  }, [pagination.currentPage, filters]);

  const fetchProblems = async () => {
    try {
      const params = new URLSearchParams({
        page: pagination.currentPage - 1,
        size: pagination.pageSize,
        ...(filters.difficulty !== 'all' && { difficulty: filters.difficulty }),
        ...(filters.category !== 'all' && { category: filters.category }),
      });

      const response = await request({
        method: 'GET',
        url: `/problems?${params.toString()}`,
      });

      setProblems(response.data.content);
      pagination.updatePageInfo(response.data);
    } catch (error) {
      console.error('Failed to fetch problems:', error);
    }
  };

  const handleFilterChange = (e) => {
    const { name, value } = e.target;
    setFilters((prev) => ({
      ...prev,
      [name]: value,
    }));
    pagination.goToPage(1);
  };

  if (loading && problems.length === 0) return <LoadingSpinner />;

  return (
    <div className="problems-container">
      <div className="problems-header">
        <h1>Coding Problems</h1>
        <p>Solve problems and improve your skills</p>
      </div>

      <div className="filters">
        <div className="filter-group">
          <label htmlFor="difficulty">Difficulty</label>
          <select
            id="difficulty"
            name="difficulty"
            value={filters.difficulty}
            onChange={handleFilterChange}
          >
            <option value="all">All Levels</option>
            <option value="EASY">Easy</option>
            <option value="MEDIUM">Medium</option>
            <option value="HARD">Hard</option>
          </select>
        </div>

        <div className="filter-group">
          <label htmlFor="category">Category</label>
          <select
            id="category"
            name="category"
            value={filters.category}
            onChange={handleFilterChange}
          >
            <option value="all">All Categories</option>
            <option value="Arrays">Arrays</option>
            <option value="Strings">Strings</option>
            <option value="Trees">Trees</option>
            <option value="Graphs">Graphs</option>
          </select>
        </div>
      </div>

      <div className="problems-list">
        <table className="problems-table">
          <thead>
            <tr>
              <th>Title</th>
              <th>Difficulty</th>
              <th>Category</th>
              <th>Acceptance Rate</th>
              <th>Action</th>
            </tr>
          </thead>
          <tbody>
            {problems.map((problem) => (
              <tr key={problem.id}>
                <td>{problem.title}</td>
                <td>
                  <span className={`badge badge-${problem.difficulty.toLowerCase()}`}>
                    {problem.difficulty}
                  </span>
                </td>
                <td>{problem.category}</td>
                <td>{problem.acceptanceRate.toFixed(1)}%</td>
                <td>
                  <Link to={`/problems/${problem.id}`} className="btn btn-small">
                    Solve
                  </Link>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>

      <div className="pagination">
        <button
          onClick={pagination.prevPage}
          disabled={pagination.currentPage === 1}
        >
          Previous
        </button>
        <span>
          Page {pagination.currentPage} of {pagination.totalPages}
        </span>
        <button
          onClick={pagination.nextPage}
          disabled={pagination.currentPage === pagination.totalPages}
        >
          Next
        </button>
      </div>
    </div>
  );
};

export default ProblemList;
```

---

## Problem Detail Component

**File**: `src/components/problems/ProblemDetail.js`

```javascript
import React, { useState, useEffect } from 'react';
import { useParams } from 'react-router-dom';
import { useApi } from '../../hooks/useApi';
import CodeEditor from './CodeEditor';
import LoadingSpinner from '../common/LoadingSpinner';
import '../styles/ProblemDetail.css';

const ProblemDetail = () => {
  const { problemId } = useParams();
  const { request, loading } = useApi();
  const [problem, setProblem] = useState(null);
  const [selectedTab, setSelectedTab] = useState('description');

  useEffect(() => {
    fetchProblem();
  }, [problemId]);

  const fetchProblem = async () => {
    try {
      const response = await request({
        method: 'GET',
        url: `/problems/${problemId}`,
      });
      setProblem(response.data);
    } catch (error) {
      console.error('Failed to fetch problem:', error);
    }
  };

  if (loading) return <LoadingSpinner />;
  if (!problem) return <div>Problem not found</div>;

  return (
    <div className="problem-detail-container">
      <div className="problem-header">
        <h1>{problem.title}</h1>
        <span className={`badge badge-${problem.difficulty.toLowerCase()}`}>
          {problem.difficulty}
        </span>
      </div>

      <div className="problem-layout">
        <div className="problem-content">
          <div className="tabs">
            <button
              className={`tab ${selectedTab === 'description' ? 'active' : ''}`}
              onClick={() => setSelectedTab('description')}
            >
              Description
            </button>
            <button
              className={`tab ${selectedTab === 'solutions' ? 'active' : ''}`}
              onClick={() => setSelectedTab('solutions')}
            >
              Solutions
            </button>
            <button
              className={`tab ${selectedTab === 'submissions' ? 'active' : ''}`}
              onClick={() => setSelectedTab('submissions')}
            >
              Submissions
            </button>
          </div>

          <div className="tab-content">
            {selectedTab === 'description' && (
              <div className="description">
                <h2>Description</h2>
                <p>{problem.description}</p>

                <h3>Examples</h3>
                {problem.examples?.map((example, idx) => (
                  <div key={idx} className="example">
                    <p>
                      <strong>Input:</strong> {example.input}
                    </p>
                    <p>
                      <strong>Output:</strong> {example.output}
                    </p>
                    {example.explanation && (
                      <p>
                        <strong>Explanation:</strong> {example.explanation}
                      </p>
                    )}
                  </div>
                ))}

                <h3>Constraints</h3>
                <p>{problem.problemStatement?.constraints}</p>

                {problem.hints?.length > 0 && (
                  <>
                    <h3>Hints</h3>
                    <ul>
                      {problem.hints.map((hint, idx) => (
                        <li key={idx}>{hint}</li>
                      ))}
                    </ul>
                  </>
                )}
              </div>
            )}

            {selectedTab === 'solutions' && (
              <div className="solutions">
                {problem.editorials?.map((editorial, idx) => (
                  <div key={idx} className="solution">
                    <h3>{editorial.approach}</h3>
                    <p>Language: {editorial.language}</p>
                    <p>Time: {editorial.timeComplexity}</p>
                    <p>Space: {editorial.spaceComplexity}</p>
                    <pre>{editorial.code}</pre>
                    <p>{editorial.explanation}</p>
                  </div>
                ))}
              </div>
            )}
          </div>
        </div>

        <div className="code-editor-section">
          <CodeEditor problemId={problemId} />
        </div>
      </div>
    </div>
  );
};

export default ProblemDetail;
```

---

## Code Editor Component

**File**: `src/components/problems/CodeEditor.js`

```javascript
import React, { useState } from 'react';
import { useApi } from '../../hooks/useApi';
import '../styles/CodeEditor.css';

const CodeEditor = ({ problemId }) => {
  const [language, setLanguage] = useState('java');
  const [code, setCode] = useState('');
  const [submitting, setSubmitting] = useState(false);
  const [result, setResult] = useState(null);
  const { request } = useApi();

  const handleRunCode = async () => {
    setSubmitting(true);
    try {
      const response = await request({
        method: 'POST',
        url: `/submissions/${problemId}/run`,
        data: { code, language },
      });
      setResult(response.data);
    } catch (error) {
      setResult({ status: 'ERROR', message: error.message });
    } finally {
      setSubmitting(false);
    }
  };

  const handleSubmit = async () => {
    setSubmitting(true);
    try {
      const response = await request({
        method: 'POST',
        url: '/submissions',
        data: { code, language, problemId },
      });
      setResult(response.data);
    } catch (error) {
      setResult({ status: 'ERROR', message: error.message });
    } finally {
      setSubmitting(false);
    }
  };

  return (
    <div className="code-editor">
      <div className="editor-header">
        <select value={language} onChange={(e) => setLanguage(e.target.value)}>
          <option value="java">Java</option>
          <option value="python">Python</option>
          <option value="cpp">C++</option>
          <option value="javascript">JavaScript</option>
        </select>

        <div className="editor-buttons">
          <button onClick={handleRunCode} disabled={submitting} className="btn-secondary">
            {submitting ? 'Running...' : 'Run Code'}
          </button>
          <button onClick={handleSubmit} disabled={submitting} className="btn-primary">
            {submitting ? 'Submitting...' : 'Submit'}
          </button>
        </div>
      </div>

      <textarea
        value={code}
        onChange={(e) => setCode(e.target.value)}
        placeholder="Write your code here..."
        className="editor-textarea"
      />

      {result && (
        <div className={`result ${result.status.toLowerCase()}`}>
          <h4>{result.status}</h4>
          <p>{result.message}</p>
          {result.testResults && (
            <div className="test-results">
              <p>Passed: {result.testResults.passed} / {result.testResults.total}</p>
            </div>
          )}
        </div>
      )}
    </div>
  );
};

export default CodeEditor;
```

---

## Common Components

**File**: `src/components/common/LoadingSpinner.js`

```javascript
import React from 'react';
import '../styles/LoadingSpinner.css';

const LoadingSpinner = () => {
  return (
    <div className="loading-spinner-container">
      <div className="loading-spinner">
        <div className="spinner"></div>
        <p>Loading...</p>
      </div>
    </div>
  );
};

export default LoadingSpinner;
```

**File**: `src/components/common/Modal.js`

```javascript
import React from 'react';
import '../styles/Modal.css';

const Modal = ({ isOpen, title, children, onClose, footer }) => {
  if (!isOpen) return null;

  return (
    <div className="modal-overlay" onClick={onClose}>
      <div className="modal-content" onClick={(e) => e.stopPropagation()}>
        <div className="modal-header">
          <h2>{title}</h2>
          <button className="modal-close" onClick={onClose}>
            &times;
          </button>
        </div>

        <div className="modal-body">{children}</div>

        {footer && <div className="modal-footer">{footer}</div>}
      </div>
    </div>
  );
};

export default Modal;
```

**File**: `src/components/common/Navbar.js`

```javascript
import React from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { useAuth } from '../../hooks/useAuth';
import '../styles/Navbar.css';

const Navbar = () => {
  const { user, logout } = useAuth();
  const navigate = useNavigate();

  const handleLogout = () => {
    logout();
    navigate('/login');
  };

  return (
    <nav className="navbar">
      <div className="navbar-brand">
        <Link to="/dashboard">CodeHub</Link>
      </div>

      <div className="navbar-menu">
        <Link to="/problems">Problems</Link>
        <Link to="/contests">Contests</Link>
        <Link to="/courses">Courses</Link>
        <Link to="/leaderboard">Leaderboard</Link>
      </div>

      <div className="navbar-user">
        <span>{user?.username}</span>
        <Link to="/profile" className="btn-small">Profile</Link>
        <button onClick={handleLogout} className="btn-small">Logout</button>
      </div>
    </nav>
  );
};

export default Navbar;
```

---

## Protected Route Component

**File**: `src/components/auth/ProtectedRoute.js`

```javascript
import React from 'react';
import { Navigate } from 'react-router-dom';
import { useAuth } from '../../hooks/useAuth';
import LoadingSpinner from '../common/LoadingSpinner';

const ProtectedRoute = ({ children }) => {
  const { isAuthenticated, loading } = useAuth();

  if (loading) return <LoadingSpinner />;

  if (!isAuthenticated) {
    return <Navigate to="/login" replace />;
  }

  return children;
};

export default ProtectedRoute;
```

---

## CSS Examples

**File**: `src/styles/global.css`

```css
* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
}

:root {
  --primary-color: #0066cc;
  --secondary-color: #ff6b6b;
  --success-color: #28a745;
  --warning-color: #ffc107;
  --danger-color: #dc3545;
  --dark-bg: #1a1a1a;
  --light-bg: #f8f9fa;
  --text-primary: #333;
  --text-secondary: #666;
  --border-color: #ddd;
}

body {
  font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', 'Roboto', 'Oxygen',
    'Ubuntu', 'Cantarell', 'Fira Sans', 'Droid Sans', 'Helvetica Neue',
    sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  background-color: var(--light-bg);
  color: var(--text-primary);
}

/* Typography */
h1, h2, h3, h4, h5, h6 {
  margin-bottom: 1rem;
  font-weight: 600;
}

h1 { font-size: 2rem; }
h2 { font-size: 1.75rem; }
h3 { font-size: 1.5rem; }

/* Buttons */
.btn {
  padding: 0.75rem 1.5rem;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-size: 1rem;
  transition: all 0.3s ease;
}

.btn-primary {
  background-color: var(--primary-color);
  color: white;
}

.btn-primary:hover {
  background-color: #0052a3;
}

.btn-secondary {
  background-color: #6c757d;
  color: white;
}

.btn-small {
  padding: 0.5rem 1rem;
  font-size: 0.875rem;
}

/* Form Elements */
input, textarea, select {
  width: 100%;
  padding: 0.75rem;
  margin-bottom: 1rem;
  border: 1px solid var(--border-color);
  border-radius: 4px;
  font-family: inherit;
  font-size: 1rem;
}

input:focus, textarea:focus, select:focus {
  outline: none;
  border-color: var(--primary-color);
  box-shadow: 0 0 0 3px rgba(0, 102, 204, 0.1);
}

/* Badges */
.badge {
  display: inline-block;
  padding: 0.25rem 0.75rem;
  border-radius: 20px;
  font-size: 0.875rem;
  font-weight: 500;
}

.badge-easy { background-color: #d4edda; color: #155724; }
.badge-medium { background-color: #fff3cd; color: #856404; }
.badge-hard { background-color: #f8d7da; color: #721c24; }

/* Tables */
table {
  width: 100%;
  border-collapse: collapse;
  margin-bottom: 1rem;
}

th, td {
  padding: 1rem;
  text-align: left;
  border-bottom: 1px solid var(--border-color);
}

th {
  background-color: var(--light-bg);
  font-weight: 600;
}

/* Responsive */
@media (max-width: 768px) {
  h1 { font-size: 1.5rem; }
  h2 { font-size: 1.25rem; }
  
  .btn {
    padding: 0.5rem 1rem;
  }
}
```

---

## Next Steps

1. **Setup React Router** for navigation
2. **Configure environment variables** in `.env`
3. **Setup CSS modules** or styled-components
4. **Add form validation** library (Formik, React Hook Form)
5. **Implement code editor** (Monaco Editor or CodeMirror)
6. **Add WebSocket support** for real-time notifications
7. **Setup Redux** for global state management (if needed)
8. **Add unit tests** with Jest and React Testing Library
9. **Optimize performance** with lazy loading and memoization
10. **Setup PWA** support for mobile experience

