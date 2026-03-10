# CUTM-Learning-Platform

## Configuration Validation

Run the following command from the repository root to validate backend/frontend setup and run tests:

```powershell
./scripts/check-project.ps1
```

What it checks:
- Required backend properties in `backend/platform/src/main/resources/application.properties`
- MongoDB port reachability on `localhost:27017`
- Backend test suite via Maven
- Frontend test suite via npm