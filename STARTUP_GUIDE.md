# 🚀 CUTM Learning Platform - Project Startup Guide

## Welcome to CodeHub! 🎓

You are about to be part of building an amazing **LeetCode-like coding learning platform**. This guide will get you started in the next **15 minutes**.

---

## 📋 What You'll Do Today

1. ✅ Clone the repository
2. ✅ Checkout your feature branch
3. ✅ Set up your environment
4. ✅ Run the application
5. ✅ Make your first commit
6. ✅ Create your first PR

**Total time: 15-30 minutes** (depending on system speed)

---

## 🎯 Step 1: Clone the Repository (2 min)

```bash
# Clone from GitHub
git clone https://github.com/imRamprasad/CUTM-Learning-Platform.git

# Navigate to project directory
cd CUTM-Learning-Platform

# Verify you're on develop branch
git branch
# You should see: * develop
```

---

## 👤 Step 2: Checkout Your Feature Branch (1 min)

**Dev 1 - Authentication:**
```bash
git checkout feature/dev1-authentication
git pull origin feature/dev1-authentication
```

**Dev 2 - Problems:**
```bash
git checkout feature/dev2-problems
git pull origin feature/dev2-problems
```

**Dev 3 - Submissions:**
```bash
git checkout feature/dev3-submissions
git pull origin feature/dev3-submissions
```

**Dev 4 - Contests:**
```bash
git checkout feature/dev4-contests
git pull origin feature/dev4-contests
```

**Dev 5 - Dashboard & Profile:**
```bash
git checkout feature/dev5-dashboard-profile
git pull origin feature/dev5-dashboard-profile
```

**Dev 6 - Problems Frontend:**
```bash
git checkout feature/dev6-problems-frontend
git pull origin feature/dev6-problems-frontend
```

**Dev 7 - Leaderboard & Discussions:**
```bash
git checkout feature/dev7-leaderboard-discussions
git pull origin feature/dev7-leaderboard-discussions
```

---

## 🔧 Step 3: Setup Your Environment

### For All Developers (5 min)

**1. Open SETUP_GUIDE.md:**
```bash
# From the project root
cat docs/SETUP_GUIDE.md
```

**2. Follow the appropriate section:**
- **Backend Developers (Dev 1-4)**: Follow "Backend Setup" section
- **Frontend Developers (Dev 5-7)**: Follow "Frontend Setup" section

### Backend Setup Quick Start (10 min)

```bash
# Check Java version (should be 17+)
java -version

# Check Maven version (should be 3.8+)
mvn -version

# Install Java 17 if not present
# For Windows: Download from https://adoptium.net/
# For Mac: brew install java17
# For Linux: sudo apt-get install openjdk-17-jdk

# Build backend
cd backend/platform
mvn clean install

# Wait for download... (might take 2-3 minutes)
```

### Frontend Setup Quick Start (10 min)

```bash
# Check Node version (should be 16+)
node -v

# Check npm version
npm -v

# Install Node if not present
# Visit: https://nodejs.org/

# Install dependencies
cd frontend
npm install

# Wait for installation... (might take 2-3 minutes)
```

---

## ▶️ Step 4: Run the Application

### Backend (Terminal 1)

```bash
cd backend/platform
mvn spring-boot:run

# You should see:
# "Started PlatformApplication in X seconds"
# "Listening on: http://localhost:8080"
```

### Frontend (Terminal 2)

```bash
cd frontend
npm start

# You should see:
# "Compiled successfully!"
# "You can now view the application in the browser."
# Browser opens to: http://localhost:3000
```

### Test If Everything Works

**Backend Test:**
```bash
curl http://localhost:8080/api/health
# Expected: {"status":"UP","components":{"mongodb":{"status":"UP"}}}
```

**Frontend Test:**
- Open browser: `http://localhost:3000`
- You should see the CodeHub landing page

---

## 💾 Step 5: Your First Commit

### 1. Create a placeholder file in your module:

