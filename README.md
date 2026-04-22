# Project-Management-System-Spring-Boot-JPA

[![Java](https://img.shields.io/badge/Java-17-orange?style=for-the-badge&logo=java&logoColor=white)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.x-brightgreen?style=for-the-badge&logo=spring&logoColor=white)](https://spring.io/projects/spring-boot)
[![Hibernate](https://img.shields.io/badge/Hibernate-ORM-59666C?style=for-the-badge&logo=hibernate&logoColor=white)](https://hibernate.org/)
![JWT](https://img.shields.io/badge/Auth-JWT-black?style=for-the-badge)

A production-grade backend system built to demonstrate **advanced JPA relationship modeling**, **JWT-based security architecture**, and **clean layered system design** using Spring Boot.

This project simulates a real-world **Project Management SaaS backend**, implementing:

- 👥 Multi-user project collaboration

- 📝 Issue tracking & threaded commenting

- 🛡 Team-based access control (RBAC-style modeling)

- 💬 Project-level real-time chat

- 💳 Subscription-ready billing architecture [Working]

- ⚛️ Upcoming React frontend

---

## 📖 Table of Contents
- [Architecture & Design](#-architecture--design)
- [Entity Relationship Model](#-entity-relationship-model)
- [Relationship Strategy](#-relationship-strategy)
- [Security Architecture](#-security-architecture)
- [Key Features](#-key-features)
- [Tech Stack](#-tech-stack)
- [Project Structure](#-project-structure)
- [Getting Started](#-getting-started)

---

# 🏛 Architecture & Design

The system follows a strict layered architecture:

```

Client → Controller → Service → Repository → Database

````

### Design Principles Applied

- Separation of Concerns
- DTO Pattern for API isolation
- Strict Cascade Control
- Orphan Removal for lifecycle integrity
- Lazy Loading for performance optimization
- Enum-based domain constraints
- Token-based stateless authentication

---

# 🧩 Entity Relationship Model

```mermaid
erDiagram

    USER }|--|{ PROJECT : "Member Of (N:M)"
    PROJECT ||--o{ ISSUE : "Contains (1:N)"
    ISSUE ||--o{ COMMENT : "Has (1:N)"
    PROJECT ||--|| CHAT : "Has (1:1)"
    CHAT ||--o{ MESSAGE : "Contains (1:N)"
    USER ||--o{ ISSUE : "Assigned To (1:N)"
    USER ||--o{ MESSAGE : "Sends (1:N)"
    USER ||--o{ INVITATION : "Receives (1:N)"
    USER ||--o{ SUBSCRIPTION : "Owns (1:N)"
````

---

# 🔗 Relationship Strategy

| Relationship            | Mapping Strategy                    | Lifecycle Logic                                                   |
| ----------------------- | ----------------------------------- | ----------------------------------------------------------------- |
| **Project ↔ Chat**      | `@OneToOne`                         | Strict composition. Each project owns exactly one chat instance.  |
| **Project ↔ Issue**     | `@OneToMany`                        | Cascade removal ensures issues are deleted if project is deleted. |
| **Issue ↔ Comment**     | `@OneToMany` + `orphanRemoval=true` | Prevents orphaned comments in DB.                                 |
| **User ↔ Project**      | `@ManyToMany` with JoinTable        | Models team collaboration across multiple projects.               |
| **Chat ↔ Message**      | `@OneToMany`                        | Message lifecycle bound to Chat.                                  |
| **User ↔ Subscription** | `@OneToMany`                        | Supports SaaS subscription scalability.                           |

---

# 🔐 Security Architecture

Authentication is implemented using:

* Spring Security
* JWT Token Provider
* Custom UserDetailsService
* Token validation filter
* Stateless session management

### Authentication Flow

1. User logs in
2. Server validates credentials
3. JWT token is generated
4. Client sends token in:

```
Authorization: Bearer <token>
```

5. Filter validates token before controller execution

No session storage. Fully stateless.

---

# 🌟 Key Features

### 🔹 Advanced ORM Mapping

* `@OneToOne`
* `@OneToMany`
* `@ManyToMany`
* `@JoinTable`
* Controlled cascading strategies

### 🔹 SaaS-Ready Subscription Modeling

* PlanType enum (FREE / MONTHLY / ANNUAL)
* Extensible billing architecture
* Payment integration-ready structure

### 🔹 Issue Tracking Engine

* Status handling
* Priority modeling
* Due date management
* DTO abstraction

### 🔹 Team Collaboration

* Multi-user project access
* Invitation-based onboarding
* Project-level communication model

### 🔹 Clean API Design

* RESTful conventions
* DTO-based request/response
* Service-layer validation
* Repository abstraction via Spring Data

---

# 🛠 Tech Stack

| Layer        | Technology                  |
| ------------ | --------------------------- |
| Language     | Java 17                     |
| Framework    | Spring Boot 3.x             |
| Security     | Spring Security + JWT       |
| ORM          | Hibernate / Spring Data JPA |
| Database     | MySQL                       |
| Build Tool   | Maven                       |
| Architecture | Layered MVC                 |

---

# 📂 Project Structure

```
src/main/java/com/example/ProjectManagementSystem
├── config          # JWT, Security, CORS configuration
├── controller      # REST API endpoints
├── service         # Business logic layer
├── repository      # Spring Data interfaces
├── model           # JPA entities
├── request         # Request DTOs
└── response        # Response DTOs
```

---

# 🚀 Getting Started

## Prerequisites

* JDK 17+
* Maven 3.6+
* MySQL running locally

---

## 1️⃣ Clone Repository

```bash
git clone https://github.com/Sarfaraz-E/project-management-system-backend.git
cd project-management-system-backend
```

---

## 2️⃣ Configure Database

Update:

```
src/main/resources/application.properties
```

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/project_management_db
spring.datasource.username=root
spring.datasource.password=yourpassword
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
```

---

## 3️⃣ Run Application

Using Maven wrapper:

```bash
./mvnw spring-boot:run
```

Application runs at:

```
http://localhost:8080
```

---

# 🧠 Engineering Highlights

This project demonstrates:

* Real-world JPA ownership modeling
* Controlled entity lifecycle management
* Stateless authentication architecture
* SaaS-ready backend structure
* Scalable domain design
* Clean code separation across layers

This is not just CRUD — it reflects production-level backend thinking.

---

<div align="center">
<sub>Designed & Developed by <strong>Sarfaraz Essa</strong></sub>
</div>

---

## 📬 Contact

**Sarfaraz Essa**
📧 [sarfarazessa18@gmail.com](mailto:sarfarazessa18@gmail.com)
[LinkedIn](https://www.linkedin.com/in/sarfaraz-essa/)
