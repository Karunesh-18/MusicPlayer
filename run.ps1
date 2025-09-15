# PowerShell script to run the MusicPlayer application
# This script sets up the environment and runs the Java application

# Set JAVA_HOME
$env:JAVA_HOME = "C:\Program Files\Eclipse Adoptium\jdk-21.0.8.9-hotspot"

# JEP paths
$JEP_JAR = "C:\Users\karun\AppData\Local\Programs\Python\Python313\Lib\site-packages\jep\jep-4.2.2.jar"
$JEP_LIB = "C:\Users\karun\AppData\Local\Programs\Python\Python313\Lib\site-packages\jep"

Write-Host "Starting MusicPlayer Application..."
Write-Host "Java Home: $env:JAVA_HOME"
Write-Host "JEP JAR: $JEP_JAR"
Write-Host "JEP Library Path: $JEP_LIB"
Write-Host ""

# Compile Java code
Write-Host "Compiling Java code..."
javac -cp "java/src;$JEP_JAR" java/src/Main.java

if ($LASTEXITCODE -eq 0) {
    Write-Host "Compilation successful!"
    Write-Host ""
    
    # Run the application
    Write-Host "Running MusicPlayer..."
    java -cp "java/src;$JEP_JAR" "-Djava.library.path=$JEP_LIB" Main
} else {
    Write-Host "Compilation failed!"
    exit 1
}
