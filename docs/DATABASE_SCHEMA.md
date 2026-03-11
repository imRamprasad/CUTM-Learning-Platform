# Database Schema Documentation

> Comprehensive MongoDB schema design for CodeHub platform

---

## Collection: Users

**Purpose**: Store user account information

```javascript
{
  "_id": ObjectId("507f1f77bcf86cd799439011"),
  "email": "john.doe@example.com",
  "username": "johndoe",
  "password": "$2a$10$...", // bcrypt hashed
  "firstName": "John",
  "lastName": "Doe",
  "profileImage": "https://storage.example.com/profiles/...",
  "bio": "Passionate about competitive programming",
  "role": "STUDENT", // STUDENT | ADMIN | INSTRUCTOR
  "status": "ACTIVE", // ACTIVE | INACTIVE | SUSPENDED | DELETED
  "emailVerified": true,
  "emailVerificationToken": null,
  "emailVerificationTokenExpiry": null,
  "socialLinks": {
    "github": "https://github.com/johndoe",
    "linkedin": "https://linkedin.com/in/johndoe",
    "twitter": "https://twitter.com/johndoe"
  },
  "socialAuthProviders": {
    "google": "google_oauth_id",
    "github": "github_oauth_id"
  },
  "passwordResetToken": null,
  "passwordResetTokenExpiry": null,
  "lastLogin": ISODate("2026-03-11T10:30:00Z"),
  "loginAttempts": 0,
  "lockUntil": null,
  "createdAt": ISODate("2026-01-15T08:00:00Z"),
  "updatedAt": ISODate("2026-03-11T10:30:00Z")
}
```

**Indexes:**
```javascript
db.users.createIndex({ email: 1 }, { unique: true });
db.users.createIndex({ username: 1 }, { unique: true });
db.users.createIndex({ status: 1 });
db.users.createIndex({ createdAt: -1 });
db.users.createIndex({ "socialAuthProviders.google": 1 }, { sparse: true });
db.users.createIndex({ "socialAuthProviders.github": 1 }, { sparse: true });
```

---

## Collection: UserProfiles

**Purpose**: Store detailed user statistics and preferences

```javascript
{
  "_id": ObjectId("507f1f77bcf86cd799439012"),
  "userId": ObjectId("507f1f77bcf86cd799439011"),
  "totalProblems": 150,
  "solvedProblems": 45,
  "attemptedProblems": 120,
  "acceptanceRate": 37.5,
  "contest": {
    "participated": 5,
    "won": 1,
    "rank": 250
  },
  "points": 5000,
  "ranking": 250,
  "streak": {
    "current": 7,
    "longest": 21,
    "lastActivityDate": ISODate("2026-03-11T10:30:00Z")
  },
  "badges": [
    "problem-solver-25",
    "python-expert",
    "first-contest",
    "weekly-champion"
  ],
  "achievements": [
    {
      "_id": ObjectId(),
      "name": "Problem Solver - 25 Problems",
      "description": "Solve 25 problems",
      "earnedAt": ISODate("2026-03-10T15:00:00Z"),
      "badge": "https://storage.example.com/badges/problem-solver.png"
    }
  ],
  "languages": {
    "java": { "count": 30, "level": "advanced" },
    "python": { "count": 45, "level": "intermediate" },
    "cpp": { "count": 15, "level": "beginner" }
  },
  "preferredLanguage": "java",
  "preferences": {
    "theme": "dark", // dark | light
    "language": "Java",
    "enableNotifications": true,
    "emailNotifications": true,
    "problemDifficulty": "medium" // easy | medium | hard | all
  },
  "createdAt": ISODate("2026-01-15T08:00:00Z"),
  "updatedAt": ISODate("2026-03-11T10:30:00Z")
}
```

**Indexes:**
```javascript
db.users_profile.createIndex({ userId: 1 }, { unique: true });
db.users_profile.createIndex({ ranking: 1 });
db.users_profile.createIndex({ points: -1 });
db.users_profile.createIndex({ solvedProblems: -1 });
```

---

## Collection: Problems

**Purpose**: Store coding problems

