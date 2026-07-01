# DigiLocker Backend

A secure document management backend built using **Java**, **Spring Boot**, **Spring Security**, **JWT Authentication**, and **PostgreSQL**.

## Overview

DigiLocker Backend is a RESTful API that allows users to securely manage digital documents. It provides authentication, authorization, document upload, download, sharing, and management features while following a layered architecture.

---

## Features

- User Registration
- Secure Login using JWT Authentication
- Role-Based Authorization
- Upload Documents
- Download Documents
- View Documents
- Delete Documents
- Share Documents with Other Users
- Input Validation
- Exception Handling
- RESTful API Design

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

```
Client
   │
   ▼
Controller
   │
   ▼
Service
   │
   ▼
Repository
   │
   ▼
PostgreSQL
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

Update:

```
src/main/resources/application.properties
```

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

- Swagger/OpenAPI Documentation
- Docker Support
- File Storage using AWS S3
- Email Notifications
- API Rate Limiting
- Microservices Architecture
- Redis Caching

---

## Author

**Deepak Tomar**

GitHub:
https://github.com/TomarDeepak141

LinkedIn:
https://linkedin.com/in/deepak-tomar-654954203
