# Demo script for the Console Music Player

Write-Host "=== Console Music Player Demo ===" -ForegroundColor Green
Write-Host ""

# Set up environment
$env:JAVA_HOME = "C:\Program Files\Eclipse Adoptium\jdk-21.0.8.9-hotspot"
$JEP_JAR = "C:\Users\karun\AppData\Local\Programs\Python\Python313\Lib\site-packages\jep\jep-4.2.2.jar"
$JEP_LIB = "C:\Users\karun\AppData\Local\Programs\Python\Python313\Lib\site-packages\jep"

Write-Host "Environment Setup:" -ForegroundColor Yellow
Write-Host "Java Home: $env:JAVA_HOME"
Write-Host "JEP JAR: $JEP_JAR"
Write-Host "JEP Library: $JEP_LIB"
Write-Host ""

# Show current downloads
Write-Host "Current Downloads:" -ForegroundColor Yellow
Get-ChildItem -Path "downloads" -Filter "*.mp3" | ForEach-Object {
    Write-Host "  - $($_.Name)" -ForegroundColor Cyan
}
Write-Host ""

# Test Python modules
Write-Host "Testing Python Backend..." -ForegroundColor Yellow
python python/music_backend/player.py
Write-Host ""

# Compile Java
Write-Host "Compiling Java Application..." -ForegroundColor Yellow
javac -cp "java/src;$JEP_JAR" java/src/Main.java
if ($LASTEXITCODE -eq 0) {
    Write-Host "OK Compilation successful!" -ForegroundColor Green
} else {
    Write-Host "FAIL Compilation failed!" -ForegroundColor Red
    exit 1
}
Write-Host ""

Write-Host "=== Ready to Run Console Music Player ===" -ForegroundColor Green
Write-Host ""
Write-Host "To start the music player, use the run.ps1 script or run:" -ForegroundColor Yellow
Write-Host "java -cp java/src;[JEP_JAR] -Djava.library.path=[JEP_LIB] Main" -ForegroundColor Cyan
Write-Host ""
Write-Host "Features:" -ForegroundColor Yellow
Write-Host "  - Enter song names to search and download" -ForegroundColor White
Write-Host "  - Automatic playback after download" -ForegroundColor White
Write-Host "  - Cross-platform audio support" -ForegroundColor White
Write-Host "  - Type quit to exit" -ForegroundColor White
Write-Host ""
Write-Host "Example song searches:" -ForegroundColor Yellow
Write-Host "  - Bohemian Rhapsody Queen" -ForegroundColor Cyan
Write-Host "  - Never Gonna Give You Up" -ForegroundColor Cyan
Write-Host "  - Imagine John Lennon" -ForegroundColor Cyan