```javascript
{
  "_id": ObjectId("507f1f77bcf86cd799439013"),
  "title": "Two Sum",
  "slug": "two-sum",
  "description": "Given an array of integers nums and an integer target...",
  "difficulty": "EASY", // EASY | MEDIUM | HARD
  "category": "Arrays", // Arrays | Hash Table | Binary Search | etc.
  "subcategories": ["two-pointers", "hash-table"],
  
  "problem_statement": {
    "description": "...",
    "constraints": "1 <= nums.length <= 10^4, -10^9 <= nums[i] <= 10^9",
    "input_description": "nums = [2, 7, 11, 15], target = 9",
    "output_description": "[0, 1]"
  },
  
  "examples": [
    {
      "input": "nums = [2, 7, 11, 15], target = 9",
      "output": "[0, 1]",
      "explanation": "Because nums[0] + nums[1] == 9, we return [0, 1]."
    }
  ],
  
  "testCases": [
    {
      "_id": ObjectId(),
      "input": "nums = [2, 7, 11, 15], target = 9",
      "output": "[0, 1]",
      "isHidden": false
    },
    {
      "_id": ObjectId(),
      "input": "nums = [3, 2, 4], target = 6",
      "output": "[1, 2]",
      "isHidden": false
    },
    {
      "_id": ObjectId(),
      "input": "nums = [3, 3], target = 6",
      "output": "[0, 1]",
      "isHidden": true // Hidden test case not shown to users
    }
  ],
  
  "hints": [
    "A really brute force way would be to search for all possible pairs...",
    "A better way is using a hash map for O(n) complexity..."
  ],
  
  "editorials": [
    {
      "language": "java",
      "approach": "HashMap Approach",
      "timeComplexity": "O(n)",
      "spaceComplexity": "O(n)",
      "code": "public int[] twoSum(int[] nums, int target) { ... }",
      "explanation": "Use HashMap to store visited numbers..."
    },
    {
      "language": "python",
      "approach": "Two Pointer",
      "timeComplexity": "O(n log n)",
      "spaceComplexity": "O(1)",
      "code": "def twoSum(nums, target): ...",
      "explanation": "Sort and use two pointers..."
    }
  ],
  
  "statistics": {
    "totalSubmissions": 5000000,
    "acceptedSubmissions": 2125000,
    "acceptanceRate": 42.5,
    "averageRating": 4.2,
    "totalUpvotes": 15000,
    "totalDownvotes": 300
  },
  
  "tags": ["array", "hash-table", "two-pointers"],
  "relatedProblems": [
    ObjectId("507f1f77bcf86cd799439014"),
    ObjectId("507f1f77bcf86cd799439015")
  ],
  
  "createdBy": ObjectId("507f1f77bcf86cd799439016"), // Admin user
  "createdAt": ISODate("2026-01-01T00:00:00Z"),
  "updatedAt": ISODate("2026-03-11T10:30:00Z"),
  "isPublished": true,
  "version": 1
}
```

**Indexes:**
```javascript
db.problems.createIndex({ slug: 1 }, { unique: true });
db.problems.createIndex({ difficulty: 1 });
db.problems.createIndex({ category: 1 });
db.problems.createIndex({ tags: 1 });
db.problems.createIndex({ isPublished: 1 });
db.problems.createIndex({ "statistics.acceptanceRate": -1 });
db.problems.createIndex({ createdAt: -1 });
```

---

## Collection: Submissions

**Purpose**: Track user code submissions

```javascript
{
  "_id": ObjectId("507f1f77bcf86cd799439017"),
  "userId": ObjectId("507f1f77bcf86cd799439011"),
  "problemId": ObjectId("507f1f77bcf86cd799439013"),
  "language": "java", // java | python | cpp | javascript
  "code": "class Solution { ... }",
  "submissionType": "SUBMIT", // SUBMIT | RUN
  
  "execution": {
    "status": "ACCEPTED", // ACCEPTED | WRONG_ANSWER | TIME_LIMIT_EXCEEDED | RUNTIME_ERROR | COMPILATION_ERROR
    "executionTime": 45, // ms
    "memory": 56, // MB
    "errorMessage": null,
    "errorLine": null,
    "errorStackTrace": null
  },
  
  "testResults": [
    {
      "testCaseId": ObjectId(),
      "testCaseIndex": 0,
      "passed": true,
      "executionTime": 10,
      "memory": 20,
      "output": "[0, 1]",
      "expectedOutput": "[0, 1]",
      "input": "nums = [2, 7, 11, 15], target = 9"
    },
    {
      "testCaseId": ObjectId(),
      "testCaseIndex": 1,
      "passed": false,
      "executionTime": 15,
      "memory": 25,
      "output": "[1, 2]",
      "expectedOutput": "[0, 1]",
      "input": "nums = [3, 3], target = 6"
    }
  ],
  
  "verdict": {
    "status": "ACCEPTED", // ACCEPTED | WRONG_ANSWER
    "score": 100, // 0-100
    "passedTestCases": 1,
    "totalTestCases": 3
  },
  
  "contestId": null, // ObjectId if submitted in contest
  "submittedAt": ISODate("2026-03-11T10:30:00Z"),
  "createdAt": ISODate("2026-03-11T10:30:00Z")
}
```

