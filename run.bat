@echo off

REM Check if Java is available and the correct version
java -version >nul 2>&1
if %ERRORLEVEL% NEQ 0 (
    echo Error: Java not found in PATH
    exit /b 1
)

REM Check Java version
for /f "tokens=3" %%g in ('java -version 2^>^&1 ^| findstr /i "version"') do (
    set JAVA_VERSION=%%g
)

set JAVA_VERSION=%JAVA_VERSION:"=%
for /f "tokens=1,2 delims=." %%a in ("%JAVA_VERSION%") do (
    set JAVA_MAJOR=%%a
)

if %JAVA_MAJOR% LSS 17 (
    echo Error: Java 17 or higher is required, but found version %JAVA_VERSION%
    exit /b 1
)

REM Check if an argument was provided
if "%~1"=="" (
    echo Error: No image directory specified
    echo Usage: run.bat ^<image_directory^>
    exit /b 1
)

REM Validate that the directory exists
if not exist "%~1\" (
    echo Error: Directory '%~1' does not exist
    exit /b 1
)

REM Compile and run the Java program with the specified directory
javac -d target src/main/java/org/example/Main.java
java -cp target org.example.Main "%~1"