# Tution App

This is a Tution Management App built with Android. It helps manage students, teachers, classes, and attendance.

## Features

- Student and Teacher Management
- Class Scheduling
- Attendance Tracking
- QR Code Generation and Scanning
- Firebase Authentication and Database Integration

## Project Structure

```
.gitignore
.gradle/
app/
    .gitignore
    app.iml
    build/
    build.gradle
    google-services.json
    libs/
    proguard-rules.pro
    src/
build.gradle
gradle/
gradle.properties
gradlew
gradlew.bat
local.properties
Presentation.pptx
settings.gradle
Tution.iml
```

## Getting Started

### Prerequisites

- Android Studio
- Java Development Kit (JDK)
- Firebase Account

### Installation

1. Clone the repository:
    ```sh
    git clone https://github.com/yourusername/tution-app.git
    ```
2. Open the project in Android Studio.
3. Sync the project with Gradle files.
4. Add your `google-services.json` file to the 

app

 directory.

### Building the Project

To build the project, run the following command in the terminal:
```sh
./gradlew build
```

### Running the Project

To run the project on an emulator or a physical device, use the following command:
```sh
./gradlew installDebug
```

## Dependencies

- Firebase Authentication
- Firebase Database
- QR Code Scanner
- QR Code Generator
- Android Support Libraries