**Indexes:**
```javascript
db.submissions.createIndex({ userId: 1, problemId: 1 });
db.submissions.createIndex({ problemId: 1 });
db.submissions.createIndex({ "execution.status": 1 });
db.submissions.createIndex({ submittedAt: -1 });
db.submissions.createIndex({ userId: 1, submittedAt: -1 });
db.submissions.createIndex({ contestId: 1 });
```

---

## Collection: Contests

**Purpose**: Store contest information

```javascript
{
  "_id": ObjectId("507f1f77bcf86cd799439018"),
  "title": "Weekly Contest 50",
  "slug": "weekly-contest-50",
  "description": "Solve 4 problems in 2 hours",
  "status": "FINISHED", // UPCOMING | ONGOING | FINISHED
  
  "timing": {
    "startTime": ISODate("2026-03-15T10:00:00Z"),
    "endTime": ISODate("2026-03-15T12:00:00Z"),
    "duration": 120 // minutes
  },
  
  "problems": [
    {
      "id": ObjectId("507f1f77bcf86cd799439019"),
      "title": "Two Sum",
      "difficulty": "EASY",
      "points": 100
    },
    {
      "id": ObjectId("507f1f77bcf86cd79943901a"),
      "title": "Add Two Numbers",
      "difficulty": "MEDIUM",
      "points": 200
    }
  ],
  
  "participants": {
    "registeredCount": 500,
    "registeredUsers": [ObjectId(...), ObjectId(...)]
  },
  
  "leaderboard": [
    {
      "rank": 1,
      "userId": ObjectId("507f1f77bcf86cd799439011"),
      "username": "johndoe",
      "totalScore": 300,
      "solvedProblems": 3,
      "penaltyTime": 45, // minutes
      "submissions": [
        {
          "problemId": ObjectId("507f1f77bcf86cd799439019"),
          "status": "ACCEPTED",
          "submissionTime": 10,
          "attempts": 1
        }
      ]
    }
  ],
  
  "rules": {
    "allowMultipleLanguages": true,
    "enablePeerDiscussion": true,
    "showTestCaseFeedback": true,
    "showPartialScores": false
  },
  
  "createdBy": ObjectId("507f1f77bcf86cd799439016"),
  "createdAt": ISODate("2026-03-01T00:00:00Z"),
  "updatedAt": ISODate("2026-03-11T10:30:00Z"),
  "isPublished": true
}
```

**Indexes:**
```javascript
db.contests.createIndex({ slug: 1 }, { unique: true });
db.contests.createIndex({ status: 1 });
db.contests.createIndex({ "timing.startTime": 1 });
db.contests.createIndex({ createdAt: -1 });
```

---

## Collection: Courses

**Purpose**: Store educational courses