**Dev 1** (Authentication):
```bash
# Create a simple Java class
cat > backend/platform/src/main/java/com/cutm/platform/controllers/AuthController.java << 'EOF'
package com.cutm.platform.controllers;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    // TODO: Implement authentication endpoints
}
EOF
```

**Dev 2** (Problems):
```bash
cat > backend/platform/src/main/java/com/cutm/platform/controllers/ProblemController.java << 'EOF'
package com.cutm.platform.controllers;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
@RequestMapping("/api/problems")
public class ProblemController {
    // TODO: Implement problem endpoints
}
EOF
```

**Dev 5** (Dashboard Frontend - dev5 and beyond):
```bash
cat > frontend/src/components/Dashboard/Dashboard.js << 'EOF'
import React from 'react';

export default function Dashboard() {
  return <div>Dashboard Component</div>;
}
EOF
```

### 2. Stage and commit:

```bash
# Stage your changes
git add .

# Create commit (use proper message)
git commit -m "feat: Add initial module structure for [your module]

- Added [file name]
- Ready for implementation
- Following architecture guidelines from ARCHITECTURE.md"

# Verify commit
git log --oneline -n 2
```

### 3. Push your branch:

```bash
# Push to your feature branch
git push origin $(git rev-parse --abbrev-ref HEAD)

# Example output:
# To https://github.com/imRamprasad/CUTM-Learning-Platform.git
#  * [new branch]      feature/dev1-authentication -> feature/dev1-authentication
```

---

## 🔄 Step 6: Create Your First Pull Request

### In GitHub:

1. Go to: https://github.com/imRamprasad/CUTM-Learning-Platform
2. Click "Pull Requests" tab
3. Click "New Pull Request"
4. Select:
   - Base: `develop`
   - Compare: `feature/dev[#]-[module]`
