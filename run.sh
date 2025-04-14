#!/bin/bash

# Check if an argument was provided
if [ $# -eq 0 ]; then
    echo "Error: No image directory specified"
    echo "Usage: ./run.sh <image_directory>"
    exit 1
fi

# Validate that the directory exists
if [ ! -d "$1" ]; then
    echo "Error: Directory '$1' does not exist"
    exit 1
fi

# Compile and run the Java program with the specified directory
javac -d target src/main/java/org/example/Main.java
java -cp target org.example.Main "$1"