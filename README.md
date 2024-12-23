# How to Run the Application

This guide explains the steps to set up and run the application on your computer. Please ensure that all prerequisites are met before proceeding.

---

## Prerequisites
1. **Java Development Kit (JDK)**
    - Ensure JDK 11 or higher is installed.
    - [Download JDK](https://www.oracle.com/java/technologies/javase-downloads.html)

2. **Neo4j Database**
    - Install Neo4j Community Edition or use an existing instance.
    - [Download Neo4j](https://neo4j.com/download-center/)

3. **Gradle** (optional if the project includes a wrapper)
    - Ensure Gradle is installed if not using the provided `gradlew` wrapper.
    - [Install Gradle](https://gradle.org/install/)

4. **IDE**
    - Use IntelliJ IDEA or another compatible Kotlin IDE.
    - [Download IntelliJ IDEA](https://www.jetbrains.com/idea/download/)

5. **Dependencies**
    - Ensure all dependencies are defined in `build.gradle.kts` or `pom.xml`.

---

## Setup Instructions

### 1. Clone the Repository
```bash
git clone <repository-url>
cd <repository-folder>
```

### 2. Configure the Neo4j Database
- Start the Neo4j database instance.
- Update the `application.properties` file (or `application.yml`) with the following:
  ```properties
  spring.neo4j.uri=bolt://<host>:<port>
  spring.neo4j.authentication.username=<username>
  spring.neo4j.authentication.password=<password>
  ```

### 3. Build the Project
If a Gradle wrapper is included, run:
```bash
./gradlew build
```
If using standalone Gradle:
```bash
gradle build
```

### 4. Run the Application
If using the Gradle wrapper:
```bash
./gradlew bootRun
```
Alternatively, run from your IDE by selecting the `Application` class and clicking `Run`.

### 5. Access the Application
- Open a browser and navigate to:
  ```
  http://localhost:<port>/swagger-ui/index.html
  ```
  Replace `<port>` with the port specified in the `application.properties` file (default: `8080`).

---

## Troubleshooting
- **Port Conflicts**: If the port is already in use, modify the `server.port` property in `application.properties`.
- **Dependency Issues**: Run `./gradlew clean build` to ensure all dependencies are refreshed.
- **Neo4j Connection Issues**: Ensure the Neo4j database is running and credentials are correct.

---

For further assistance, contact the application maintainer.

