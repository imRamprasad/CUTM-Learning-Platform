# Team Workflow & GitHub Best Practices

> A comprehensive guide for 7-member team collaboration using GitHub branch strategy

---

## Table of Contents

1. [Branch Strategy](#branch-strategy)
2. [Feature Development Workflow](#feature-development-workflow)
3. [Pull Request Process](#pull-request-process)
4. [Conflict Resolution](#conflict-resolution)
5. [Code Review Guidelines](#code-review-guidelines)
6. [Communication](#communication)
7. [Release Management](#release-management)

---

## Branch Strategy

### Branch Types

```
main
  ↓ (only for releases)
develop
  ├── feature-auth          (Always has latest from develop)
  ├── feature-profile
  ├── feature-problems
  ├── feature-editor
  ├── feature-submissions
  ├── feature-contest
  └── feature-leaderboard

Temporary branches:
  ├── bugfix/*              (created from develop, merged back)
  ├── hotfix/*              (created from main, merged to both main & develop)
  └── release/*             (used for release preparation)
```

### Branch Naming Convention

```
feature-{feature-name}      # New feature
bugfix-{issue-id}           # Bug fix
hotfix-{issue-id}           # Critical production fix
release/v{version}          # Release branch
docs/{documentation-name}   # Documentation only
```

---

## Feature Development Workflow

### Step 1: Setup Your Local Environment

```powershell
# Clone repository (only once)
git clone https://github.com/CUTM/CUTM-Learning-Platform.git
cd CUTM-Learning-Platform

# Create local tracking branch (each developer)
git checkout develop
git pull origin develop
git checkout -b feature-{your-feature}
```

### Step 2: Daily Development

```powershell
# Before starting work, sync with latest develop
git fetch origin
git rebase origin/develop

# Make your changes
# ... edit files ...

# Stage and commit
git add .
git commit -m "feat: Add login functionality

- Implement login endpoint
- Add JWT token generation
- Add password hashing with bcrypt

Fixes #123"
```

### Step 3: Keep Branch Updated

```powershell
# Multiple times during development
git fetch origin
git rebase origin/develop

# If conflicts occur, resolve them:
# 1. Fix conflicts in your editor
# 2. Stage resolved files
git add .
# 3. Continue rebase
git rebase --continue
```

### Step 4: Push Changes

```powershell
# Push to your feature branch
git push origin feature-{your-feature}

# If you rebased and need to force push (only on your own branch!)
git push origin feature-{your-feature} -f
```

### Step 5: Create Pull Request

```
Title: [FEATURE-NAME] Brief description
Description:
## What does this PR do?
Explain the changes...

## Type of Change
- [ ] New feature
- [ ] Bug fix
- [ ] Documentation update

## How to test?
1. Step 1
2. Step 2

## Checklist
- [ ] Code follows style guidelines
- [ ] Tests added/updated
- [ ] No console warnings/errors
- [ ] Documentation updated
- [ ] Rebased on latest develop
```

---

## Pull Request Process

### For PR Author

**Before creating PR:**
- [ ] Rebase on latest `develop` branch
- [ ] Run all tests locally: `mvn test` (backend), `npm test` (frontend)
- [ ] Run linter: `mvn spotbugs:check` (backend), `npm run lint` (frontend)
- [ ] Add screenshots/videos for UI changes
- [ ] Update relevant documentation
- [ ] Add/update unit tests (min 70% coverage)

**Creating PR:**
- Link related issues: "Closes #123, Relates to #456"
- Assign reviewers (2-3 team members)
- Add labels: `backend`, `frontend`, `documentation`
- Set reviewers as "Request reviewers"

**After creating PR:**
- Fix any failing CI/CD checks
- Respond to review comments promptly
- Request re-review after changes
- Merge only after approval

### For Code Reviewers

**Review Checklist:**
1. **Code Quality**
   - [ ] Code is clean and readable
   - [ ] No commented-out code
   - [ ] No hardcoded values
   - [ ] Proper error handling
   - [ ] Follows code style guidelines

2. **Functionality**
   - [ ] Feature works as described
   - [ ] Edge cases handled
   - [ ] No breaking changes
   - [ ] Backward compatible

3. **Testing**
   - [ ] Tests are present
   - [ ] Tests cover functionality
   - [ ] No test coverage decreased
   - [ ] Tests are passing

4. **Documentation**
   - [ ] Code has appropriate comments
   - [ ] README updated if needed
   - [ ] API docs updated
   - [ ] Database schema documented

5. **Security**
   - [ ] No secrets exposed (keys, passwords)
   - [ ] Input validation present
   - [ ] SQL injection prevention
   - [ ] CORS properly configured

**Leaving Review Comments:**
```
// ✅ Good
Instead of:
"This is wrong"

Use:
"Consider using HashMap instead of nested loops for O(n) performance. 
See line 45 for more context."

// Template for suggestions:
This could be improved by [suggestion]. 
This would [benefit]. 
See [reference] for more details.
```

---

## Conflict Resolution

### Preventing Conflicts

**✅ DO:**
- Keep feature branches focused (1-2 features per branch)
- Update from develop frequently (daily)
- Communicate with teammates working on related features
- Use feature flagging for overlapping changes

**❌ DON'T:**
- Work on the same file as teammate
- Ignore merge conflicts for days
- Force push to develop/main
- Merge without code review

### Resolving Conflicts

```powershell
# When pulling from develop
git pull origin develop
# Result: Conflict!

# View conflicts
git status

# Open conflicted files and look for markers:
<<<<<<< HEAD
  your code
=======
  their code
>>>>>>> feature-profile

# Resolve manually, keep the correct code:
  final code

# Mark as resolved
git add .

# Continue the merge/rebase
git rebase --continue
# or
git commit -m "Merge resolve: feature conflicts"
```

---

## Code Review Guidelines

### Code Style

**Java (Backend)**
```
✅ Use Google Java Style Guide
✅ Naming: camelCase for variables/methods, PascalCase for classes
✅ Max line length: 120 characters
✅ Use 4 spaces for indentation
✅ Add JavaDoc for public methods
```

**JavaScript (Frontend)**
```
✅ Use ESLint + Prettier
✅ Naming: camelCase for variables/functions, PascalCase for components
✅ Use const/let (never var)
✅ Use arrow functions where appropriate
✅ One component per file
```

### Comment Standards

```java
// ❌ Bad
int x = 5; // set x to 5

// ✅ Good
int maxRetries = 5; // Maximum number of retry attempts before failing

// ❌ Bad
// TODO: fix this later

// ✅ Good
// TODO: Fix connection timeout issue (tracked in #456)
// Priority: High
// Owner: @john-doe
```

### Commit Message Format

```
<type>(<scope>): <subject>

<body>

<footer>

Type: feat, fix, docs, style, refactor, test, chore
Scope: module name (auth, problems, contests, etc.)
Subject: What, not how. Imperative mood.

Example:
feat(auth): Implement JWT token refresh endpoint

Add endpoint to refresh expired JWT tokens without re-authentication.
Implements sliding window token rotation for enhanced security.

Fixes #234
Related to #456
```

---

## Communication

### Daily Standup (15 mins)

**Agenda:**
1. What did I complete yesterday?
2. What will I complete today?
3. Any blockers or help needed?

**Format:**
```
[Dev Name] - [Feature]
✅ Completed: ...
🔄 Working on: ...
🚫 Blocker: ... (if any)
```

### Weekly Technical Sync (60 mins)

**Topics:**
- Architecture decisions
- Technical challenges
- Code review discussions
- Integration status
- DevOps/infrastructure

### Right-Now Communication

- **Quick questions**: Slack/Discord
- **Feature discussions**: GitHub Discussions
- **Code questions**: GitHub PR comments
- **Emergencies**: Phone call

### Documentation

- **Architecture decisions**: ADR (Architecture Decision Record)
- **API changes**: Update API docs immediately
- **Database changes**: Update schema docs
- **Process changes**: Update CONTRIBUTING.md

---

## Release Management

### Version Numbering: MAJOR.MINOR.PATCH

```
v1.0.0 (MAJOR.MINOR.PATCH)
v1.2.0 (MAJOR.MINOR.PATCH)
v1.2.3 (MAJOR.MINOR.PATCH)
```

### Release Process

**1. Create Release Branch (1 week before release)**
```powershell
git checkout develop
git pull origin develop
git checkout -b release/v1.0.0
```

**2. Update Version Numbers**
- Backend: `pom.xml`
- Frontend: `package.json`
- CHANGELOG.md

**3. Create Release PR**
- Target: `main`
- Review and approve

**4. Tag Release**
```powershell
git checkout main
git pull origin main
git tag -a v1.0.0 -m "Release version 1.0.0"
git push origin v1.0.0
```

**5. Merge back to develop**
```powershell
git checkout develop
git merge release/v1.0.0
git push origin develop
```

---

## Emergency Hotfix

### When Production is Broken

**1. Create hotfix branch from main**
```powershell
git checkout main
git pull origin main
git checkout -b hotfix/critical-bug-fix
```

**2. Fix the issue**
```powershell
# ... fix code ...
git add .
git commit -m "hotfix: Fix critical authentication bug"
```

**3. Create PR to main (expedited review)**
```
Title: [HOTFIX] Critical bug fix
Priority: Critical
Need: Immediate approval and merge
```

**4. After merging to main, merge back to develop**
```powershell
git checkout develop
git pull origin develop
git merge main
git push origin develop
```

---

## Tools & Automation

### CI/CD Pipeline (GitHub Actions)

```yaml
# .github/workflows/ci.yml
on: [push, pull_request]

jobs:
  test:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v2
      - name: Build
        run: mvn clean package
      - name: Test
        run: mvn test
```

### Branch Protection Rules

**On develop branch:**
- Require pull request reviews: 2
- Require status checks to pass
- Require branches to be up to date before merging
- Require code reviews from code owners
- Require conversation resolution before merging

**On main branch:**
- Require pull request reviews: 3
- Require all status checks
- Dismiss stale pull request approvals
- Require code review from CODEOWNERS

---

## Troubleshooting

### "My branch is behind develop by 50 commits"

```powershell
git fetch origin
git rebase origin/develop
git push origin feature-name -f
```

### "I accidentally pushed to develop"

```powershell
# If not merged yet:
git revert <commit-hash>
git push origin develop

# Or contact team lead for more drastic measures
```

### "I need to update my PR but already have open commits"

```powershell
# Option 1: Squash your commits
git rebase -i HEAD~3  # (for last 3 commits)
# Mark all but first as 'squash'

# Option 2: Force push
git push origin feature-name -f
```

---

## Summary: Developer Quick Reference

```
Daily workflow:
1. git status                    # Check current state
2. git fetch origin              # Get latest updates
3. git rebase origin/develop     # Sync with main branch
4. # Make changes ...
5. git add . && git commit       # Commit with good messages
6. git push origin feature-name  # Push to GitHub
7. Create/update PR              # Open for review
8. Respond to reviews            # Fix issues
9. Get approval                  # 2 approvals required
10. "Squash and merge"           # Merge to develop

Tips:
- Keep feature branches SMALL (< 500 lines changed)
- Merge to develop at least WEEKLY
- Always rebase before creating/updating PR
- Never force push to shared branches (main, develop)
- Communicate with team when working on related features
```

