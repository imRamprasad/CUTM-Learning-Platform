# Quick Reference Guide

> Keep this bookmarked! One-page reference for common tasks

---

## 🚀 Daily Start (Copy & Paste)

```powershell
# 1. Start of morning (9:00 AM)
cd CUTM-Learning-Platform
git fetch origin develop
git checkout feature-your-feature
git rebase origin/develop
# If conflicts: resolve, git add ., git rebase --continue

# 2. Start coding
# ... make changes ...

# 3. Every 2-3 hours
git add .
git commit -m "feat: module - Brief description of what you did"
git push origin feature-your-feature

# 4. End of day (5:00 PM)
git push origin feature-your-feature
# Notify team: "Updated feature-X with Y"
```

---

## 📝 Git Commands Cheat Sheet

```powershell
# Setup branch
git checkout develop
git pull origin develop
git checkout -b feature-your-feature

# Daily sync
git fetch origin
git rebase origin/develop
# Resolve conflicts if any

# Commit changes
git add .
git commit -m "feat(module): description"
git push origin feature-your-feature

# Pull from develop without pushing (safe)
git fetch origin develop
git log --oneline HEAD..origin/develop
git log --oneline origin/develop..HEAD

# Fix last commit message
git commit --amend -m "new message"
git push origin feature-your-feature -f

# Undo last commit (keep changes)
git reset --soft HEAD~1

# Discard changes
git checkout -- src/file.js
```

---

## 🔧 Backend Commands

```powershell
cd backend/platform

# Install dependencies
mvn clean install

# Run application
mvn spring-boot:run

# Run with specific profile
mvn spring-boot:run -Dspring-boot.run.arguments="--spring.profiles.active=dev"

# Run tests
mvn test

# Run specific test
mvn test -Dtest=UserServiceTest

# Build JAR
mvn clean package -DskipTests

# Check code quality
mvn spotbugs:check
mvn test -Pit.coverage

# Format code
mvn spotless:apply
```

---

## ⚛️ Frontend Commands

```powershell
cd frontend

# Install packages
npm install

# Start dev server
npm start

# Run tests
npm test

# Run tests (no watch)
npm test -- --watchAll=false

# Build for production
npm run build

# Lint code
npm run lint

# Format code
npm run format

# Clean cache
rm -rf node_modules package-lock.json
npm install
```

---

## 📂 File Locations

### Backend Models
```
src/main/java/com/cutm/platform/MODULE_NAME/model/
├── User.java, Problem.java, etc.
```

### Backend Services
```
src/main/java/com/cutm/platform/MODULE_NAME/service/
├── SomeService.java
```

### Backend Controllers
```
src/main/java/com/cutm/platform/MODULE_NAME/controller/
├── SomeController.java
```

### Backend Repositories
```
src/main/java/com/cutm/platform/MODULE_NAME/repository/
├── SomeRepository.java
```

### Frontend Components
```
src/components/MODULE_NAME/
├── Component.js
├── Component.css
```

### Frontend Services
```
src/services/
├── authService.js
├── problemService.js
```

### Facebook Configurations
```
backend/platform/src/main/resources/
├── application.properties
├── application-dev.properties
└── application-prod.properties

frontend/
├── .env
├── .env.development
└── .env.production
```

---

## 🔐 API Endpoints Quick Reference

### Auth Endpoints
```
POST   /api/v1/auth/register
POST   /api/v1/auth/login
POST   /api/v1/auth/logout
POST   /api/v1/auth/verify-email?token=...
POST   /api/v1/auth/forgot-password?email=...
POST   /api/v1/auth/reset-password?token=&password=&confirmPassword=
```

### Problems Endpoints
```
GET    /api/v1/problems?page=0&size=20&difficulty=EASY&category=Arrays
GET    /api/v1/problems/{id}
POST   /api/v1/problems (ADMIN)
PUT    /api/v1/problems/{id} (ADMIN)
DELETE /api/v1/problems/{id} (ADMIN)
```

### Submissions Endpoints
```
POST   /api/v1/submissions
GET    /api/v1/submissions/{id}
GET    /api/v1/submissions?userId=...
POST   /api/v1/submissions/{id}/run
```

### Contests Endpoints
```
GET    /api/v1/contests
GET    /api/v1/contests/{id}
POST   /api/v1/contests/{id}/register
GET    /api/v1/contests/{id}/leaderboard
```

---

## 🧪 Testing Code Examples

### Unit Test (Backend)
```java
@SpringBootTest
class UserServiceTest {
    @Autowired
    private UserService userService;

    @Test
    void testRegister() {
        // Arrange
        RegisterRequest request = new RegisterRequest();
        request.setEmail("test@example.com");

        // Act
        userService.register(request);

        // Assert
        assertTrue(userRepository.existsByEmail("test@example.com"));
    }
}
```

### Component Test (Frontend)
```javascript
import { render, screen } from '@testing-library/react';
import Login from './Login';

test('renders login form', () => {
    render(<Login />);
    expect(screen.getByPlaceholderText(/email/i)).toBeInTheDocument();
});
```

---

## ❌ Common Mistakes & Fixes

