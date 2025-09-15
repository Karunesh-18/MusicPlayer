# PowerShell script to test download functionality with debugging
# This script focuses specifically on testing the download components

Write-Host "=== Download Functionality Test ===" -ForegroundColor Green
Write-Host ""

# Create logs directory if it doesn't exist
if (!(Test-Path "logs")) {
    New-Item -ItemType Directory -Path "logs" | Out-Null
    Write-Host "Created logs directory" -ForegroundColor Yellow
}

# Test 1: Check Python and required modules
Write-Host "1. Checking Python and required modules..." -ForegroundColor Yellow
python -c "
import sys
print(f'Python version: {sys.version}')
print(f'Python executable: {sys.executable}')

try:
    import spotdl
    print('✓ spotdl imported successfully')
except ImportError as e:
    print(f'✗ spotdl import failed: {e}')

try:
    import yt_dlp
    print('✓ yt_dlp imported successfully')
except ImportError as e:
    print(f'✗ yt_dlp import failed: {e}')

try:
    from dotenv import load_dotenv
    print('✓ python-dotenv imported successfully')
except ImportError as e:
    print(f'✗ python-dotenv import failed: {e}')
"
Write-Host ""

# Test 2: Check spotdl version and basic functionality
Write-Host "2. Testing spotdl version and basic functionality..." -ForegroundColor Yellow
python -m spotdl --version
if ($LASTEXITCODE -eq 0) {
    Write-Host "✓ spotdl version check passed" -ForegroundColor Green
} else {
    Write-Host "✗ spotdl version check failed" -ForegroundColor Red
}
Write-Host ""

# Test 3: Check yt-dlp version
Write-Host "3. Testing yt-dlp version..." -ForegroundColor Yellow
python -m yt_dlp --version
if ($LASTEXITCODE -eq 0) {
    Write-Host "✓ yt-dlp version check passed" -ForegroundColor Green
} else {
    Write-Host "✗ yt-dlp version check failed" -ForegroundColor Red
}
Write-Host ""

# Test 4: Check FFmpeg
Write-Host "4. Testing FFmpeg..." -ForegroundColor Yellow
ffmpeg -version | Select-Object -First 1
if ($LASTEXITCODE -eq 0) {
    Write-Host "✓ FFmpeg is available" -ForegroundColor Green
} else {
    Write-Host "✗ FFmpeg is not available or not in PATH" -ForegroundColor Red
    Write-Host "  Make sure FFmpeg is installed and added to your PATH" -ForegroundColor Yellow
}
Write-Host ""

# Test 5: Test network connectivity
Write-Host "5. Testing network connectivity..." -ForegroundColor Yellow
try {
    $response = Invoke-WebRequest -Uri "https://www.youtube.com" -Method Head -TimeoutSec 10 -UseBasicParsing
    Write-Host "✓ YouTube is accessible" -ForegroundColor Green
} catch {
    Write-Host "✗ Cannot access YouTube: $($_.Exception.Message)" -ForegroundColor Red
}

try {
    $response = Invoke-WebRequest -Uri "https://open.spotify.com" -Method Head -TimeoutSec 10 -UseBasicParsing
    Write-Host "✓ Spotify is accessible" -ForegroundColor Green
} catch {
    Write-Host "✗ Cannot access Spotify: $($_.Exception.Message)" -ForegroundColor Red
}
Write-Host ""

# Test 6: Run the comprehensive debug script
Write-Host "6. Running comprehensive download debug..." -ForegroundColor Yellow
Write-Host "This may take a few minutes..." -ForegroundColor Cyan
python debug_download.py
if ($LASTEXITCODE -eq 0) {
    Write-Host "✓ Debug script completed successfully" -ForegroundColor Green
} else {
    Write-Host "✗ Debug script failed" -ForegroundColor Red
}
Write-Host ""

# Test 7: Test the downloader module directly
Write-Host "7. Testing downloader module directly..." -ForegroundColor Yellow
python python/music_backend/downloader.py
if ($LASTEXITCODE -eq 0) {
    Write-Host "✓ Downloader module test passed" -ForegroundColor Green
} else {
    Write-Host "✗ Downloader module test failed" -ForegroundColor Red
}
Write-Host ""

# Test 8: Quick download test with a very simple query
Write-Host "8. Quick download test..." -ForegroundColor Yellow
Write-Host "Testing with a simple query (this will create a test download)..." -ForegroundColor Cyan

# Create a temporary test directory
$testDir = "test_download_$(Get-Date -Format 'HHmmss')"
New-Item -ItemType Directory -Path $testDir | Out-Null

python -c "
import sys
import os
sys.path.insert(0, os.path.join('python', 'music_backend'))
from downloader import search_and_download

print('Testing download with simple query...')
success = search_and_download('test song', '$testDir')
if success:
    print('✓ Download function executed without errors')
else:
    print('✗ Download function reported failure')
"

# Check if any files were created
$files = Get-ChildItem -Path $testDir -ErrorAction SilentlyContinue
if ($files) {
    Write-Host "✓ Files were created in test directory:" -ForegroundColor Green
    $files | ForEach-Object { Write-Host "  - $($_.Name) ($($_.Length) bytes)" -ForegroundColor Cyan }
} else {
    Write-Host "✗ No files were created in test directory" -ForegroundColor Red
}

# Clean up test directory
Remove-Item -Path $testDir -Recurse -Force -ErrorAction SilentlyContinue
Write-Host ""

# Summary
Write-Host "=== Download Test Summary ===" -ForegroundColor Green
Write-Host ""
Write-Host "If downloads are still failing after this test:" -ForegroundColor Yellow
Write-Host "1. Check the logs directory for detailed debug information" -ForegroundColor White
Write-Host "2. Ensure all dependencies are properly installed:" -ForegroundColor White
Write-Host "   - pip install spotdl yt-dlp python-dotenv" -ForegroundColor Cyan
Write-Host "3. Make sure FFmpeg is installed and in your PATH" -ForegroundColor White
Write-Host "4. Check your internet connection and firewall settings" -ForegroundColor White
Write-Host "5. Try running individual commands manually to isolate the issue" -ForegroundColor White
Write-Host ""
Write-Host "For more detailed debugging, run: python debug_download.py" -ForegroundColor Cyan
