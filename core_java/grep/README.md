# Java Grep Application

This project implements a Java application for text processing using the grep command. The app reads files and searches for specific patterns (such as regular expressions) and outputs matching results to a file. The technologies used include Core Java for the logic, Lambda expressions for more efficient processing, Maven for dependency management, and Docker for containerization and easier distribution. An IDE like IntelliJ IDEA is used for development.

## Technologies Used
This application leverages the following technologies:
- **Java**: Core logic implementation
- **Maven**: Build and dependency management
- **Java Regex APIs**: `java.util.regex` for pattern matching
- **Java IO APIs**: `java.io` for file input and output operations
- **Docker**: Containerization for deployment
- **Logging Frameworks**: `log4j` and `slf4j` for logging
- **IDE**: IntelliJ IDEA (recommended for development)

## Quick Start

To run the application, first ensure Docker is installed on your machine. Then, build the Docker image using Maven and run the container with the following command:

`docker build -t my-grep-app .`

`docker run --rm -v \`pwd\`/data:/data -v \`pwd\`/log:/log my-grep-app '.*Romeo.*Juliet.*' /data /log/grep.out`

This will search for lines containing the pattern "Romeo" and "Juliet" in the files located in the `/data` directory and output the results to `/log/grep.out`.

## Implementation
Pseudocode:
```java
matchedLines = []
for file in listFilesRecursively(rootDir)
  for line in readLines(file)
      if containsPattern(line)
        matchedLines.add(line)
writeToFile(matchedLines)
```

The process method works as follows:

1. Read the file from the input file path using a `BufferedReader`.
2. Initialize the output file for writing using a `BufferedWriter`.
3. Loop through each line in the input file.
4. Check if the line matches the pattern. If it does, write it to the output file.
5. Close the reader and writer resources to avoid memory leaks.

A potential memory issue arises when handling large files, as the entire file may be read into memory. This could be improved by processing the file line-by-line to reduce memory usage. One possible fix is to utilize a streaming approach or buffering to read and process chunks of the file at a time.

## Test

The application was tested by preparing sample data in the `data` directory, which includes text files with lines containing specific patterns. The `process` method was run on this data with the pattern "Romeo" and "Juliet". The output was manually checked in the `grep.out` file to verify correctness.

## Deployment

The application was dockerized using a Dockerfile to package it into a container. The JAR file is copied into the container, and an entry point is specified to run the application. The resulting Docker image can be easily shared and run on any machine with Docker installed.

## Improvement

1. **Improve Memory Efficiency**: Implementing streaming file processing to handle large files without consuming excessive memory.
2. **Add Unit Tests**: Introduce unit tests for individual methods to ensure code quality and reliability.
3. **Build a User Interface**: Create a simple GUI using frameworks like JavaFX or Swing to allow users to input patterns, select files or directories, and view results interactively. This would make the application more accessible to non-technical users.
4. **Provide Pattern Suggestions**: Enhance the GUI or CLI to suggest common patterns or provide an autocomplete feature for building regex queries dynamically.