| Mistake | Problem | Fix |
|---------|---------|-----|
| Not syncing before starting work | Merge conflicts later | `git fetch origin && git rebase origin/develop` |
| Large commits with many changes | Hard to review & revert | Make small commits (1-2 files) |
| No descriptive commit messages | Impossible to understand history | Use: `feat, fix, docs, style, refactor, test` |
| Modifying files you don't own | Creates conflicts for others | Only modify your module files |
| Force pushing to develop/main | Destroys history for everyone | Never! Only force push to feature branches |
| Committing secrets (keys, passwords) | Security breach! | Add to .gitignore, use environment variables |
| Not testing before PRing | Builds fail for others | `npm test`, `mvn test` locally |
| Long-lived feature branches | Diverges from develop | Merge at least weekly |
| Not pulling develop for days | Major conflicts later | Daily: `git fetch && git rebase origin/develop` |

---

## 🆘 Troubleshooting

### Backend Won't Start
```powershell
# 1. Check Java version
java -version

# 2. Check MongoDB is running
mongod  # or 'mongo' to connect

# 3. Check port 8080 not in use
netstat -ano | findstr :8080

# 4. Clean and rebuild
mvn clean install
mvn spring-boot:run

# 5. Check logs for actual error
```

### Frontend Won't Start
```powershell
# 1. Check Node version
node --version

# 2. Delete cache
rm -rf node_modules package-lock.json

# 3. Reinstall
npm install

# 4. Start
npm start

# 5. Check console for error messages
```

### Can't Merge to Develop
```powershell
# Reason: Conflicts with develop

# Solution:
git fetch origin develop
git rebase origin/develop
# Resolve conflicts in your editor
git add . && git rebase --continue
git push origin feature-your-feature

# If that's too complicated:
git rebase --abort
# Ask for help in Slack #codehub-dev
```

---

## 📋 Code Review Checklist

Before requesting review:

- [ ] Rebased on latest develop
- [ ] All tests passing locally
- [ ] No linting warnings
- [ ] Wrote clear commit messages
- [ ] Updated relevant comments
- [ ] No hardcoded values/secrets
- [ ] Error handling implemented
- [ ] Function/class documented
- [ ] < 500 lines changed
- [ ] Related to single feature

---

## 🔑 Important Files

### To Understand Architecture
- ARCHITECTURE.md (45 min read)
- DEVELOPER_HANDBOOK.md (30 min read)

### To Setup Environment
- docs/SETUP_GUIDE.md

### Code Examples
- docs/STARTER_CODE_BACKEND.md
- docs/STARTER_CODE_FRONTEND.md

### To Follow Team Workflow
- TEAM_WORKFLOW.md

### Database Info
- docs/DATABASE_SCHEMA.md

---

## 💬 Slack/Communication

### Standup Template (9:00 AM)
```
[Your Name] - feature-module
✅ Yesterday: Completed X, Y
🔄 Today: Working on X
🚫 Blocker: [If any]
```

### When You Finish PR
```
"Created PR for feature-X
- Implementation done
- Tests passing (85% coverage)
- Ready for review
cc: @reviewer1 @reviewer2"
```

### When You Have Issue
```
"Merge conflict in feature-problems
- Rebasing on latest develop
- Conflict in common/dto/ApiResponse.java
- Need help before resolving
cc: @tech-lead"
```

---

## 📊 Code Style Quick Rules

### Java
- Class names: `PascalCase` (UserService)
- Method names: `camelCase` (getUserById)
- Constants: `UPPER_SNAKE_CASE`
- Line length: max 120 chars
- 4 spaces indentation

### JavaScript
- File names: `PascalCase.js` (components), `camelCase.js` (utils)
- Variable names: `camelCase`
- Constants: `UPPER_SNAKE_CASE`
- 2 spaces indentation
- Use `const` by default

### MongoDB
- Collection names: `lowercase_plural` (users, problems)
- Field names: `camelCase`
- IDs: Use MongoDB ObjectId

---

## ⏰ Time Estimates

| Task | Time |
|------|------|
| Setup backend | 30 min |
| Setup frontend | 20 min |
| Understand architecture | 1 hour |
| Implement simple module | 2-3 days |
| Implement complex module | 5-7 days |
| Code review | 30 min - 1 hour |
| Testing (unit + integration) | 1-2 hours per module |

---

## 📞 Quick Help

**Get stuck?**
1. Search in ARCHITECTURE.md
2. Check docs/ folder
3. Ask in Slack #codehub-dev
4. DM Tech Lead if urgent

**Need permission?**
- Modify shared code → Ask Tech Lead in Slack
- Change database schema → Ask Tech Lead
- Update .gitignore or config → Ask Tech Lead

**Want to deploy?**
1. All tests passing ✅
2. Code reviewed & approved ✅
3. Merged to develop ✅
4. Notify Tech Lead ✅

---

## 🎯 Your First Day

1. **Read** (1 hour)
   - docs/SETUP_GUIDE.md
   - ARCHITECTURE.md (your role section)

2. **Setup** (30 min)
   - Clone repo
   - Install dependencies
   - Get backend/frontend running

3. **Explore** (30 min)
   - Open existing code
   - Understand folder structure
   - Find your module

4. **Create branch** (5 min)
   - `git checkout -b feature-your-feature`

5. **First commit** (30 min)
   - Make small change
   - Test it
   - Commit & push

---

## 🏁 Ready?

✅ You now have everything you need!

Next step: `docs/SETUP_GUIDE.md`

