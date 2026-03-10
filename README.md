# CUTM Learning Platform

## Project Overview

The **CUTM Learning Platform** is a coding learning and practice platform built using:

* **Frontend:** React
* **Backend:** Spring Boot
* **Database:** MongoDB
* **Version Control:** Git & GitHub
* **Team Size:** 7 Members

The platform allows students to **learn, practice coding problems, participate in contests, and view leaderboards**.

---

# Project Structure

```
CUTM-Learning-Platform
│
├── backend/
│   └── platform/
│       ├── src/
│       ├── pom.xml
│       ├── mvnw
│       └── mvnw.cmd 
│
├── frontend/
│   ├── src/
│   ├── public/
│   └── package.json
│
├── scripts/
│   ├── setup.ps1
│   └── run.ps1
│
├── docs/
└── README.md
```

---

# Technologies Used

Frontend

* React
* JavaScript
* HTML/CSS

Backend

* Spring Boot
* Spring Security
* REST APIs

Database

* MongoDB

Dev Tools

* Git
* GitHub
* Maven Wrapper

---

# Git Branch Strategy

The repository follows a **feature-based branching model**.

Main Branches:

```
main
develop
```

Feature Branches:

```
feature-auth
feature-profile
feature-problems
feature-editor
feature-submissions
feature-contest
feature-leaderboard
```

Workflow:

```
feature branch → pull request → develop → main
```

---

# Team Module Distribution

Authentication Module
Handles user registration, login, and JWT authentication.

User Profile Module
Manages student information, rank, and submission history.

Problem Management Module
Stores coding problems and test cases.

Code Editor Module
Provides an interface for writing and editing code.

Submission Module
Tracks code submissions and results.

Contest Module
Manages coding contests.

Leaderboard Module
Displays ranking of students.

---

# Problems Faced During Setup

## Git Pull Error

Error:

```
There is no tracking information for the current branch
```

Cause:
The **develop branch was not linked to the remote branch**.

Solution:

```
git push -u origin develop
```

This command sets the **upstream tracking branch**.

---

# Important Git Commands

Check branches

```
git branch
```

Switch branch

```
git checkout develop
```

Pull latest changes

```
git pull
```

Push changes

```
git push
```

Set upstream branch

```
git push -u origin develop
```

Fetch remote branches

```
git fetch
```

---

# Automatic Setup Scripts

To make the project easy for teammates, two scripts were created.

## Setup Script

Location:

```
scripts/setup.ps1
```

Purpose:

* Install frontend dependencies
* Build backend project

Command:

```
powershell -ExecutionPolicy Bypass -File scripts/setup.ps1
```

This runs:

```
npm install
.\mvnw.cmd clean install
```

---

# Run Script

Location:

```
scripts/run.ps1
```

Purpose:

Start both backend and frontend automatically.

Command:

```
powershell -ExecutionPolicy Bypass -File scripts/run.ps1
```

This will:

* Start Spring Boot backend
* Start React frontend
* Open two terminals

Backend runs on:

```
http://localhost:8080
```

Frontend runs on:

```
http://localhost:3000
```

---

# How Teammates Run the Project

Step 1: Clone repository

```
git clone https://github.com/imRamprasad/CUTM-Learning-Platform.git
```

Step 2: Enter project folder

```
cd CUTM-Learning-Platform
```

Step 3: Setup dependencies

```
powershell -ExecutionPolicy Bypass -File scripts/setup.ps1
```

Step 4: Run the project

```
powershell -ExecutionPolicy Bypass -File scripts/run.ps1
```

---

# Future Improvements

* Docker containerization
* Docker Compose setup
* CI/CD pipeline
* Code execution sandbox
* Contest ranking improvements
* Notification system

---

# Project Modules Overview

Authentication
User login and security

User Profile
Student details and statistics

Problem Management
Coding questions and test cases

Code Editor
Write and edit code

Submission System
Track submissions

Contest System
Coding competitions

Leaderboard
Ranking system

---

# Role of Project Manager / DevOps

Responsibilities:

* Manage GitHub repository
* Maintain branch structure
* Review pull requests
* Maintain setup scripts
* Integrate Docker later

---

# License

This project is developed for academic learning purposes at **CUTM University**.