5. Fill in the PR template (it's auto-populated)
6. Click "Create Pull Request"

### PR Template Fields to Complete:

```markdown
## 📝 Description
Initial module structure for [Your Module Name]

## 🎯 Type of Change
- [x] New feature (skeleton implementation)

## 🔗 Related Issues
Part of: CodeHub Platform

## ✅ Testing
- [x] Code structure verified

## 📋 Checklist
- [x] Following ARCHITECTURE.md
- [x] Files in correct package
- [x] No conflicts with other modules
- [x] Code owner assigned
```

### Wait for Review

- **Code Owner** will review within 4 hours
- Make any requested changes
- Once approved, **code owner merges** the PR

---

## 📚 Next Steps After First Commit

1. **Read the Architecture** (20 min)
   ```bash
   cat ARCHITECTURE.md | head -100
   ```

2. **Read Your Module Section** (15 min)
   - Dev 1: "Backend > Auth Module"
   - Dev 2: "Backend > Problems Module"
   - etc.

3. **Review Your Starter Code** (20 min)
   ```bash
   cat docs/STARTER_CODE_BACKEND.md
   # or
   cat docs/STARTER_CODE_FRONTEND.md
   ```

4. **Review API Contracts** with team in Slack
   - Discuss endpoint design
   - Agree on request/response formats
   - Document in API_DOCUMENTATION.md (coming soon)

5. **Start Implementation**
   - Reference QUICK_REFERENCE.md for git commands
   - Make small commits (one feature per commit)
   - Create PR once ready for review

---

## ⚡ Quick Troubleshooting

### Java/Maven Issues

```bash
# Java not found
java -version
# If not installed: https://adoptium.net/ (install JDK 17)

# Maven not found
mvn -version
# If not installed: Download from https://maven.apache.org/

# Maven cache issues
mvn clean install -U
```

### Node/npm Issues

```bash
# Node not found
node -v
# If not installed: https://nodejs.org/ (install LTS 18+)

# npm cache issues
npm cache clean --force
npm install

# Port 3000 already in use
npm start -- --port 3001
```

### Git Issues

```bash
# Branch not tracking
git branch -u origin/feature/dev1-authentication feature/dev1-authentication

# Merge conflict (ask your code owner)
git status  # See conflicts
# Fix conflicts manually
# Then: git add . && git commit -m "Resolve merge conflict"
```

### MongoDB Connection Issues

```bash
# Start MongoDB (if using local)
# Windows: net start MongoDB
# Mac: brew services start mongodb-community
# Linux: sudo systemctl start mongod

# Check if running
curl http://localhost:27017
```

---

## 📞 Getting Help

### Quick Answers (5 min)
1. **QUICK_REFERENCE.md** - Search for your issue
2. **Check SETUP_GUIDE.md** - Common problems section

### Moderate Help (15 min)
1. **Read docs/** folder
2. **Search ARCHITECTURE.md**
3. **Check DEVELOPER_HANDBOOK.md** - Conflict scenarios

### Need More Help (Team Slack)
```
@YourCodeOwner I'm stuck on [issue]. 
Here's what I tried: [paste error message]

Reference: [relevant doc section]
```

---

## ✅ Verification Checklist

- [ ] Repository cloned
- [ ] Feature branch checked out
- [ ] Java 17 installed (backend devs)
- [ ] Node 18+ installed (frontend devs)
- [ ] Maven works: `mvn -version`
- [ ] npm works: `npm -version`
- [ ] Backend runs without errors
- [ ] Frontend runs without errors
- [ ] Made first commit
- [ ] Created first PR
- [ ] PR approved by code owner
- [ ] PR merged to develop

**Once all checked: You're officially ready to start development!** 🎉

---

## 🎓 Learning Resources

If you're new to any technology, check these:

### Backend (Java/Spring Boot)
- **Spring Boot**: https://spring.io/guides/gs/spring-boot/
- **MongoDB**: https://docs.mongodb.com/
- **JWT Auth**: https://tools.ietf.org/html/rfc7519
- **REST API**: https://restfulapi.net/

### Frontend (React/JavaScript)
- **React**: https://react.dev/
- **React Router**: https://reactrouter.com/
- **Axios**: https://axios-http.com/
- **JavaScript**: https://developer.mozilla.org/en-US/docs/Web/JavaScript

### Tools
- **Git**: https://git-scm.com/doc
- **GitHub**: https://docs.github.com/
- **VS Code**: https://code.visualstudio.com/docs
- **IntelliJ IDEA**: https://www.jetbrains.com/help/idea/

---

## 📝 Daily Routine

### Morning Standup (5 min)
```bash
# Get latest code
git fetch origin
git pull origin develop

# Check if feature branch needs rebase
git rebase develop
# (Resolve conflicts if any - ask code owner for help)
```

### During Day
- Keep VS Code/IntelliJ open
- Make small commits (one feature per commit)
- Test your changes locally
- Push to feature branch multiple times

### Before EOD
```bash
# Final commit
git add .
git commit -m "feat: Add [specific feature]"

# Push branch
git push origin $(git rev-parse --abbrev-ref HEAD)

# After 30-40% complete, create PR
# Code owner reviews
# You fix feedback
# Code owner merges
# Repeat for next feature
```

---

## 🚀 Week 1 Goals

### Dev 1-4 (Backend)
- [ ] Understand folder structure
- [ ] Setup development environment
- [ ] Implement model classes for your module
- [ ] Implement repository interfaces
- [ ] Create 1-2 REST endpoints
- [ ] Create unit tests

### Dev 5-7 (Frontend)
- [ ] Understand folder structure
- [ ] Setup development environment
- [ ] Create component structure
- [ ] Implement 2-3 components
- [ ] Connect to API (via mocks if backend not ready)
- [ ] Create basic styling

### Everyone
- [ ] Read ARCHITECTURE.md
- [ ] Understand CODEOWNERS
- [ ] Participate in team syncs
- [ ] Review 1 PR from teammate

---

## 🎉 Welcome Aboard!

You're now part of the CodeHub team. Let's build something amazing together!

**Questions?** Check QUICK_REFERENCE.md or Slack your code owner.

**Ready to code?** Your feature branch is waiting! 

**Last thing:** Follow the commit message format from TEAM_WORKFLOW.md for clean history.

Happy Coding! 🚀

