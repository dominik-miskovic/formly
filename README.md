# Forumly Application

This is a Basic Forum as a self-contained Spring Boot application with an embedded H2 database.

## Features
 - User Registration
 - User Login
 - User Logout
 - Forum Creation
 - Forum Posting
 - Forum Commenting

## Prerequisites

- Java 17 or higher must be installed on your system.
- You can verify your Java installation by opening a terminal or command prompt and running: `java -version`

## How to Run

1.  Unzip the `forumly-app.zip` file.
2.  Navigate into the extracted folder.
3.  Run the application either:
    -   **By command** in a shell: ```java -jar target/forumly-app.jar```
    -   **With Docker:** ```docker-compose up --build -d```

## How to Access the Application

-   **Web Application**: Open your browser and go to [http://localhost:8080](http://localhost:8080)
-   **H2 Database Console**: To view the database directly, go to [http://localhost:8080/h2-console](http://localhost:8080/h2-console)
    -   **JDBC URL**: `jdbc:h2:file:./data/forumlydb`
    -   **Username**: `sa`
    -   **Password**: `password`

When you run the application, a `data` directory will be created in the same folder, containing the database files.