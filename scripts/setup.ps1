param(
    [string]$BackendPath = ".\backend\platform",
    [string]$FrontendPath = ".\frontend"
)

Write-Host "`nInstalling frontend dependencies..." -ForegroundColor Cyan
cd $FrontendPath
npm install

Write-Host "`nBuilding backend..." -ForegroundColor Cyan
cd ..\backend\platform
.\mvnw.cmd clean install

Write-Host "`nSetup completed successfully." -ForegroundColor Green