```javascript
{
  "_id": ObjectId("507f1f77bcf86cd79943901b"),
  "title": "Complete Java Programming",
  "slug": "complete-java-programming",
  "description": "Learn Java from basics to advanced...",
  "difficulty": "BEGINNER", // BEGINNER | INTERMEDIATE | ADVANCED
  "category": "Programming Languages",
  "thumbnail": "https://storage.example.com/courses/...",
  "duration": 120, // hours
  
  "instructor": {
    "id": ObjectId("507f1f77bcf86cd799439016"),
    "name": "John Instructor",
    "bio": "..."
  },
  
  "enrollments": {
    "totalEnrolled": 5000,
    "enrolledUsers": [ObjectId(...), ObjectId(...)]
  },
  
  "modules": [
    {
      "_id": ObjectId(),
      "title": "Java Basics",
      "description": "Variables, data types, operators",
      "order": 1,
      "lessons": [
        {
          "_id": ObjectId(),
          "title": "Variables and Data Types",
          "duration": 15, // minutes
          "videoUrl": "https://storage.example.com/videos/...",
          "transcript": "... transcript ...",
          "resources": [
            {
              "title": "Java Basics PDF",
              "url": "https://storage.example.com/resources/..."
            }
          ],
          "order": 1
        },
        {
          "_id": ObjectId(),
          "title": "Operators in Java",
          "duration": 20,
          "videoUrl": "https://storage.example.com/videos/...",
          "order": 2
        }
      ]
    },
    {
      "_id": ObjectId(),
      "title": "OOP Concepts",
      "order": 2,
      "lessons": [...]
    }
  ],
  
  "userProgress": [
    {
      "userId": ObjectId("507f1f77bcf86cd799439011"),
      "enrolledAt": ISODate("2026-02-01T00:00:00Z"),
      "completedModules": 2,
      "totalModules": 5,
      "completionPercentage": 40,
      "lastAccessedAt": ISODate("2026-03-11T10:30:00Z"),
      "lessonsCompleted": [ObjectId(...), ObjectId(...)]
    }
  ],
  
  "rating": {
    "averageRating": 4.8,
    "totalReviews": 500,
    "distribution": {
      "5": 450,
      "4": 40,
      "3": 8,
      "2": 1,
      "1": 1
    }
  },
  
  "createdBy": ObjectId("507f1f77bcf86cd799439016"),
  "createdAt": ISODate("2026-01-01T00:00:00Z"),
  "updatedAt": ISODate("2026-03-11T10:30:00Z"),
  "isPublished": true
}
```

**Indexes:**
```javascript
db.courses.createIndex({ slug: 1 }, { unique: true });
db.courses.createIndex({ difficulty: 1 });
db.courses.createIndex({ category: 1 });
db.courses.createIndex({ "rating.averageRating": -1 });
db.courses.createIndex({ isPublished: 1 });
```

---

## Collection: Discussions

**Purpose**: Store discussion threads and comments

```javascript
{
  "_id": ObjectId("507f1f77bcf86cd79943901c"),
  "title": "How to optimize two sum solution?",
  "slug": "how-to-optimize-two-sum-solution",
  "description": "I'm getting TLE with my current approach",
  
  "author": ObjectId("507f1f77bcf86cd799439011"),
  "authorName": "johndoe",
  
  "relatedEntity": {
    "type": "PROBLEM", // PROBLEM | COURSE | CONTEST
    "id": ObjectId("507f1f77bcf86cd799439013")
  },
  
  "tags": ["optimization", "array", "performance"],
  "category": "Implementation", // Implementation | Theory | Help | etc.
  
  "engagement": {
    "upvotes": 25,
    "downvotes": 2,
    "views": 150,
    "replies": 5
  },
  
  "comments": [
    {
      "_id": ObjectId(),
      "author": ObjectId("507f1f77bcf86cd799439016"),
      "authorName": "instructor",
      "content": "Try using HashMap instead of nested loops...",
      "upvotes": 10,
      "downvotes": 1,
      "level": 0, // 0 for main comments
      "replies": [
        {
          "_id": ObjectId(),
          "author": ObjectId("507f1f77bcf86cd799439011"),
          "content": "Thanks! This really helped.",
          "upvotes": 3,
          "downvotes": 0,
          "level": 1, // 1 for replies
          "createdAt": ISODate("2026-03-11T12:00:00Z")
        }
      ],
      "createdAt": ISODate("2026-03-11T11:00:00Z")
    }
  ],
  
  "status": "OPEN", // OPEN | CLOSED | RESOLVED
  "isPinned": false,
  "isFeatured": false,
  
  "createdAt": ISODate("2026-03-11T10:00:00Z"),
  "updatedAt": ISODate("2026-03-11T12:30:00Z")
}
```

