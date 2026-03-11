# Setup Guide & Configuration

> Complete setup instructions for backend and frontend development

---

## Table of Contents

1. [Prerequisites](#prerequisites)
2. [Backend Setup (Spring Boot + MongoDB)](#backend-setup)
3. [Frontend Setup (React)](#frontend-setup)
4. [Environment Configuration](#environment-configuration)
5. [Running the Project](#running-the-project)
6. [Database Setup](#database-setup)
7. [IDE Configuration](#ide-configuration)
8. [Troubleshooting](#troubleshooting)

---

## Prerequisites

### System Requirements
- **OS**: Windows 10+, macOS 10.12+, or Linux (Ubuntu 18+)
- **RAM**: 8GB minimum (16GB recommended)
- **Disk Space**: 10GB free space

### Required Software

#### Java Development
- **Java JDK 17+**: [Download from Oracle](https://www.oracle.com/java/technologies/downloads/) or use [OpenJDK](https://adoptopenjdk.net/)
- **Maven 3.8+**: [Download Maven](https://maven.apache.org/download.cgi)

#### Node.js & Frontend
- **Node.js 16+**: [Download Node.js](https://nodejs.org/)
- **npm 8+** (comes with Node.js) or **Yarn**

#### Database
- **MongoDB 5.0+**: [Download Community Edition](https://www.mongodb.com/try/download/community)

#### Version Control
- **Git 2.30+**: [Download Git](https://git-scm.com/)

#### IDE (Choose One)
- **IntelliJ IDEA** (Recommended for Java): [Download](https://www.jetbrains.com/idea/download/)
- **VS Code**: [Download](https://code.visualstudio.com/)
- **Eclipse**: [Download](https://www.eclipse.org/downloads/)

---

## Backend Setup

### Step 1: Verify Java Installation

```powershell
java -version
javac -version
```

Expected output:
```
java version "17.0.X" or higher
javac 17.0.X or higher
```

### Step 2: Verify Maven Installation

```powershell
mvn -version
```

Expected output:
```
Apache Maven 3.8.X
```

### Step 3: Clone Repository

```powershell
# Clone the repository
git clone https://github.com/CUTM/CUTM-Learning-Platform.git

# Navigate to backend folder
cd CUTM-Learning-Platform/backend/platform
```

### Step 4: Install Dependencies

```powershell
# Clean and install dependencies
mvn clean install

# Or skip tests for faster installation
mvn clean install -DskipTests
```

### Step 5: Backend Project Structure (pom.xml)

**Key Dependencies to Add:**

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0
         http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>3.0.0</version>
        <relativePath/>
    </parent>

    <groupId>com.cutm</groupId>
    <artifactId>platform</artifactId>
    <version>1.0.0</version>
    <name>CUTM Learning Platform</name>
    <description>Coding learning platform</description>

    <properties>
        <java.version>17</java.version>
        <maven.compiler.source>17</maven.compiler.source>
        <maven.compiler.target>17</maven.compiler.target>
    </properties>

    <dependencies>
        <!-- Spring Boot Starters -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>

        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-data-mongodb</artifactId>
        </dependency>

        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-security</artifactId>
        </dependency>

        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-validation</artifactId>
        </dependency>

        <!-- JWT -->
        <dependency>
            <groupId>io.jsonwebtoken</groupId>
            <artifactId>jjwt-api</artifactId>
            <version>0.11.5</version>
        </dependency>

        <dependency>
            <groupId>io.jsonwebtoken</groupId>
            <artifactId>jjwt-impl</artifactId>
            <version>0.11.5</version>
            <scope>runtime</scope>
        </dependency>

        <dependency>
            <groupId>io.jsonwebtoken</groupId>
            <artifactId>jjwt-jackson</artifactId>
            <version>0.11.5</version>
            <scope>runtime</scope>
        </dependency>

        <!-- Lombok -->
        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
            <optional>true</optional>
        </dependency>

        <!-- Email -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-mail</artifactId>
        </dependency>

        <!-- Testing -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>

        <dependency>
            <groupId>org.springframework.security</groupId>
            <artifactId>spring-security-test</artifactId>
            <scope>test</scope>
        </dependency>

        <dependency>
            <groupId>de.flapdoodle.embed</groupId>
            <artifactId>de.flapdoodle.embed.mongo</artifactId>
            <scope>test</scope>
        </dependency>

        <!-- API Documentation (Swagger/Springdoc) -->
        <dependency>
            <groupId>org.springdoc</groupId>
            <artifactId>springdoc-openapi-starter-webmvc-ui</artifactId>
            <version>2.0.0</version>
        </dependency>

        <!-- Database Migration -->
        <dependency>
            <groupId>de.impress</groupId>
            <artifactId>mongock-spring-boot-starter</artifactId>
            <version>5.2.2</version>
        </dependency>

        <!-- Pagination & Filtering -->
        <dependency>
            <groupId>org.springframework.data</groupId>
            <artifactId>spring-data-rest-webmvc</artifactId>
        </dependency>

        <!-- Code Quality -->
        <dependency>
            <groupId>com.github.spotbugs</groupId>
            <artifactId>spotbugs-annotations</artifactId>
            <version>4.7.1</version>
        </dependency>
    </dependencies>

    <build>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
            </plugin>

            <!-- Code Coverage -->
            <plugin>
                <groupId>org.jacoco</groupId>
                <artifactId>jacoco-maven-plugin</artifactId>
                <version>0.8.8</version>
                <executions>
                    <execution>
                        <goals>
                            <goal>prepare-agent</goal>
                        </goals>
                    </execution>
                    <execution>
                        <id>report</id>
                        <phase>test</phase>
                        <goals>
                            <goal>report</goal>
                        </goals>
                    </execution>
                </executions>
            </plugin>

            <!-- SpotBugs -->
            <plugin>
                <groupId>com.github.spotbugs</groupId>
                <artifactId>spotbugs-maven-plugin</artifactId>
                <version>4.7.1.1</version>
            </plugin>
        </plugins>
    </build>
</project>
```

### Step 6: Create application.properties

**File**: `src/main/resources/application.properties`

```properties
# Server
server.port=8080
server.servlet.context-path=/api

# MongoDB
spring.data.mongodb.uri=mongodb://localhost:27017/codehub
spring.data.mongodb.auto-index-creation=true

# JWT
app.jwt.secret=your-secret-key-change-this-in-production
app.jwt.expiration=86400000
app.jwt.refresh-token-expiration=604800000

# Email
spring.mail.host=smtp.gmail.com
spring.mail.port=587
spring.mail.username=your-email@gmail.com
spring.mail.password=your-app-password
spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.starttls.enable=true

# CORS
app.cors.allowed-origins=http://localhost:3000,http://localhost:3001

# Logging
logging.level.root=INFO
logging.level.com.cutm.platform=DEBUG

# API Documentation
springdoc.api-docs.path=/api-docs
springdoc.swagger-ui.path=/swagger-ui.html

# Development
spring.h2.console.enabled=false
spring.data.mongodb.auto-index-creation=true
```

---

## Frontend Setup

### Step 1: Verify Node.js Installation

```powershell
node --version
npm --version
```

Expected output:
```
v16.X.X or higher
8.X.X or higher
```

### Step 2: Navigate to Frontend Folder

```powershell
cd CUTM-Learning-Platform/frontend
```

### Step 3: Install Dependencies

```powershell
npm install

# Or using yarn
yarn install
```

### Step 4: Create .env File

**File**: `frontend/.env`

```env
REACT_APP_API_URL=http://localhost:8080/api/v1
REACT_APP_ENV=development
REACT_APP_DEBUG=true
```

### Step 5: Update package.json

**Add these scripts to `package.json`:**

```json
{
  "scripts": {
    "start": "react-scripts start",
    "build": "react-scripts build",
    "test": "react-scripts test",
    "eject": "react-scripts eject",
    "lint": "eslint src/",
    "format": "prettier --write src/"
  },
  "devDependencies": {
    "axios": "^1.4.0",
    "react-router-dom": "^6.10.0",
    "eslint": "^8.40.0",
    "prettier": "^2.8.8"
  }
}
```

### Step 6: Proxy Setup for Development

**File**: `frontend/package.json` (add proxy section)

```json
{
  "proxy": "http://localhost:8080",
  ...
}
```

---

## Environment Configuration

### Backend - application-dev.properties

```properties
# Development Profile
server.port=8080

# MongoDB Development
spring.data.mongodb.uri=mongodb://localhost:27017/codehub-dev

# Logging
logging.level.root=INFO
logging.level.com.cutm.platform=DEBUG

# Mail (Use local testing)
spring.mail.host=localhost
spring.mail.port=1025
```

### Backend - application-prod.properties

```properties
# Production Profile
server.port=80

# MongoDB Production
spring.data.mongodb.uri=${MONGODB_URI}

# JWT (Use environment variable)
app.jwt.secret=${JWT_SECRET}

# Mail
spring.mail.host=${MAIL_HOST}
spring.mail.port=${MAIL_PORT}

# Logging
logging.level.root=WARN
logging.level.com.cutm.platform=INFO
```

---

## Running the Project

### Start MongoDB

```powershell
# Windows (if installed as service)
net start MongoDB

# macOS
brew services start mongodb-community

# Linux
sudo systemctl start mongod

# Or if using Docker
docker run -d -p 27017:27017 --name mongodb mongo:latest
```

### Start Backend

```powershell
cd CUTM-Learning-Platform/backend/platform

# Using Maven
mvn spring-boot:run

# Or with specific profile
mvn spring-boot:run -Dspring-boot.run.arguments="--spring.profiles.active=dev"
```

Backend will be available at: `http://localhost:8080`

### Start Frontend

```powershell
cd CUTM-Learning-Platform/frontend

npm start
```

Frontend will be available at: `http://localhost:3000`

---

## Database Setup

### Create Database & Collections

```javascript
// Connect to MongoDB
mongo mongodb://localhost:27017

// Create database
use codehub

// Create collections with validation
db.createCollection("users", {
  validator: {
    $jsonSchema: {
      bsonType: "object",
      required: ["email", "username", "password"],
      properties: {
        email: { bsonType: "string" },
        username: { bsonType: "string" },
        password: { bsonType: "string" }
      }
    }
  }
})

// Create indexes
db.users.createIndex({ email: 1 }, { unique: true })
db.users.createIndex({ username: 1 }, { unique: true })
db.problems.createIndex({ difficulty: 1 })
db.problems.createIndex({ category: 1 })
db.submissions.createIndex({ userId: 1, problemId: 1 })
db.submissions.createIndex({ status: 1 })
```

---

## IDE Configuration

### IntelliJ IDEA

1. **Open Project**
   - File → Open → Select `CUTM-Learning-Platform` folder

2. **Configure SDK**
   - File → Project Structure → Project
   - Set Project SDK to Java 17

3. **Configure Tomcat/Run Configuration**
   - Edit Configurations → + → Maven
   - Working directory: `CUTM-Learning-Platform/backend/platform`
   - Command line: `spring-boot:run`

4. **Enable Lombok**
   - Settings → Plugins → Search "Lombok"
   - Install Lombok Plugin
   - Settings → Build, Execution, Deployment → Compiler → Annotation Processors
   - Enable annotation processing

### VS Code

1. **Install Extensions**
   - Extension Pack for Java
   - Spring Boot Extension Pack
   - REST Client (for API testing)

2. **Configure launch.json**

```json
{
  "version": "0.2.0",
  "configurations": [
    {
      "type": "java",
      "name": "Spring Boot App",
      "request": "launch",
      "mainClass": "com.cutm.platform.PlatformApplication",
      "projectName": "platform",
      "cwd": "${workspaceFolder}/backend/platform",
      "console": "integratedTerminal"
    }
  ]
}
```

---

## Troubleshooting

### Java Version Mismatch

```
Error: java.lang.UnsupportedClassVersionError

Solution: Ensure Java 17+ is installed and set as default
java -version  # Check version
```

### MongoDB Connection Error

```
Error: com.mongodb.MongoSocketOpenException: Exception opening socket

Solution:
1. Ensure MongoDB is running: mongod
2. Check connection string in application.properties
3. Verify port 27017 is not blocked by firewall
```

### Port Already in Use

```
Error: Port 8080 already in use

Solution 1: Kill the process using port 8080
# Windows
netstat -ano | findstr :8080
taskkill /PID <PID> /F

# Linux/Mac
lsof -i :8080
kill -9 <PID>

Solution 2: Change port in application.properties
server.port=8081
```

### Dependencies Not Found

```
Error: [ERROR] Could not find goal 'spring-boot:run'

Solution:
mvn clean install
mvn spring-boot:run
```

### CORS Error

```
Error: Access to XMLHttpRequest has been blocked by CORS policy

Solution: Update application.properties
app.cors.allowed-origins=http://localhost:3000
```

### Node Modules Issues

```
Error: npm ERR! code ERESOLVE

Solution:
rm -rf node_modules package-lock.json
npm install --legacy-peer-deps
```

---

## Useful Commands

### Backend Commands

```powershell
# Build project
mvn clean build

# Run tests
mvn test

# Run specific test
mvn test -Dtest=UserServiceTest

# Check code quality
mvn spotbugs:check

# Create JAR file
mvn package -DskipTests
```

### Frontend Commands

```powershell
# Install dependencies
npm install

# Start development server
npm start

# Build for production
npm build

# Run tests
npm test

# Run linter
npm run lint

# Format code
npm run format
```

### Git Commands

```powershell
# Create feature branch
git checkout develop
git pull origin develop
git checkout -b feature-your-feature

# Commit and push
git add .
git commit -m "feat: your feature description"
git push origin feature-your-feature

# Update from develop
git fetch origin
git rebase origin/develop
```

---

## Development Workflow

```
1. Clone repository
2. Create feature branch from develop
3. Setup backend (Java, Maven, MongoDB)
4. Setup frontend (Node.js, npm)
5. Create .env files
6. Run backend: mvn spring-boot:run
7. Run frontend: npm start
8. Make changes
9. Test locally
10. Commit and push
11. Create PR to develop
12. Request code review
```

---

## Next Steps

- [ ] Complete backend setup
- [ ] Complete frontend setup
- [ ] Setup database
- [ ] Run local backend
- [ ] Run local frontend
- [ ] Create feature branch
- [ ] Make first commit
- [ ] Create pull request

