# Full-Stack Integration TODO

## Status: Backend Ready [2/15 ✅]

### 1. Environment Setup ✅
- [x] Created TODO.md
- [x] MongoDB running (localhost:27017) ✓

### 2. Backend Launch 🚀 [EXECUTING]
- [ ] cd backend/platform && mvnw.cmd clean compile
- [x] Configs verified:
  - MongoDB: cutmlearning DB ✓
  - CORS: localhost:5173 ✓
  - JWT secret ✓
  - Port 8080 ✓
- [ ] mvnw.cmd spring-boot:run → Expect /api/auth/login etc.

### 3. Frontend [Pending]
- [ ] npm i axios (frontend)
- [ ] vite.config.ts proxy
- [ ] api.ts + AuthContext real calls
- [ ] Migrate mocks → API (Problems, Courses, etc.)

### 4. Test Flow
- [ ] Student login → problems → submit
- [ ] Admin CRUD users/problems

Progress: 2/15. Launching backend...