**Indexes:**
```javascript
db.discussions.createIndex({ slug: 1 }, { unique: true });
db.discussions.createIndex({ "relatedEntity.id": 1 });
db.discussions.createIndex({ author: 1 });
db.discussions.createIndex({ status: 1 });
db.discussions.createIndex({ createdAt: -1 });
db.discussions.createIndex({ "engagement.upvotes": -1 });
```

---

## Collection: Leaderboards

**Purpose**: Store cached leaderboard data

```javascript
{
  "_id": ObjectId("507f1f77bcf86cd79943901d"),
  "type": "GLOBAL", // GLOBAL | WEEKLY | CONTEST
  "contestId": null, // ObjectId if CONTEST type
  
  "period": {
    "startDate": ISODate("2026-03-01T00:00:00Z"),
    "endDate": ISODate("2026-03-31T23:59:59Z")
  },
  
  "entries": [
    {
      "rank": 1,
      "userId": ObjectId("507f1f77bcf86cd799439011"),
      "username": "johndoe",
      "profileImage": "https://storage.example.com/...",
      "score": 5000,
      "problemsSolved": 150,
      "solvedInPeriod": 10,
      "contestWins": 3,
      "streak": 7,
      "lastActivityDate": ISODate("2026-03-11T10:30:00Z")
    },
    {
      "rank": 2,
      "userId": ObjectId("507f1f77bcf86cd79943901e"),
      "username": "janedoe",
      "profileImage": "...",
      "score": 4800,
      "problemsSolved": 140,
      "solvedInPeriod": 8,
      "contestWins": 2,
      "streak": 5,
      "lastActivityDate": ISODate("2026-03-11T09:00:00Z")
    }
  ],
  
  "lastUpdated": ISODate("2026-03-11T10:30:00Z"),
  "cacheExpiresAt": ISODate("2026-03-11T12:30:00Z")
}
```

**Indexes:**
```javascript
db.leaderboards.createIndex({ type: 1, "period.startDate": -1 });
db.leaderboards.createIndex({ contestId: 1 });
db.leaderboards.createIndex({ "entries.rank": 1 });
```

---

## Collection: Notifications

**Purpose**: Store user notifications

```javascript
{
  "_id": ObjectId("507f1f77bcf86cd79943901f"),
  "userId": ObjectId("507f1f77bcf86cd799439011"),
  "notificationType": "CONTEST_ANNOUNCEMENT", // Multiple types
  
  "content": {
    "title": "New contest available",
    "message": "Weekly Contest 50 is now live",
    "actionUrl": "/contests/weekly-contest-50"
  },
  
  "relatedEntity": {
    "entityType": "CONTEST", // CONTEST | PROBLEM | COURSE | USER
    "entityId": ObjectId("507f1f77bcf86cd799439018")
  },
  
  "readStatus": {
    "isRead": false,
    "readAt": null
  },
  
  "priority": "NORMAL", // LOW | NORMAL | HIGH
  "createdAt": ISODate("2026-03-11T10:30:00Z"),
  "expiresAt": ISODate("2026-04-11T10:30:00Z")
}
```

**Indexes:**
```javascript
db.notifications.createIndex({ userId: 1, isRead: 1 });
db.notifications.createIndex({ userId: 1, createdAt: -1 });
db.notifications.createIndex({ expiresAt: 1 }, { expireAfterSeconds: 0 }); // TTL
```

---

## Data Relationships

```
Users
├── UserProfiles (1-to-1)
├── Submissions (1-to-many)
├── Contests (many-to-many through participants)
├── Courses (many-to-many through enrollments)
├── Discussions (1-to-many)
└── Notifications (1-to-many)

Problems
├── Submissions (1-to-many)
├── Discussions (1-to-many)
└── Contests (many-to-many through problems)

Contests
├── Problems (many-to-many)
└── Leaderboards (1-to-1)
```

---

## Best Practices

1. **Always include timestamps**: `createdAt` and `updatedAt`
2. **Use ObjectId for references**: Between documents
3. **Embed small related data**: TestCases, Comments
4. **Index every query field**: Improve performance
5. **Use TTL for temporary data**: Notifications, Reset tokens
6. **Denormalize for speed**: Username, Profile picture
7. **Archive old data**: Contests, Submissions after period
8. **Use transactions**: For multi-document updates

