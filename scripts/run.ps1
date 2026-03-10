param(
    [string]$BackendPath = ".\backend\platform",
    [string]$FrontendPath = ".\frontend"
)

$ErrorActionPreference = "Stop"

function Write-Step {
    param([string]$Message)
    Write-Host "`n==> $Message" -ForegroundColor Cyan
}

function Check-Folder {
    param([string]$Path)
    if (-not (Test-Path $Path)) {
        throw "Folder not found: $Path"
    }
}

Write-Step "Checking project folders"

Check-Folder $BackendPath
Check-Folder $FrontendPath

Write-Host "Project folders OK" -ForegroundColor Green

Write-Step "Starting backend (Spring Boot)"

Start-Process powershell -ArgumentList "-NoExit","-Command","cd $BackendPath; .\mvnw.cmd spring-boot:run"

Write-Host "Backend starting on http://localhost:8080" -ForegroundColor Green

Write-Step "Starting frontend (React)"

Start-Process powershell -ArgumentList "-NoExit","-Command","cd $FrontendPath; npm start"

Write-Host "Frontend starting on http://localhost:3000" -ForegroundColor Green

Write-Step "Project started successfully"