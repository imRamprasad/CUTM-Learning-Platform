param(
    [string]$BackendPath = ".\\backend\\platform",
    [string]$FrontendPath = ".\\frontend",
    [string]$MongoHost = "localhost",
    [int]$MongoPort = 27017
)

$ErrorActionPreference = "Stop"

function Write-Step {
    param([string]$Message)
    Write-Host "`n==> $Message" -ForegroundColor Cyan
}

function Run-Command {
    param(
        [string]$WorkingDirectory,
        [string]$Command,
        [string[]]$Arguments
    )

    Push-Location $WorkingDirectory
    try {
        & $Command @Arguments
        if ($LASTEXITCODE -ne 0) {
            throw "Command failed: $Command $($Arguments -join ' ')"
        }
    }
    finally {
        Pop-Location
    }
}

Write-Step "Checking backend application.properties"
$backendPropsFile = Join-Path $BackendPath "src/main/resources/application.properties"
if (-not (Test-Path $backendPropsFile)) {
    throw "Missing backend properties file: $backendPropsFile"
}

$propsContent = Get-Content -LiteralPath $backendPropsFile -Raw
if ($propsContent -notmatch "spring\.application\.name\s*=\s*.+") {
    throw "spring.application.name is missing in application.properties"
}
if ($propsContent -notmatch "server\.port\s*=\s*\d+") {
    throw "server.port is missing or invalid in application.properties"
}
if ($propsContent -notmatch "spring\.data\.mongodb\.uri\s*=\s*mongodb(\+srv)?://.+") {
    throw "spring.data.mongodb.uri is missing or invalid in application.properties"
}
Write-Host "Backend configuration keys look valid." -ForegroundColor Green

Write-Step "Checking MongoDB port availability ($MongoHost`:$MongoPort)"
$mongoReachable = Test-NetConnection -ComputerName $MongoHost -Port $MongoPort -InformationLevel Quiet
if (-not $mongoReachable) {
    Write-Warning "MongoDB is not reachable at $MongoHost`:$MongoPort. Backend tests may still pass if DB is not required at startup."
}
else {
    Write-Host "MongoDB port is reachable." -ForegroundColor Green
}

Write-Step "Running backend tests (Maven)"
Run-Command -WorkingDirectory $BackendPath -Command ".\\mvnw.cmd" -Arguments @("test")
Write-Host "Backend tests passed." -ForegroundColor Green

Write-Step "Running frontend tests (React)"
Push-Location $FrontendPath
try {
    $env:CI = "true"
    & npm test -- --ci --watch=false --runInBand
    if ($LASTEXITCODE -ne 0) {
        throw "Frontend tests failed"
    }
}
finally {
    Pop-Location
}
Write-Host "Frontend tests passed." -ForegroundColor Green

Write-Step "Project configuration checks completed successfully"
Write-Host "All configured checks completed." -ForegroundColor Green
