# CUTM Learning Platform - Code Review Report

## Summary

This code review identified and fixed several issues in the CUTM Learning Platform codebase. The issues ranged from API endpoint misconfigurations to missing methods and visibility modifiers.

---

## Issues Found and Fixed

### 1. **Missing `/api` Prefix in Controllers** (CRITICAL)

**Problem:** Several controllers were missing the `/api` prefix in their `@RequestMapping`, causing a mismatch with:
- Security configuration that expects `/api/**` paths
- Frontend API calls that use `/api/*` base URL

**Affected Controllers:**
- `AuthController` - Was `/auth`, now `/api/auth`
- `ProblemController` - Was `/problems`, now `/api/problems`
- `SubmissionController` - Was `/submissions`, now `/api/submissions`

**Fix Applied:** Added `/api` prefix to all controller RequestMappings.

---

### 2. **CORS Configuration Issues** (CRITICAL)

**Problem:** CORS was configured inconsistently across controllers:
- Some controllers had `http://localhost:3000` only
- Some had `http://localhost:5173` only
- Frontend runs on port 5173 (Vite)

**Fix Applied:** Updated all controllers to support both ports:
```java
@CrossOrigin(origins = {"http://localhost:5173", "http://localhost:3000"})
```

---

### 3. **Missing Methods in ContestService** (HIGH)

**Problem:** The ContestController called methods that didn't exist in ContestService:
- `getContestWithProblems(String contestId)` - Missing
- `getContestLeaderboard(String contestId)` - Missing

**Fix Applied:** Added both methods to ContestService.

---

### 4. **Package-Private Visibility Issue** (MEDIUM)

**Problem:** The `ProblemRef` class inside `Contest.java` was package-private (`class ProblemRef`), preventing access from the `ContestService` package.

**Fix Applied:** Changed to `public class ProblemRef`.

---

### 5. **Method Signature Mismatch** (MEDIUM)

**Problem:** `createContest` method in ContestService didn't match the controller's expectations. The controller passed `userId` but the method only took `Contest`.

**Fix Applied:** Updated method signature to:
```java
public Contest createContest(Contest contest, String userId)
```

---

## Additional Observations (Not Fixed)

### 1. LeaderboardService - Hardcoded Points
The `calculateUserPoints` method always defaults to `MEDIUM_POINTS` regardless of actual problem difficulty:
```java
totalPoints += MEDIUM_POINTS; // Default to medium
```
**Recommendation:** Fetch actual problem difficulty from the database.

### 2. Global Exception Handler
- Returns generic `RuntimeException` messages to clients (potential information leak)
- Consider creating custom exceptions with user-friendly messages

### 3. AuthController Error Handling
- Catches all exceptions and returns empty responses
- Should log actual errors for debugging

### 4. Duplicate Files in Frontend
There are duplicate `.js` and `.jsx` files (e.g., `Login.js` and `Login.jsx`, `Dashboard.js` and `Dashboard.jsx`).
**Recommendation:** Clean up duplicate files.

---

## Files Modified

### Backend
1. `AuthController.java` - Added `/api` prefix, fixed CORS
2. `ProblemController.java` - Added `/api` prefix, fixed CORS
3. `UserController.java` - Fixed CORS
4. `LeaderboardController.java` - Fixed CORS
5. `ContestController.java` - Fixed CORS
6. `SubmissionController.java` - Added `/api` prefix, fixed CORS
7. `ContestService.java` - Added missing methods, updated createContest
8. `Contest.java` - Made ProblemRef public

---

## Testing

Backend is now running successfully on `http://localhost:8080`

To verify the fixes:
1. Start MongoDB
2. Backend: `cd backend/platform && mvn spring-boot:run`
3. Frontend: `cd frontend && npm run dev`
4. Test registration/login at `http://localhost:5173`

---

*Report generated on 2026-03-11*

