![Java](https://img.shields.io/badge/Java-21-orange)

![Spring Boot](https://img.shields.io/badge/SpringBoot-3.x-green)

![PostgreSQL](https://img.shields.io/badge/PostgreSQL-Database-blue)

![JWT](https://img.shields.io/badge/JWT-Authentication-red)

![License](https://img.shields.io/badge/License-MIT-yellow)

# DigiLocker Backend

A secure document management **REST API** built using Java 21, Spring Boot, Spring Security, JWT Authentication, and PostgreSQL.

## Overview

A secure document management REST API built using **Java 21**, **Spring Boot**, **Spring Security**, **JWT Authentication**, and **PostgreSQL**. The application enables users to securely upload, download, share, and manage digital documents while enforcing role-based access control and JWT-based authentication.

---

## ✨ Features

### 🔐 Authentication & Security
- User Registration
- JWT Authentication
- BCrypt Password Encryption
- Role-Based Authorization

### 📄 Document Management
- Upload Documents
- Download Documents
- Delete Documents
- View Documents

### 🤝 Sharing
- Share Documents
- View Shared Documents

### 🛠 Backend Features
- DTO Pattern
- Request Validation
- Global Exception Handling
- Swagger API Documentation

---

## Tech Stack

- Java 21
- Spring Boot
- Spring Security
- JWT
- Spring Data JPA
- Hibernate
- PostgreSQL
- Maven
- Git
- IntelliJ IDEA

---

## Project Structure

```
src
 ├── controller
 ├── service
 ├── repository
 ├── entity
 ├── dto
 ├── security
 ├── config
 ├── exception
 └── resources
```

---

## Architecture
```text
                Client
         (Swagger/Postman)
                  │
                  ▼
        Spring Security Filter
          JWT Authentication
                  │
                  ▼
            Controller Layer
                  │
                  ▼
             Service Layer
        (Business Logic)
                  │
                  ▼
          Repository Layer
         (Spring Data JPA)
                  │
                  ▼
             PostgreSQL
```

---

##DataBase Design
```text
User
-----
id
name
email
password
role

      1
      │
      │
      *
Document
--------
id
fileName
documentType
owner

      *
      │
      │
      *
Shared Users
```

---

## Authentication

The application uses **JWT (JSON Web Token)** authentication.

Flow:

1. Register a new user.
2. Login with email and password.
3. Receive a JWT token.
4. Include the token in the Authorization header for protected APIs.

---

## API Endpoints

### Authentication

- POST /register
- POST /login

### Documents

- POST /documents/upload
- GET /documents
- GET /documents/{id}
- DELETE /documents/{id}
- POST /documents/share
  
### Admin

- GET /admin/users
- GET /admin/documents
- DELETE /admin/users/{id}
- DELETE /admin/documents/{id}

---

## Key Concepts Demonstrated

- Layered Architecture
- REST API Design
- Spring Security
- JWT Authentication
- Role-Based Authorization
- DTO Pattern
- Spring Data JPA
- Hibernate
- Exception Handling
- Request Validation
- File Handling
- Dependency Injection

---

## How to Run

### Clone the repository

```bash
git clone https://github.com/TomarDeepak141/DigiLocker.git
```

### Navigate into the project

```bash
cd DigiLocker
```

### Configure PostgreSQL

Copy application.properties.example
to

application.properties

and configure:

DB URL
Username
Password
JWT Secret

with your database credentials.

### Build the project

```bash
mvn clean install
```

### Run

```bash
mvn spring-boot:run
```

---

## Future Improvements

- AWS S3 Integration
- Docker
- Redis
- Unit Testing
- CI/CD
- Email Verification
- Audit Logs
- Rate Limiting

---

## Author

**Deepak Tomar**

GitHub:
https://github.com/TomarDeepak141

LinkedIn:
https://linkedin.com/in/deepak-tomar-654954203
