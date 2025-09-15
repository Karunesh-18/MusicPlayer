# Test script to verify all components are working

Write-Host "=== MusicPlayer Component Test ===" -ForegroundColor Green
Write-Host ""

# Test 1: Python Backend
Write-Host "1. Testing Python Backend..." -ForegroundColor Yellow
python python/music_backend/main.py
if ($LASTEXITCODE -eq 0) {
    Write-Host "OK Python backend working" -ForegroundColor Green
} else {
    Write-Host "FAIL Python backend failed" -ForegroundColor Red
}
Write-Host ""

# Test 2: Python Dependencies
Write-Host "2. Testing Python Dependencies..." -ForegroundColor Yellow
python -c "import spotdl, yt_dlp, dotenv; print('All Python dependencies imported successfully')"
if ($LASTEXITCODE -eq 0) {
    Write-Host "OK Python dependencies working" -ForegroundColor Green
} else {
    Write-Host "FAIL Python dependencies failed" -ForegroundColor Red
}
Write-Host ""

# Test 3: FFmpeg
Write-Host "3. Testing FFmpeg..." -ForegroundColor Yellow
spotdl --help | Out-Null
if ($LASTEXITCODE -eq 0) {
    Write-Host "OK FFmpeg and spotdl working" -ForegroundColor Green
} else {
    Write-Host "FAIL FFmpeg or spotdl failed" -ForegroundColor Red
}
Write-Host ""

# Test 4: Java Compilation
Write-Host "4. Testing Java Compilation..." -ForegroundColor Yellow
$env:JAVA_HOME = "C:\Program Files\Eclipse Adoptium\jdk-21.0.8.9-hotspot"
$JEP_JAR = "C:\Users\karun\AppData\Local\Programs\Python\Python313\Lib\site-packages\jep\jep-4.2.2.jar"
javac -cp "java/src;$JEP_JAR" java/src/Main.java
if ($LASTEXITCODE -eq 0) {
    Write-Host "OK Java compilation working" -ForegroundColor Green
} else {
    Write-Host "FAIL Java compilation failed" -ForegroundColor Red
}
Write-Host ""

# Test 5: Python Downloader Module
Write-Host "5. Testing Python Downloader Module..." -ForegroundColor Yellow
python python/music_backend/downloader.py
if ($LASTEXITCODE -eq 0) {
    Write-Host "OK Python downloader working" -ForegroundColor Green
} else {
    Write-Host "FAIL Python downloader failed" -ForegroundColor Red
}
Write-Host ""

# Test 6: Java-Python Integration
Write-Host "6. Testing Java-Python Integration..." -ForegroundColor Yellow
$JEP_LIB = "C:\Users\karun\AppData\Local\Programs\Python\Python313\Lib\site-packages\jep"
java -cp "java/src;$JEP_JAR" "-Djava.library.path=$JEP_LIB" Main
if ($LASTEXITCODE -eq 0) {
    Write-Host "OK Java-Python integration working" -ForegroundColor Green
} else {
    Write-Host "FAIL Java-Python integration failed" -ForegroundColor Red
}
Write-Host ""

Write-Host "=== Test Complete ===" -ForegroundColor Green
