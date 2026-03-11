# CodeHub Platform - Complete Architecture Guide

> A comprehensive guide for building a scalable coding learning platform with React + Spring Boot + MongoDB

**Table of Contents:**
1. [Project Structure Overview](#project-structure-overview)
2. [Backend Package Structure](#backend-package-structure)
3. [MongoDB Collections & Schema](#mongodb-collections--schema)
4. [REST API Endpoints](#rest-api-endpoints)
5. [Frontend Component Structure](#frontend-component-structure)
6. [Database Design Patterns](#database-design-patterns)
7. [Team Workflow & Best Practices](#team-workflow--best-practices)

---

## Project Structure Overview

```
CUTM-Learning-Platform/
├── backend/
│   ├── platform/
│   │   ├── src/
│   │   │   ├── main/
│   │   │   │   ├── java/com/cutm/platform/
│   │   │   │   │   ├── PlatformApplication.java
│   │   │   │   │   ├── config/
│   │   │   │   │   │   ├── SecurityConfig.java
│   │   │   │   │   │   ├── MongoConfig.java
│   │   │   │   │   │   ├── CorsConfig.java
│   │   │   │   │   │   └── JwtConfig.java
│   │   │   │   │   ├── auth/
│   │   │   │   │   │   ├── controller/
│   │   │   │   │   │   │   └── AuthController.java
│   │   │   │   │   │   ├── service/
│   │   │   │   │   │   │   ├── AuthService.java
│   │   │   │   │   │   │   └── EmailService.java
│   │   │   │   │   │   ├── repository/
│   │   │   │   │   │   │   └── UserRepository.java
│   │   │   │   │   │   ├── model/
│   │   │   │   │   │   │   ├── User.java
│   │   │   │   │   │   │   └── LoginRequest.java
│   │   │   │   │   │   ├── util/
│   │   │   │   │   │   │   └── JwtUtil.java
│   │   │   │   │   │   └── filter/
│   │   │   │   │   │       └── JwtAuthenticationFilter.java
│   │   │   │   │   ├── profile/
│   │   │   │   │   │   ├── controller/
│   │   │   │   │   │   │   └── ProfileController.java
│   │   │   │   │   │   ├── service/
│   │   │   │   │   │   │   └── ProfileService.java
│   │   │   │   │   │   ├── repository/
│   │   │   │   │   │   │   └── ProfileRepository.java
│   │   │   │   │   │   └── model/
│   │   │   │   │   │       └── UserProfile.java
│   │   │   │   │   ├── problems/
│   │   │   │   │   │   ├── controller/
│   │   │   │   │   │   │   └── ProblemController.java
│   │   │   │   │   │   ├── service/
│   │   │   │   │   │   │   └── ProblemService.java
│   │   │   │   │   │   ├── repository/
│   │   │   │   │   │   │   └── ProblemRepository.java
│   │   │   │   │   │   └── model/
│   │   │   │   │   │       ├── Problem.java
│   │   │   │   │   │       └── TestCase.java
│   │   │   │   │   ├── submissions/
│   │   │   │   │   │   ├── controller/
│   │   │   │   │   │   │   └── SubmissionController.java
│   │   │   │   │   │   ├── service/
│   │   │   │   │   │   │   ├── SubmissionService.java
│   │   │   │   │   │   │   └── CodeExecutor.java
│   │   │   │   │   │   ├── repository/
│   │   │   │   │   │   │   └── SubmissionRepository.java
│   │   │   │   │   │   └── model/
│   │   │   │   │   │       └── Submission.java
│   │   │   │   │   ├── contests/
│   │   │   │   │   │   ├── controller/
│   │   │   │   │   │   │   └── ContestController.java
│   │   │   │   │   │   ├── service/
│   │   │   │   │   │   │   └── ContestService.java
│   │   │   │   │   │   ├── repository/
│   │   │   │   │   │   │   └── ContestRepository.java
│   │   │   │   │   │   └── model/
│   │   │   │   │   │       ├── Contest.java
│   │   │   │   │   │       └── ContestParticipation.java
│   │   │   │   │   ├── leaderboard/
│   │   │   │   │   │   ├── controller/
│   │   │   │   │   │   │   └── LeaderboardController.java
│   │   │   │   │   │   ├── service/
│   │   │   │   │   │   │   └── LeaderboardService.java
│   │   │   │   │   │   ├── repository/
│   │   │   │   │   │   │   └── LeaderboardRepository.java
│   │   │   │   │   │   └── model/
│   │   │   │   │   │       └── LeaderboardEntry.java
│   │   │   │   │   ├── courses/
│   │   │   │   │   │   ├── controller/
│   │   │   │   │   │   │   └── CourseController.java
│   │   │   │   │   │   ├── service/
│   │   │   │   │   │   │   └── CourseService.java
│   │   │   │   │   │   ├── repository/
│   │   │   │   │   │   │   └── CourseRepository.java
│   │   │   │   │   │   └── model/
│   │   │   │   │   │       ├── Course.java
│   │   │   │   │   │       └── Module.java
│   │   │   │   │   ├── discussions/
│   │   │   │   │   │   ├── controller/
│   │   │   │   │   │   │   └── DiscussionController.java
│   │   │   │   │   │   ├── service/
│   │   │   │   │   │   │   └── DiscussionService.java
│   │   │   │   │   │   ├── repository/
│   │   │   │   │   │   │   └── DiscussionRepository.java
│   │   │   │   │   │   └── model/
│   │   │   │   │   │       ├── Discussion.java
│   │   │   │   │   │       └── Comment.java
│   │   │   │   │   ├── notifications/
│   │   │   │   │   │   ├── controller/
│   │   │   │   │   │   │   └── NotificationController.java
│   │   │   │   │   │   ├── service/
│   │   │   │   │   │   │   └── NotificationService.java
│   │   │   │   │   │   ├── repository/
│   │   │   │   │   │   │   └── NotificationRepository.java
│   │   │   │   │   │   └── model/
│   │   │   │   │   │       └── Notification.java
│   │   │   │   │   ├── admin/
│   │   │   │   │   │   ├── controller/
│   │   │   │   │   │   │   ├── AdminUserController.java
│   │   │   │   │   │   │   ├── AdminProblemController.java
│   │   │   │   │   │   │   ├── AdminContestController.java
│   │   │   │   │   │   │   └── AdminDashboardController.java
│   │   │   │   │   │   ├── service/
│   │   │   │   │   │   │   ├── AdminService.java
│   │   │   │   │   │   │   └── AdminDashboardService.java
│   │   │   │   │   │   └── security/
│   │   │   │   │   │       └── AdminAuthority.java
│   │   │   │   │   ├── common/
│   │   │   │   │   │   ├── exception/
│   │   │   │   │   │   │   ├── GlobalExceptionHandler.java
│   │   │   │   │   │   │   └── CustomExceptions.java
│   │   │   │   │   │   ├── dto/
│   │   │   │   │   │   │   ├── ApiResponse.java
│   │   │   │   │   │   │   ├── PagedResponse.java
│   │   │   │   │   │   │   └── ErrorResponse.java
│   │   │   │   │   │   └── util/
│   │   │   │   │   │       └── Constants.java
│   │   │   │   │   └── shared/
│   │   │   │   │       └── BaseEntity.java
│   │   │   │   └── resources/
│   │   │   │       ├── application.properties
│   │   │   │       ├── application-dev.properties
│   │   │   │       ├── application-prod.properties
│   │   │   │       └── templates/
│   │   │   └── test/
│   │   │       └── java/com/cutm/platform/
│   │   ├── pom.xml
│   │   ├── HELP.md
│   │   └── mvnw
│   ├── .gitignore
│   └── README.md
├── frontend/
│   ├── src/
│   │   ├── components/
│   │   │   ├── auth/
│   │   │   │   ├── Login.js
│   │   │   │   ├── Register.js
│   │   │   │   ├── ForgotPassword.js
│   │   │   │   └── EmailVerification.js
│   │   │   ├── dashboard/
│   │   │   │   ├── StudentDashboard.js
│   │   │   │   ├── ActivityOverview.js
│   │   │   │   ├── ProgressCard.js
│   │   │   │   └── RecentActivity.js
│   │   │   ├── profile/
│   │   │   │   ├── UserProfile.js
│   │   │   │   ├── ProfileEdit.js
│   │   │   │   ├── Achievements.js
│   │   │   │   └── Statistics.js
│   │   │   ├── problems/
│   │   │   │   ├── ProblemList.js
│   │   │   │   ├── ProblemDetail.js
│   │   │   │   ├── CodeEditor.js
│   │   │   │   ├── TestCaseRunner.js
│   │   │   │   └── SubmissionHistory.js
│   │   │   ├── courses/
│   │   │   │   ├── CourseList.js
│   │   │   │   ├── CourseDetail.js
│   │   │   │   ├── ModuleView.js
│   │   │   │   └── VideoPlayer.js
│   │   │   ├── contests/
│   │   │   │   ├── ContestList.js
│   │   │   │   ├── ContestDetail.js
│   │   │   │   ├── ContestEditor.js
│   │   │   │   └── ContestLeaderboard.js
│   │   │   ├── leaderboard/
│   │   │   │   ├── GlobalLeaderboard.js
│   │   │   │   ├── WeeklyLeaderboard.js
│   │   │   │   └── ContestLeaderboard.js
│   │   │   ├── discussions/
│   │   │   │   ├── DiscussionList.js
│   │   │   │   ├── DiscussionThread.js
│   │   │   │   └── CreateQuestion.js
│   │   │   ├── admin/
│   │   │   │   ├── AdminDashboard.js
│   │   │   │   ├── UserManagement.js
│   │   │   │   ├── ProblemManagement.js
│   │   │   │   ├── ContestManagement.js
│   │   │   │   ├── DiscussionModeration.js
│   │   │   │   └── AnalyticsDashboard.js
│   │   │   ├── common/
│   │   │   │   ├── Navbar.js
│   │   │   │   ├── Sidebar.js
│   │   │   │   ├── Footer.js
│   │   │   │   ├── Loader.js
│   │   │   │   ├── LoadingSpinner.js
│   │   │   │   ├── Modal.js
│   │   │   │   └── Toast.js
│   │   │   └── layout/
│   │   │       ├── StudentLayout.js
│   │   │       └── AdminLayout.js
│   │   ├── pages/
│   │   │   ├── HomePage.js
│   │   │   ├── NotFoundPage.js
│   │   │   └── UnauthorizedPage.js
│   │   ├── hooks/
│   │   │   ├── useAuth.js
│   │   │   ├── useApi.js
│   │   │   ├── usePagination.js
│   │   │   └── useLocalStorage.js
│   │   ├── services/
│   │   │   ├── api.js
│   │   │   ├── authService.js
│   │   │   ├── problemService.js
│   │   │   ├── submissionService.js
│   │   │   ├── courseService.js
│   │   │   ├── contestService.js
│   │   │   └── leaderboardService.js
│   │   ├── context/
│   │   │   ├── AuthContext.js
│   │   │   ├── NotificationContext.js
│   │   │   └── AppThemeContext.js
│   │   ├── utils/
│   │   │   ├── apiClient.js
│   │   │   ├── tokenManager.js
│   │   │   ├── validators.js
│   │   │   ├── formatters.js
│   │   │   └── constants.js
│   │   ├── styles/
│   │   │   ├── variables.css
│   │   │   ├── global.css
│   │   │   └── animations.css
│   │   ├── App.js
│   │   ├── App.css
│   │   └── index.js
│   ├── public/
│   ├── package.json
│   └── README.md
├── docs/
│   ├── API_DOCUMENTATION.md
│   ├── DATABASE_SCHEMA.md
│   ├── DEPLOYMENT.md
│   ├── TEAM_WORKFLOW.md
│   └── SETUP_GUIDE.md
├── scripts/
│   ├── setup.ps1
│   ├── run.ps1
│   └── deploy.sh
├── ARCHITECTURE.md (this file)
├── CONTRIBUTING.md
├── README.md
└── .github/
    ├── ISSUE_TEMPLATE/
    │   ├── bug_report.md
    │   └── feature_request.md
    └── pull_request_template.md
```

---

## Backend Package Structure

### 3-Layer Architecture (Controller → Service → Repository)

```
com.cutm.platform
├── PlatformApplication.java            [Entry point]
├── config/                             [Configuration]
│   ├── SecurityConfig.java             [Spring Security & JWT]
│   ├── MongoConfig.java                [MongoDB Configuration]
│   ├── CorsConfig.java                 [CORS settings]
│   └── JwtConfig.java                  [JWT Configuration]
├── auth/                               [Authentication Module]
│   ├── controller/AuthController.java
│   ├── service/AuthService.java
│   ├── repository/UserRepository.java
│   ├── model/User.java
│   ├── util/JwtUtil.java
│   ├── filter/JwtAuthenticationFilter.java
│   └── dto/LoginRequest.java
├── [feature-modules]/
│   ├── controller/
│   ├── service/
│   ├── repository/
│   ├── model/
│   └── dto/
├── admin/                              [Admin features]
│   ├── controller/
│   ├── service/
│   └── security/AdminAuthority.java
├── common/                             [Shared utilities]
│   ├── exception/GlobalExceptionHandler.java
│   ├── dto/ApiResponse.java
│   └── util/Constants.java
└── shared/                             [Base classes]
    └── BaseEntity.java
```

### Key Design Principles:

1. **Separation of Concerns**: Each feature has its own package
2. **Single Responsibility**: Controller → Service → Repository → Model
3. **DTO Pattern**: Separate models from DTOs
4. **Exception Handling**: Centralized exception handling
5. **Constants**: Centralized constants file

---

## MongoDB Collections & Schema

### 1. Users Collection

```json
{
  "_id": ObjectId,
  "email": "user@example.com",
  "username": "johndoe",
  "password": "hashed_password",
  "firstName": "John",
  "lastName": "Doe",
  "profileImage": "url",
  "bio": "I love coding",
  "role": "STUDENT" | "ADMIN",
  "status": "ACTIVE" | "INACTIVE",
  "emailVerified": true,
  "socialLinks": {
    "github": "url",
    "linkedin": "url"
  },
  "createdAt": ISODate,
  "updatedAt": ISODate,
  "lastLogin": ISODate
}
```

### 2. UserProfiles Collection

```json
{
  "_id": ObjectId,
  "userId": ObjectId,
  "totalProblems": 150,
  "solvedProblems": 45,
  "attemptedProblems": 120,
  "contestParticipations": 5,
  "ranking": 250,
  "points": 5000,
  "badges": ["python-expert", "problem-solver-50"],
  "achievementDetails": [
    {
      "name": "First Problem",
      "description": "Solved first problem",
      "earnedAt": ISODate,
      "badge": "url"
    }
  ],
  "preferences": {
    "theme": "dark" | "light",
    "language": "Java" | "Python" | "C++",
    "notifications": true
  },
  "createdAt": ISODate,
  "updatedAt": ISODate
}
```

### 3. Problems Collection

```json
{
  "_id": ObjectId,
  "title": "Two Sum",
  "description": "Given an array of integers...",
  "difficulty": "EASY" | "MEDIUM" | "HARD",
  "category": "Arrays" | "Strings" | "Trees",
  "platforms": ["LeetCode", "GeeksforGeeks"],
  "sampleInput": "nums = [2, 7, 11, 15], target = 9",
  "sampleOutput": "[0, 1]",
  "explanation": "The nums[0] and nums[1] are the answer",
  "acceptanceRate": 42.5,
  "totalSubmissions": 5000,
  "successfulSubmissions": 2125,
  "testCases": [
    {
      "input": "nums = [2, 7, 11, 15], target = 9",
      "output": "[0, 1]",
      "isHidden": false
    },
    {
      "input": "nums = [3, 3], target = 6",
      "output": "[0, 1]",
      "isHidden": true
    }
  ],
  "problemConstraints": "1 <= nums.length <= 10^4",
  "hints": ["Use HashMap for O(n) solution"],
  "editorials": [
    {
      "language": "Java",
      "code": "...",
      "explanation": "..."
    }
  ],
  "tags": ["array", "hash-table", "medium"],
  "relatedProblems": [ObjectId, ObjectId],
  "createdBy": ObjectId,
  "createdAt": ISODate,
  "updatedAt": ISODate,
  "isPublished": true
}
```

### 4. Submissions Collection

```json
{
  "_id": ObjectId,
  "userId": ObjectId,
  "problemId": ObjectId,
  "code": "...",
  "language": "java" | "python" | "cpp",
  "status": "ACCEPTED" | "WRONG_ANSWER" | "TIME_LIMIT_EXCEEDED" | "RUNTIME_ERROR",
  "executionTime": 45,
  "memory": 56,
  "submittedAt": ISODate,
  "testResults": [
    {
      "testCaseId": ObjectId,
      "passed": true,
      "executionTime": 10,
      "memory": 20,
      "output": "[0, 1]",
      "expectedOutput": "[0, 1]"
    }
  ],
  "errorMessage": null | "Runtime Error: Array index out of bounds",
  "contestId": null | ObjectId,
  "score": 100 | 50 | 0
}
```

### 5. Contests Collection

```json
{
  "_id": ObjectId,
  "title": "Weekly Contest 1",
  "description": "Solve 4 problems in 2 hours",
  "startTime": ISODate,
  "endTime": ISODate,
  "duration": 120,
  "problems": [ObjectId, ObjectId, ObjectId, ObjectId],
  "participants": [ObjectId, ...],
  "status": "UPCOMING" | "ONGOING" | "FINISHED",
  "isPublished": true,
  "createdBy": ObjectId,
  "leaderboard": [
    {
      "userId": ObjectId,
      "rank": 1,
      "score": 400,
      "time": 45
    }
  ],
  "createdAt": ISODate,
  "updatedAt": ISODate
}
```

### 6. Courses Collection

```json
{
  "_id": ObjectId,
  "title": "Complete Java Programming",
  "description": "Learn Java from basics to advanced",
  "instructor": ObjectId,
  "difficulty": "BEGINNER" | "INTERMEDIATE" | "ADVANCED",
  "category": "Programming Languages",
  "thumbnail": "url",
  "enrollments": [ObjectId, ...],
  "modules": [
    {
      "_id": ObjectId,
      "title": "Java Basics",
      "description": "Variables, data types, operators",
      "lessons": [
        {
          "_id": ObjectId,
          "title": "Variables and Data Types",
          "videoUrl": "url",
          "duration": 15,
          "resources": ["pdf", "code"]
        }
      ],
      "order": 1
    }
  ],
  "rating": 4.8,
  "reviews": 500,
  "createdAt": ISODate,
  "updatedAt": ISODate
}
```

### 7. Discussions Collection

```json
{
  "_id": ObjectId,
  "title": "How to optimize two sum solution?",
  "description": "I'm getting TLE with my current approach",
  "author": ObjectId,
  "problemId": null | ObjectId,
  "courseId": null | ObjectId,
  "tags": ["optimization", "two-sum"],
  "upvotes": 25,
  "downvotes": 2,
  "viewCount": 150,
  "comments": [
    {
      "_id": ObjectId,
      "author": ObjectId,
      "content": "Try using HashMap...",
      "upvotes": 10,
      "downvotes": 1,
      "createdAt": ISODate
    }
  ],
  "isPinned": false,
  "status": "OPEN" | "CLOSED",
  "createdAt": ISODate,
  "updatedAt": ISODate
}
```

### 8. Notifications Collection

```json
{
  "_id": ObjectId,
  "userId": ObjectId,
  "type": "CONTEST_ANNOUNCEMENT" | "SYSTEM_UPDATE" | "PROBLEM_HINT",
  "title": "New contest available",
  "message": "Weekly Contest 50 is now live",
  "relatedEntity": {
    "entityType": "CONTEST" | "PROBLEM" | "COURSE",
    "entityId": ObjectId
  },
  "isRead": false,
  "createdAt": ISODate,
  "readAt": null | ISODate
}
```

### 9. Leaderboard Collection

```json
{
  "_id": ObjectId,
  "type": "GLOBAL" | "WEEKLY" | "CONTEST",
  "periodStart": ISODate,
  "periodEnd": ISODate,
  "entries": [
    {
      "rank": 1,
      "userId": ObjectId,
      "username": "johndoe",
      "score": 5000,
      "problemsSolved": 150,
      "updateTime": ISODate
    }
  ],
  "lastUpdated": ISODate
}
```

### Indexes to Create:

```javascript
// Users
db.users.createIndex({ email: 1 }, { unique: true });
db.users.createIndex({ username: 1 }, { unique: true });

// Problems
db.problems.createIndex({ difficulty: 1 });
db.problems.createIndex({ category: 1 });
db.problems.createIndex({ isPublished: 1 });

// Submissions
db.submissions.createIndex({ userId: 1, problemId: 1 });
db.submissions.createIndex({ status: 1 });
db.submissions.createIndex({ submittedAt: -1 });

// Contests
db.contests.createIndex({ startTime: 1 });
db.contests.createIndex({ status: 1 });

// Discussions
db.discussions.createIndex({ problemId: 1 });
db.discussions.createIndex({ author: 1 });

// Notifications
db.notifications.createIndex({ userId: 1, isRead: 1 });
```

---

## REST API Endpoints

### Authentication Module
```
POST   /api/v1/auth/register              - Register new user
POST   /api/v1/auth/login                 - Login user
POST   /api/v1/auth/logout                - Logout user
POST   /api/v1/auth/refresh-token         - Refresh JWT token
POST   /api/v1/auth/forgot-password       - Send reset email
POST   /api/v1/auth/reset-password        - Reset password
POST   /api/v1/auth/verify-email          - Verify email
GET    /api/v1/auth/validate-token        - Validate JWT token
```

### User Profile Module
```
GET    /api/v1/profile/{userId}           - Get user profile
PUT    /api/v1/profile/{userId}           - Update profile
GET    /api/v1/profile/{userId}/stats     - Get user statistics
GET    /api/v1/profile/{userId}/achievements - Get achievements
PUT    /api/v1/profile/{userId}/preferences - Update preferences
```

### Problems Module
```
GET    /api/v1/problems                   - Get all problems (paginated)
GET    /api/v1/problems/{id}              - Get problem details
POST   /api/v1/problems                   - Create problem (admin)
PUT    /api/v1/problems/{id}              - Update problem (admin)
DELETE /api/v1/problems/{id}              - Delete problem (admin)
GET    /api/v1/problems/{id}/solutions    - Get problem editorials
GET    /api/v1/problems/related/{id}      - Get related problems
```

### Submissions Module
```
POST   /api/v1/submissions                - Submit solution
GET    /api/v1/submissions/{id}           - Get submission details
GET    /api/v1/submissions                - Get user submissions
GET    /api/v1/submissions/problem/{problemId} - Get problem submissions
POST   /api/v1/submissions/{id}/run       - Run code (without submitting)
```

### Contests Module
```
GET    /api/v1/contests                   - Get all contests
GET    /api/v1/contests/{id}              - Get contest details
POST   /api/v1/contests                   - Create contest (admin)
PUT    /api/v1/contests/{id}              - Update contest (admin)
POST   /api/v1/contests/{id}/register    - Register for contest
DELETE /api/v1/contests/{id}/register    - Unregister from contest
GET    /api/v1/contests/{id}/leaderboard - Get contest leaderboard
```

### Courses Module
```
GET    /api/v1/courses                    - Get all courses
GET    /api/v1/courses/{id}               - Get course details
POST   /api/v1/courses                    - Create course (admin)
PUT    /api/v1/courses/{id}               - Update course (admin)
POST   /api/v1/courses/{id}/enroll       - Enroll in course
GET    /api/v1/courses/{id}/modules       - Get course modules
GET    /api/v1/courses/progress           - Get user course progress
```

### Discussions Module
```
GET    /api/v1/discussions                - Get all discussions
GET    /api/v1/discussions/{id}           - Get discussion thread
POST   /api/v1/discussions                - Create discussion
PUT    /api/v1/discussions/{id}           - Update discussion
DELETE /api/v1/discussions/{id}           - Delete discussion
POST   /api/v1/discussions/{id}/comments - Add comment
PUT    /api/v1/discussions/{id}/comments/{commentId} - Update comment
POST   /api/v1/discussions/{id}/upvote   - Upvote discussion
POST   /api/v1/discussions/{id}/downvote - Downvote discussion
```

### Leaderboard Module
```
GET    /api/v1/leaderboard/global         - Get global leaderboard
GET    /api/v1/leaderboard/weekly         - Get weekly leaderboard
GET    /api/v1/leaderboard/contests/{id}  - Get contest leaderboard
GET    /api/v1/leaderboard/user/{userId}  - Get user ranking
```

### Admin Module
```
GET    /api/v1/admin/dashboard            - Get admin dashboard
GET    /api/v1/admin/users                - Get all users
PUT    /api/v1/admin/users/{id}           - Update user status
DELETE /api/v1/admin/users/{id}           - Delete user
GET    /api/v1/admin/analytics            - Get platform analytics
POST   /api/v1/admin/notifications        - Send notification
```

### Notifications Module
```
GET    /api/v1/notifications              - Get user notifications
PUT    /api/v1/notifications/{id}/read    - Mark as read
DELETE /api/v1/notifications/{id}         - Delete notification
```

---

## Frontend Component Structure

### Component Hierarchy

```
App
├── Layout
│   ├── Navbar (student/admin specific)
│   ├── Sidebar (student/admin specific)
│   ├── Footer
│   └── Router
│       ├── StudentLayout
│       │   ├── Dashboard
│       │   │   ├── ActivityOverview
│       │   │   ├── ProgressCard
│       │   │   └── RecentActivity
│       │   ├── Problems
│       │   │   ├── ProblemList
│       │   │   └── ProblemDetail
│       │   │       ├── CodeEditor
│       │   │       ├── TestCaseRunner
│       │   │       └── SubmissionHistory
│       │   ├── Courses
│       │   │   ├── CourseList
│       │   │   └── CourseDetail
│       │   │       ├── ModuleView
│       │   │       └── VideoPlayer
│       │   ├── Contests
│       │   │   ├── ContestList
│       │   │   ├── ContestDetail
│       │   │   └── ContestEditor
│       │   ├── Leaderboard
│       │   │   ├── GlobalLeaderboard
│       │   │   ├── WeeklyLeaderboard
│       │   │   └── ContestLeaderboard
│       │   ├── Profile
│       │   │   ├── UserProfile
│       │   │   ├── ProfileEdit
│       │   │   ├── Achievements
│       │   │   └── Statistics
│       │   └── Discussions
│       │       ├── DiscussionList
│       │       └── DiscussionThread
│       │
│       └── AdminLayout
│           ├── Dashboard
│           ├── UserManagement
│           ├── ProblemManagement
│           ├── ContestManagement
│           ├── DiscussionModeration
│           └── AnalyticsDashboard
│
└── Toast/Modal (global)
```

### Reusable Components (Common)
- `Navbar` - Navigation bar
- `Sidebar` - Side navigation
- `LoadingSpinner` - Loading indicator
- `Modal` - Generic modal dialog
- `Toast` - Notification popup
- `Pagination` - Pagination component
- `Table` - Reusable table
- `Card` - Reusable card component

---

## Database Design Patterns

### Normalization vs Denormalization

1. **Referenced Collections (Normalized)**
   ```javascript
   // User document
   { _id: ObjectId, name: "John" }
   
   // Profile references User
   { _id: ObjectId, userId: ObjectId("...") }
   ```

2. **Embedded Documents (Denormalized)**
   ```javascript
   // Contest with embedded problems
   {
     _id: ObjectId,
     problems: [
       { _id: ObjectId, title: "Two Sum", ... }
     ]
   }
   ```

### Best Practices:
- **Use references** for: Users, Problems, Courses (large collections)
- **Use embedding** for: Test cases, Comments, Recent activities (small related data)
- **Update patterns**: Use aggregation pipelines for leaderboard calculations
- **Indexing strategy**: Index frequently queried fields + compound indexes

---

## Team Workflow & Best Practices

### Branch Organization

```
main (production-ready)
  ↓
develop (integration branch)
  ├── feature-auth (developer 1)
  ├── feature-profile (developer 2)
  ├── feature-problems (developer 3)
  ├── feature-editor (developer 4)
  ├── feature-submissions (developer 5)
  ├── feature-contest (developer 6)
  └── feature-leaderboard (developer 7)
```

### Developer Responsibilities

| Developer | Feature | Dependencies |
|-----------|---------|--------------|
| Dev 1 | Auth | - |
| Dev 2 | Profile | Auth |
| Dev 3 | Problems | Auth, Profile |
| Dev 4 | Editor | Problems, Auth |
| Dev 5 | Submissions | Problems, Editor, Auth |
| Dev 6 | Contest | Problems, Submissions, Auth |
| Dev 7 | Leaderboard | Contest, Submissions, Auth |

### Pull Request Workflow

1. **Create PR from feature branch → develop**
2. **Code review checklist:**
   - Code follows naming conventions
   - Tests are included
   - No merge conflicts
   - No hardcoded values
   - Proper error handling
3. **Minimum 1 approval required**
4. **Squash & merge or create merge commit**

### Conflict Prevention

- **Each feature owns its package** → No file conflicts
- **Database schema versioning** → Track changes
- **API contract versioning** → Backward compatibility
- **Regular integration** → Merge develop frequently

### Communication Protocol

1. **Daily standup** - 15 mins check-in
2. **Weekly sync** - Technical discussion
3. **Slack/Discord** - Quick questions
4. **GitHub Issues** - Feature planning

---

## Getting Started Checklist

- [ ] Clone repository
- [ ] Install backend dependencies (Maven)
- [ ] Install frontend dependencies (npm)
- [ ] Create `.env` files
- [ ] Start MongoDB
- [ ] Run backend: `mvn spring-boot:run`
- [ ] Run frontend: `npm start`
- [ ] Create first feature branch
- [ ] Make first commit
- [ ] Create pull request

---

**Next Steps:**
1. Review [API_DOCUMENTATION.md](../docs/API_DOCUMENTATION.md) for detailed endpoint specs
2. Review [DATABASE_SCHEMA.md](../docs/DATABASE_SCHEMA.md) for complete schema with validation
3. Review [TEAM_WORKFLOW.md](../docs/TEAM_WORKFLOW.md) for detailed collaboration guidelines
4. Review starter code examples in the next sections

