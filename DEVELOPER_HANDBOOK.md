# Developer Handbook: Conflict Prevention & Task Assignment

> Practical guide for 7-member team to collaborate without merge conflicts

---

## Table of Contents

1. [Team Structure & Responsibilities](#team-structure--responsibilities)
2. [Feature Ownership](#feature-ownership)
3. [Architecture Boundaries](#architecture-boundaries)
4. [Task Assignment Strategy](#task-assignment-strategy)
5. [Preventing Merge Conflicts](#preventing-merge-conflicts)
6. [Daily Workflow](#daily-workflow)
7. [Communication Protocol](#communication-protocol)
8. [Code Ownership](#code-ownership)

---

## Team Structure & Responsibilities

### Team Composition: 7 Developers + 1 Tech Lead (You)

```
Tech Lead (You)
├── Backend Team (3-4 developers)
├── Frontend Team (3 developers)
└── Full Stack Support (1 developer)
```

### Recommended Assignment

| Member | Role | Feature | Backend Owner | Frontend Owner |
|--------|------|---------|---------------|----------------|
| Dev 1 | Backend | Auth | ✅ | - |
| Dev 2 | Backend | Problems | ✅ | - |
| Dev 3 | Backend | Submissions | ✅ | - |
| Dev 4 | Backend | Contests | ✅ | - |
| Dev 5 | Frontend | Dashboard & Profile | - | ✅ |
| Dev 6 | Frontend | Problems & Contests | - | ✅ |
| Dev 7 | Frontend | Leaderboard & Discussions | - | ✅ |

---

## Feature Ownership

### Each Developer Owns Their Feature **Completely**

#### Auth Module (Dev 1 - Backend)
**Files Only This Developer Touches:**
- `src/main/java/com/cutm/platform/auth/**` (all Java files)
- `src/test/java/com/cutm/platform/auth/**` (all tests)
- No one else modifies these files

**Shared Responsibilities:**
- Define User schema/model
- Create UserRepository interface
- Implement JWT utilities (in `common/auth/*`)

---

### Problems Module (Dev 2 - Backend)
**Files Only This Developer Touches:**
- `src/main/java/com/cutm/platform/problems/**`
- `src/test/java/com/cutm/platform/problems/**`

**Dependencies:**
- Uses UserRepository (from Dev 1)
- Implements ProblemRepository independently

---

### Submissions Module (Dev 3 - Backend)
**Files Only This Developer Touches:**
- `src/main/java/com/cutm/platform/submissions/**`
- `src/test/java/com/cutm/platform/submissions/**`

**Dependencies:**
- Uses UserRepository and ProfileRepository
- Uses ProblemRepository

**Process:**
1. Wait for Dev 1 & Dev 2 to create repositories
2. Once merged to develop, pull and use them
3. Implement SubmissionRepository independently

---

### Contests Module (Dev 4 - Backend)
**Files Only This Developer Touches:**
- `src/main/java/com/cutm/platform/contests/**`

**Dependencies:**
- Uses UserRepository, ProblemRepository

---

## Architecture Boundaries

### Backend Structure - NO OVERLAP

```
com/cutm/platform/
├── auth/                    [Dev 1 Only]
│   ├── controller/
│   ├── service/
│   ├── repository/
│   ├── model/
│   └── dto/
│
├── problems/                [Dev 2 Only]
│   ├── controller/
│   ├── service/
│   ├── repository/
│   ├── model/
│   └── dto/
│
├── submissions/             [Dev 3 Only]
│   ├── controller/
│   ├── service/
│   ├── repository/
│   ├── model/
│   └── dto/
│
├── contests/                [Dev 4 Only]
│   ├── controller/
│   ├── service/
│   ├── repository/
│   ├── model/
│   └── dto/
│
├── common/                  [SHARED - Careful!]
│   ├── exception/
│   ├── dto/
│   ├── util/
│   └── constant/
│
└── config/                  [SHARED - Tech Lead]
    ├── SecurityConfig.java
    ├── MongoConfig.java
    ├── CorsConfig.java
    └── JwtConfig.java
```

### Frontend Structure - NO OVERLAP

```
src/components/
├── auth/                    [Dev 5,6,7 Discuss Ownership]
│
├── dashboard/               [Dev 5 Only]
│   ├── StudentDashboard.js
│   ├── ActivityOverview.js
│   └── RecentActivity.js
│
├── profile/                 [Dev 5 Only]
│   ├── UserProfile.js
│   ├── ProfileEdit.js
│   └── Achievements.js
│
├── problems/                [Dev 6 Only]
│   ├── ProblemList.js
│   ├── ProblemDetail.js
│   └── CodeEditor.js
│
├── contests/                [Dev 6 Only]
│   ├── ContestList.js
│   ├── ContestDetail.js
│   └── ContestLeaderboard.js
│
├── leaderboard/             [Dev 7 Only]
│   ├── GlobalLeaderboard.js
│   ├── WeeklyLeaderboard.js
│   └── ContestLeaderboard.js
│
├── discussions/             [Dev 7 Only]
│   ├── DiscussionList.js
│   └── DiscussionThread.js
│
└── common/                  [SHARED - All Discuss]
    ├── Navbar.js
    ├── Sidebar.js
    ├── Modal.js
    └── LoadingSpinner.js
```

---

## Task Assignment Strategy

### Phase 1: Foundation (Week 1-2)

**Priority: Establish shared infrastructure**

| Task | Owner | Duration | Files |
|------|-------|----------|-------|
| Backend security config | Tech Lead | 3 days | `SecurityConfig.java`, `JwtConfig.java` |
| Frontend setup & routing | Tech Lead | 2 days | `App.js`, routing structure |
| Auth module (backend) | Dev 1 | 5 days | `auth/**` |
| Auth page (frontend) | Dev 5 | 3 days | `components/auth/**` |
| User profile model | Dev 1 | 2 days | `common/model/User.java` |
| Common DTOs | Tech Lead | 2 days | `common/dto/**` |
| Exception handling | Tech Lead | 1 day | `common/exception/**` |

**Deliverables:**
- ✅ Backend boots successfully
- ✅ Frontend builds without errors
- ✅ Login flow works
- ✅ JWT token generation works

### Phase 2: Core Features (Week 3-4)

**Priority: Implement isolated features**

| Feature | Owner | Depends On | Duration |
|---------|-------|-----------|----------|
| Problems (Backend) | Dev 2 | Auth | 5 days |
| Problems (Frontend) | Dev 6 | Auth + Backend API | 5 days |
| Submissions (Backend) | Dev 3 | Problems | 5 days |
| Submissions (Frontend) | Dev 6 | Problems + Backend API | 4 days |
| Contests (Backend) | Dev 4 | Problems | 4 days |
| Contests (Frontend) | Dev 6 | Problems + Backend API | 4 days |
| Dashboard (Frontend) | Dev 5 | Auth | 3 days |

### Phase 3: Secondary Features (Week 5-6)

| Feature | Owner | Depends On |
|---------|-------|-----------|
| Leaderboard (Backend) | Dev 3 or 4 | Contests, Submissions |
| Leaderboard (Frontend) | Dev 7 | Leaderboard API |
| Discussions (Backend) | Dev 1, 4 | Problems, Users |
| Discussions (Frontend) | Dev 7 | Discussions API |
| Courses (Backend) | All (collaborative) | Auth, Users |
| Courses (Frontend) | All (collaborative) | Courses API |

---

## Preventing Merge Conflicts

### Rule 1: Each Developer Owns Their Package

**Golden Rule:**
> If you didn't create the package, you don't modify any files in it (except via PR review and discussion).

### Rule 2: Shared Code Review Process

For `common/` and `config/` packages:

```
1. Dev proposes change
2. Tech Lead reviews
3. Tech Lead approves and merges
4. Others pull before working on shared code
```

### Rule 3: Repository Interface First

**Pattern:**
```java
// Dev 2 creates interface (others can see immediately)
public interface ProblemRepository extends MongoRepository<Problem, String> {
    Optional<Problem> findBySlug(String slug);
}

// Dev 3 uses it WITHOUT modifying
problemRepository.findBySlug("two-sum");
```

### Rule 4: Frequent Syncs with Develop

**Schedule:**
- **Every morning**: Pull latest `develop` into your feature branch
- **Before creating PR**: Rebase on latest develop
- **Before ending day**: Push your work

```powershell
# Every morning (9:00 AM)
git fetch origin develop
git rebase origin/develop

# If conflicts occur:
git rebase --abort  # Cancel and notify team
# Or resolve and continue
git add .
git rebase --continue
```

### Rule 5: Small, Focused Commits

**Good Practice:**
```
feature-auth (100 new lines)
├── Commit 1: Add User model (20 lines)
├── Commit 2: Add UserRepository interface (10 lines)
├── Commit 3: Add AuthService (50 lines)
└── Commit 4: Add AuthController (20 lines)
```

**Bad Practice:**
```
feature-auth (1000 new lines in single commit)
```

---

## Daily Workflow

### 9:00 AM - Start of Day

```powershell
# 1. Pull latest code
cd CUTM-Learning-Platform
git fetch origin develop
git status

# 2. Check if develop has updates
git log --oneline origin/develop..HEAD  # Shows commits we made
git log --oneline HEAD..origin/develop  # Shows new commits on develop

# 3. Sync your branch
git checkout feature-your-feature
git rebase origin/develop

# If conflicts occur, attend standup and notify team
```

### During Day - Regular Commits

```powershell
# Work on your feature
# ... modify files ...

# Every 2-3 hours, commit progress
git add .
git commit -m "feat: [module] Add specific feature

- Implementation detail 1
- Implementation detail 2

Related to #123"

git push origin feature-your-feature
```

### 5:00 PM - End of Day

```powershell
# Push final work
git push origin feature-your-feature

# Create/update PR (if significant progress)
# Or keep pushing to existing PR

# Notify team in Slack
# "Updated feature-auth with email verification logic"
```

### Before Creating PR

```powershell
# 1. Sync with latest develop
git fetch origin develop
git rebase origin/develop

# 2. Run tests locally
mvn test                    # Backend
npm test -- --watchAll=false  # Frontend

# 3. Run linter
mvn spotbugs:check          # Backend
npm run lint                # Frontend

# 4. Create PR
git push origin feature-your-feature

# GitHub → Open PR → Set reviewers
```

---

## Communication Protocol

### Daily Standup (9:15 AM - 15 mins)

**Format:**
```
[Name] - feature-module
✅ Yesterday: Completed X, Y, Z
🔄 Today: Working on X, Y
🚫 Blocker: Issue with Z (need help from [person])

[Next person]
...
```

**Example:**
```
Dev 1 - feature-auth
✅ Yesterday: Implemented login & password reset endpoints
🔄 Today: Adding email verification service
🚫 Blocker: None

Dev 2 - feature-problems
✅ Yesterday: Created Problem model & repository
🔄 Today: Implementing problem CRUD endpoints
🚫 Blocker: Need User entity from Dev 1 (just merged, will pull)
```

### Slack Channel Communication

**Channel: `#codehub-dev`**

**When to post:**
- Running into blocking issue
- Need code review urgently
- Completed major feature milestone
- Found bug that affects others
- Merged PR to develop

**Example:**
```
✋ @here [08:45 AM] - Feature-problems complete and merged to develop
- All problem CRUD endpoints implemented
- Tests passing with 85% coverage
- Ready for Dev 6 to start frontend

🔴 @Tech-Lead [02:30 PM] - Merge conflict issue
- Rebasing feature-submissions on develop
- Conflicts in common/dto/ApiResponse.java
- Need guidance before resolving
```

### PR Description Format

```markdown
## What does this PR do?
Brief description of changes...

## Type of Change
- [ ] New feature
- [ ] Bug fix
- [ ] Documentation update

## Related Issues
Closes #123
Relates to #456

## Testing
- [ ] Unit tests added
- [ ] All tests passing
- [ ] Manual testing done

## Checklist
- [ ] Code follows style guidelines
- [ ] Rebased on latest develop
- [ ] No console warnings/errors
- [ ] Documentation updated

## Testing Instructions
1. Pull this branch
2. Run `mvn test`
3. Start server: `mvn spring-boot:run`
4. Test endpoints with curl/Postman
```

---

## Code Ownership

### CODEOWNERS File

**File**: `.github/CODEOWNERS`

```
# Backend modules - feature owners required
backend/platform/src/main/java/com/cutm/platform/auth/**      @dev1
backend/platform/src/main/java/com/cutm/platform/problems/**   @dev2
backend/platform/src/main/java/com/cutm/platform/submissions/** @dev3
backend/platform/src/main/java/com/cutm/platform/contests/**   @dev4

# Frontend modules - feature owners required
frontend/src/components/dashboard/**    @dev5
frontend/src/components/profile/**      @dev5
frontend/src/components/problems/**     @dev6
frontend/src/components/contests/**     @dev6
frontend/src/components/leaderboard/**  @dev7
frontend/src/components/discussions/**  @dev7

# Shared code - tech lead required
backend/platform/src/main/java/com/cutm/platform/common/**  @tech-lead
backend/platform/src/main/java/com/cutm/platform/config/**  @tech-lead
frontend/src/common/**                                       @tech-lead
frontend/src/utils/**                                        @tech-lead
frontend/src/hooks/**                                        @tech-lead
frontend/src/context/**                                      @tech-lead

# Configuration files
pom.xml      @tech-lead
package.json @tech-lead
docker-compose.yml @tech-lead
```

### PR Review Requirements

```yaml
# Branch protection rules

On develop branch:
- Require pull request reviews: 2
- Require status checks passing
- Require code review from CODEOWNERS
- Dismiss stale pull request approvals
- Require conversation resolution

On feature branches:
- Minimum 1 review from CODEOWNER
- All tests must pass
- No conflicts with develop
```

---

## Conflict Scenarios & Solutions

### Scenario 1: Dev 2 & Dev 3 Both Modify common/dto/ApiResponse.java

**Prevention:**
1. Don't do it (core rule)
2. If needed: coordinate in Slack
3. Tech Lead reviews and merges changes

**Resolution:**
1. Stop work and notify team in Slack
2. Tech Lead investigates and merges
3. Both devs pull latest and continue

### Scenario 2: Dev 5 & Dev 6 Both Modify Frontend common/Navbar.js

**Prevention:**
1. Discuss component design in advance
2. Navbar owned by Tech Lead, others request modifications
3. Tech Lead makes changes and pushes to develop

**Resolution:**
1. One dev reverts their changes
2. Both coordinate on needed modifications
3. Tech Lead implements and pushes

### Scenario 3: Dev 1's PR Can't Merge - Conflicts with Develop

**Causes:**
- Common files were modified
- Someone else modified the same package

**Solution:**
```powershell
# Check what changed on develop
git fetch origin
git log --oneline origin/develop..HEAD

# Rebase your branch
git rebase origin/develop

# Resolve conflicts in your editor
# Or if you can't resolve, ask for help:
git rebase --abort  # Cancel rebasing
# Notify team in Slack
```

---

## Schedule Example

```
9:00 AM   - Daily standup
9:15 AM   - Start coding
12:00 PM  - First sync (pull + commit)
1:00 PM   - Lunch break
2:00 PM   - Continue coding
4:00 PM   - Second sync (pull + commit)
5:00 PM   - Push work, prepare for next day
```

---

## Metrics & Tracking

### Weekly Goals

- [ ] All developers make >= 5 commits
- [ ] No merge conflicts during rebase
- [ ] All PRs merged within 24 hours
- [ ] >= 80% test coverage
- [ ] Zero bugs in `develop` branch

### Monthly Goals

- [ ] Complete assigned features on time
- [ ] Reduce PR review time to < 4 hours
- [ ] Zero critical bugs in production
- [ ] Documentation 100% up-to-date

---

## Escalation Path

**Issue → Action → Owner → Timeline**

| Issue | Action | Owner | Timeline |
|-------|--------|-------|----------|
| Small bug | Submit PR | Feature owner | 1 day |
| Blocking issue | Slack + meeting | Tech Lead | 2 hours |
| Architectural question | Design discussion | All + Tech Lead | 1 day |
| Merge conflict | Investigate + resolve | Tech Lead | 2 hours |
| Critical bug | Hotfix branch | Tech Lead + Owner | Immediate |

---

## Summary Checklist

### For Each Developer

- [ ] Clone repository and setup
- [ ] Understand your feature scope
- [ ] Know who owns shared files
- [ ] Follow commit message conventions
- [ ] Sync with develop every morning
- [ ] Comment in Slack before modifying shared code
- [ ] Small, focused PRs (< 500 lines)
- [ ] Wait for 2 approvals before merging
- [ ] Delete branch after merging
- [ ] Keep documentation updated

### For Tech Lead (You)

- [ ] Setup repository with CODEOWNERS
- [ ] Setup branch protection rules
- [ ] Review all PRs merging to develop
- [ ] Manage shared code ownership
- [ ] Resolve conflicts
- [ ] Facilitate communication
- [ ] Track progress and blockers
- [ ] Onboard new team members
- [ ] Make architectural decisions

