# Project-Management-System — Spring Boot Backend

[![Java](https://img.shields.io/badge/Java-25-orange?style=for-the-badge&logo=java&logoColor=white)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring_Boot-4.0.2-brightgreen?style=for-the-badge&logo=spring&logoColor=white)](https://spring.io/projects/spring-boot)
[![MySQL](https://img.shields.io/badge/MySQL-8.0-blue?style=for-the-badge&logo=mysql&logoColor=white)](https://www.mysql.com/)
[![Hibernate](https://img.shields.io/badge/Hibernate-7-59666C?style=for-the-badge&logo=hibernate&logoColor=white)](https://hibernate.org/)
[![JWT](https://img.shields.io/badge/Auth-JWT-black?style=for-the-badge)](https://jwt.io/)
[![Swagger](https://img.shields.io/badge/Docs-OpenAPI_3-85EA2D?style=for-the-badge&logo=openapi-initiative&logoColor=black)](https://project-management-system-backend-production.up.railway.app/swagger-ui/index.html)
[![Railway](https://img.shields.io/badge/Deployed-Railway-8B5CF6?style=for-the-badge&logo=railway&logoColor=white)](https://project-management-system-backend-production.up.railway.app)
[![Status](https://img.shields.io/badge/Status-Live-success?style=for-the-badge)](https://project-management-system-backend-production.up.railway.app)


> A **production-grade** Project Management SaaS backend demonstrating advanced JPA modeling, JWT security, granular RBAC, global exception handling, structured Logback logging, and cloud deployment on Railway — built with Spring Boot 4.

---

## 🌐 Live Deployment

| Resource | Link |
| :--- | :--- |
| 🚀 **API Base URL** | `https://project-management-system-backend-production.up.railway.app` |
| 📄 **Swagger UI (Live)** | [Open Swagger →](https://project-management-system-backend-production.up.railway.app/swagger-ui/index.html) |
| 📦 **OpenAPI JSON** | `https://project-management-system-backend-production.up.railway.app/v3/api-docs` |

---

## 📖 Table of Contents

- [Features](#-features)
- [Architecture](#-architecture--design)
- [Entity Relationship Model](#-entity-relationship-model)
- [Security & RBAC](#-security--rbac-architecture)
- [Global Exception Handling](#-global-exception-handling)
- [Logback Logging](#-logback-logging)
- [API Documentation](#-api-documentation)
- [Feature Gallery](#-feature-gallery)
- [Tech Stack](#-tech-stack)
- [Project Structure](#-project-structure)
- [Getting Started](#-getting-started)
- [Deployment](#-deployment)
- [Engineering Highlights](#-engineering-highlights)
- [Contact](#-contact)

---

## ✨ Features

| | Feature |
| :---: | :--- |
| 👥 | Multi-user project collaboration with team roles |
| 📝 | Issue tracking with priority, status, due dates & threaded comments |
| 🛡️ | Jira-style granular RBAC with method-level `@PreAuthorize` guards |
| 📨 | Secure project invitations via SMTP (Gmail + STARTTLS) |
| 💬 | Project-level chat with persistent message history |
| 📚 | Auto-generated interactive Swagger UI (OpenAPI 3.1) |
| 🌱 | Automated database seeding for Roles & Permissions on startup |
| 🚨 | Centralized global exception handling with uniform JSON error responses |
| 📋 | Structured Logback logging with rolling file strategy |
| 💳 | SaaS-ready subscription billing architecture (FREE / MONTHLY / ANNUAL) |
| 🚀 | Deployed live on Railway with Docker + Aiven Cloud MySQL |

---

## 🏛 Architecture & Design

```text
┌─────────┐     ┌────────────┐     ┌────────────┐     ┌─────────┐     ┌──────────────┐
│  Client │────▶│ JWT Filter │────▶│ Controller │────▶│ Service │────▶│  Repository  │
└─────────┘     └────────────┘     └────────────┘     └─────────┘     └──────┬───────┘
                                          │                                   │
                               ┌──────────▼──────────┐               ┌───────▼──────┐
                               │  Global Exception   │               │  Aiven MySQL │
                               │      Handler        │               │  (SSL/Cloud) │
                               └──────────┬──────────┘               └──────────────┘
                                          │
                                 ┌────────▼────────┐
                                 │  Logback Logger │
                                 │  (Console+File) │
                                 └─────────────────┘
```

### Design Principles

| Principle | Implementation |
| :--- | :--- |
| **Separation of Concerns** | Strict Controller → Service → Repository layering |
| **DTO Pattern** | Prevents infinite recursion in bidirectional JSON mappings |
| **Cascade Control** | Orphan removal ensures DB integrity on parent deletion |
| **Stateless Auth** | JWT-only; no server-side sessions — horizontally scalable |
| **Centralized Errors** | `@RestControllerAdvice` intercepts all exceptions globally |
| **Observability** | Logback with timestamped, thread-aware, rolling-file logs |

---

## 🗃 Entity Relationship Model

![Database ER Diagram](assets/er-diagram.png)

---

## 🔐 Security & RBAC Architecture

Authentication is fully stateless, implemented with **Spring Security 7** and **JWT (jjwt 0.12.6)**.

### Hierarchical Permission System

| Role | Permissions | Use Case |
| :--- | :--- | :--- |
| **PROJECT_OWNER** | All 10 permissions | Creator — full admin & destructive power |
| **ADMIN** | 9 permissions (no `DELETE_PROJECT`) | Trusted manager for team coordination |
| **DEVELOPER** | `READ`, `CREATE_ISSUE`, `UPDATE_ISSUE`, `COMMENT` | Core contributor focused on execution |
| **VIEWER** | `READ_PROJECT` only | Read-only for stakeholders or clients |

**Method-level security example:**

```java
@PreAuthorize("@projectSecurity.hasPermission(authentication, #projectId, 'INVITE_USERS')")
public ResponseEntity<MessageResponse> inviteProjectMember(...) { }
```

### Authentication Flow

```text
POST /auth/login
      │
      ▼
CustomUserDetailsService.loadUserByUsername()
      │
      ▼
BCrypt password verification
      │
      ▼
JWT signed & returned → stored client-side
      │
      ▼
Every request: JwtFilter extracts token → sets SecurityContext
```

---

## 🚨 Global Exception Handling

A centralized `@RestControllerAdvice` intercepts all exceptions — no try-catch clutter in controllers.

### Uniform Error Response

```json
{
  "timestamp": "2026-05-01T18:35:23.149",
  "status": 404,
  "error": "Not Found",
  "message": "Project with ID 42 not found",
  "path": "/api/projects/42"
}
```

### Exception Coverage

| Exception | HTTP Status |
| :--- | :---: |
| `ResourceNotFoundException` | `404` |
| `AccessDeniedException` | `403` |
| `BadCredentialsException` | `401` |
| `MethodArgumentNotValidException` | `400` |
| `Exception` (catch-all fallback) | `500` |

```java
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(ResourceNotFoundException ex, HttpServletRequest req) {
        log.error("Resource not found: {}", ex.getMessage());
        return ResponseEntity.status(404).body(new ErrorResponse(404, ex.getMessage(), req.getRequestURI()));
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> handleAccessDenied(AccessDeniedException ex, HttpServletRequest req) {
        log.warn("Access denied at {}: {}", req.getRequestURI(), ex.getMessage());
        return ResponseEntity.status(403).body(new ErrorResponse(403, "Insufficient permissions", req.getRequestURI()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneral(Exception ex, HttpServletRequest req) {
        log.error("Unhandled exception at {}: {}", req.getRequestURI(), ex.getMessage(), ex);
        return ResponseEntity.status(500).body(new ErrorResponse(500, "An unexpected error occurred", req.getRequestURI()));
    }
}
```

---

## 📋 Logback Logging

Production-grade structured logging with **Logback**, configured via `logback-spring.xml`.

### Log Levels

| Package | Level | Purpose |
| :--- | :---: | :--- |
| `com.example.ProjectManagementSystem` | `DEBUG` | Full application trace |
| `org.springframework.security` | `INFO` | Security filter events |
| `org.hibernate.SQL` | `DEBUG` | SQL query visibility |
| Root | `INFO` | General application events |

### Sample Output

```log
2026-05-01 18:35:23 [restartedMain]        INFO  c.e.P.ProjectManagementSystemApplication - Started in 16.9s
2026-05-01 18:35:45 [http-nio-8080-exec-1] DEBUG c.e.P.service.ProjectService            - Fetching project ID: 5
2026-05-01 18:35:45 [http-nio-8080-exec-1] WARN  c.e.P.security.JwtFilter                - Invalid JWT token detected
2026-05-01 18:35:46 [http-nio-8080-exec-1] ERROR c.e.P.exception.GlobalExceptionHandler  - Resource not found: Project with ID 42
```

### `logback-spring.xml`

```xml
<configuration>
    <appender name="CONSOLE" class="ch.qos.logback.core.ConsoleAppender">
        <encoder>
            <pattern>%d{yyyy-MM-dd HH:mm:ss} [%thread] %-5level %logger{36} - %msg%n</pattern>
        </encoder>
    </appender>

    <appender name="FILE" class="ch.qos.logback.core.rolling.RollingFileAppender">
        <file>logs/application.log</file>
        <rollingPolicy class="ch.qos.logback.core.rolling.TimeBasedRollingPolicy">
            <fileNamePattern>logs/application-%d{yyyy-MM-dd}.log</fileNamePattern>
            <maxHistory>30</maxHistory>
        </rollingPolicy>
        <encoder>
            <pattern>%d{yyyy-MM-dd HH:mm:ss} [%thread] %-5level %logger{36} - %msg%n</pattern>
        </encoder>
    </appender>

    <logger name="com.example.ProjectManagementSystem" level="DEBUG"/>
    <logger name="org.springframework.security" level="INFO"/>
    <logger name="org.hibernate.SQL" level="DEBUG"/>

    <root level="INFO">
        <appender-ref ref="CONSOLE"/>
        <appender-ref ref="FILE"/>
    </root>
</configuration>
```

---

## 📋 API Documentation

👉 **[Live Swagger UI](https://project-management-system-backend-production.up.railway.app/swagger-ui/index.html)**

![Swagger API Dashboard](assets/Swagger-Hero-Shot.png)

| Module | Coverage |
| :--- | :--- |
| **Auth** | Register, Login, JWT |
| **User** | Profile view & update |
| **Project** | Create, search, invite, manage team |
| **Issue** | Create, assign, status lifecycle, filter |
| **Comment** | Threaded comments on issues |
| **Chat** | Send & retrieve project messages |

---

## 🖼 Feature Gallery

### 🛡️ Authentication & User Profile

| Sign In | User Profile |
| :---: | :---: |
| ![Sign In](assets/sign%20in.png) | ![User Profile](assets/User%20Profile.png) |

### 📁 Project Management

| Creating a Project | Search by Keyword |
| :---: | :---: |
| ![Creating Project](assets/creating%20new%20project.png) | ![Search Project](assets/Search%20projects%20with%20keyword.png) |

### 📝 Issue Tracking & Assignments

| Adding an Assignee | Issues by Project |
| :---: | :---: |
| ![Assignee](assets/adding%20assignee%20on%20issue.png) | ![Get Issues](assets/Get%20Issues%20By%20Project%20id.png) |

### 💬 Real-Time Chat

![Project Chat](assets/Send%20Messages.png)

### 🚀 Startup & DB Seeding Logs

![Startup Logs](assets/console-startup-logs.png)

---

## 🛠 Tech Stack

| Layer | Technology |
| :--- | :--- |
| **Language** | Java 25 |
| **Framework** | Spring Boot 4.0.2 |
| **Security** | Spring Security 7 + JWT (jjwt 0.12.6) |
| **ORM** | Hibernate 7 / Spring Data JPA |
| **Database** | MySQL 8.0 — Aiven Cloud |
| **API Docs** | SpringDoc OpenAPI 3.1 / Swagger UI 5 |
| **Mailing** | Jakarta Mail + Gmail SMTP (STARTTLS) |
| **Logging** | Logback (structured, rolling file) |
| **Error Handling** | `@RestControllerAdvice` global handler |
| **Deployment** | Railway — Docker multi-stage build |
| **Build Tool** | Maven 3.9 |

---

## 📂 Project Structure

```text
src/
├── main/
│   ├── java/com/example/ProjectManagementSystem/
│   │   ├── config/         # Security, JWT, CORS, Swagger configuration
│   │   ├── controller/     # REST endpoints with @PreAuthorize method guards
│   │   ├── exception/      # GlobalExceptionHandler + custom exception classes
│   │   ├── service/        # Business logic & cross-entity validations
│   │   ├── repository/     # Spring Data JPA interfaces
│   │   ├── model/          # JPA entities (User, Project, Issue, Chat, ChatUser…)
│   │   ├── request/        # Incoming request payload DTOs
│   │   ├── response/       # Standardized API response schemas
│   │   └── seeder/         # DatabaseSeeder — auto-populates Roles & Permissions
│   └── resources/
│       ├── application.properties   # Environment configuration
│       └── logback-spring.xml       # Structured logging configuration
└── test/                            # Unit & integration tests
```

---

## 🚀 Getting Started

### Prerequisites

- JDK 21+
- Maven 3.8+
- MySQL 8.0+ (local or Aiven Cloud)
- Gmail App Password for SMTP

### 1️⃣ Clone

```bash
git clone https://github.com/Sarfaraz-E/project-management-system-backend.git
cd project-management-system-backend
```

### 2️⃣ Configure `application.properties`

```properties
# Database
spring.datasource.url=jdbc:mysql://localhost:3306/projectmanagement
spring.datasource.username=root
spring.datasource.password=your_password
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true

# JWT
jwt.secret=your_jwt_secret_key

# SMTP (Gmail)
spring.mail.host=smtp.gmail.com
spring.mail.port=587
spring.mail.username=your_email@gmail.com
spring.mail.password=your_16_digit_app_password
spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.starttls.enable=true
```

### 3️⃣ Run

```bash
./mvnw spring-boot:run
```

On startup the `DatabaseSeeder` auto-initializes all Roles and Permissions:

```log
--- Checking and Seeding Database Roles & Permissions ---
--- Database Seeding Complete ---
```

| Endpoint | URL |
| :--- | :--- |
| API | `http://localhost:8080` |
| Swagger UI | `http://localhost:8080/swagger-ui/index.html` |

---

## ☁️ Deployment

### Architecture

```text
GitHub (main)
     │  push → auto-deploy
     ▼
Railway CI/CD
     │  Docker multi-stage build
     ▼
Spring Boot App  ──────SSL──────▶  Aiven Cloud MySQL
(Railway Pod)
     │
     ▼
Public HTTPS URL
```

### Dockerfile

```dockerfile
FROM maven:3.9-eclipse-temurin-21-alpine AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
```

### Railway Environment Variables

```env
SPRING_DATASOURCE_URL=jdbc:mysql://...aivencloud.com:PORT/defaultdb?sslMode=REQUIRED
SPRING_DATASOURCE_USERNAME=avnadmin
SPRING_DATASOURCE_PASSWORD=***
SPRING_JPA_HIBERNATE_DDL_AUTO=update
JWT_SECRET=***
SPRING_MAIL_USERNAME=your_email@gmail.com
SPRING_MAIL_PASSWORD=***
PORT=8080
```

---

## 🧠 Engineering Highlights

| Area | Detail |
| :--- | :--- |
| **Advanced JPA** | Explicit join entity for `chat_users` satisfies cloud DB primary key constraints; mastered all relationship types |
| **Method-level Security** | Custom `ProjectSecurity` bean evaluates permissions dynamically per user-project pair |
| **Stateless Design** | JWT-only authentication — no session affinity, horizontally scalable |
| **Defensive JSON** | `@JsonIgnore`, `@EqualsAndHashCode.Exclude`, `@ToString.Exclude` prevent circular serialization |
| **Error Contracts** | Uniform error shape across entire API surface — no inconsistent responses |
| **Observability** | Per-package Logback levels with rolling 30-day file retention |
| **CI/CD** | Auto-deploy triggered on every `git push` to `main` via Railway GitHub integration |
| **Idempotent Seeding** | `DatabaseSeeder` checks before inserting — safe to run on every startup |

---

*Designed & Developed by **Sarfaraz Essa***

---

## 📬 Contact

**Sarfaraz Essa**
📧 [sarfarazessa18@gmail.com](mailto:sarfarazessa18@gmail.com)
🔗 [LinkedIn](https://www.linkedin.com/in/sarfaraz-essa/)
🐙 [GitHub](https://github.com/Sarfaraz-E)
