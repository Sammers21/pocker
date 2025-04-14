@echo off

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