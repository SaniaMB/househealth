<div align="center">

# 🏠 HouseHealth Backend

**A secure REST API powering HouseHealth, a family-centered health monitoring platform built with Spring Boot.**

![Java](https://img.shields.io/badge/Java-21-orange?style=for-the-badge&logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-4.0.2-6DB33F?style=for-the-badge&logo=springboot&logoColor=white)
![Spring Security](https://img.shields.io/badge/Spring%20Security-JWT-success?style=for-the-badge&logo=springsecurity&logoColor=white)
![MySQL](https://img.shields.io/badge/MySQL-Aiven-4479A1?style=for-the-badge&logo=mysql&logoColor=white)
![Maven](https://img.shields.io/badge/Build-Maven-C71A36?style=for-the-badge&logo=apachemaven&logoColor=white)
![Render](https://img.shields.io/badge/Deployment-Render-5C4EE5?style=for-the-badge&logo=render&logoColor=white)

</div>

<br>

## 🌐 Live Demo

The backend powers the live HouseHealth application.

**Website:** [https://www.househealth.site](https://www.househealth.site)

<br>

## 🔑 Demo Account

Explore the application immediately using the pre-populated demo account.

| Field        | Value                      |
|:-------------|:---------------------------|
| **Email**    | `DemoUser@example.com`      |
| **Password** | `1234567890`                |

The demo account includes:

- Sample blood pressure history
- Sample fasting blood sugar history
- Sample post-meal blood sugar history
- Dashboard analytics
- Trend analysis
- Family data
- Notifications
- Reminder settings
- PDF health report generation

> 💡 Want to experience email verification and password reset? Create your own account instead of using the demo account.

<br>

## 📖 Overview

HouseHealth is a family-centered health monitoring platform designed to help users record important health metrics while securely sharing information with trusted family members.

This repository contains the backend REST API responsible for:

- Authentication and authorization
- Health data management
- Family management
- Trend analysis
- Reminder scheduling
- Notifications
- PDF report generation

The application follows a layered architecture based on the **Controller → Service → Repository** pattern, separating HTTP handling, business logic, and persistence into well-defined layers.

<br>

## 🚀 Engineering Highlights

HouseHealth demonstrates backend engineering concepts beyond traditional CRUD applications.

- Layered Spring Boot architecture
- RESTful API design
- JWT-based stateless authentication
- Token-based email verification
- Secure password reset using time-limited verification tokens
- BCrypt password hashing
- Object-level authorization (BOLA protection)
- DTO-based API contracts
- Family-based authorization model
- Scheduled reminder system
- Health trend analysis
- PDF report generation with charts
- Centralized exception handling
- Production deployment

<br>

## 🧭 Design Principles

HouseHealth was designed around a few core principles:

- Security by default through authentication and authorization.
- Separation of concerns using a layered architecture.
- Family-centered health sharing with explicit ownership and membership rules.
- RESTful API design using DTOs to decouple persistence from API contracts.
- Clean, maintainable code emphasizing readability and extensibility.

<br>

## ⭐ Core Capabilities

### Authentication

- User registration
- JWT authentication
- Token-based email verification
- Forgot password
- Token-based password reset

### Health Tracking

Supports logging and managing:

- Blood Pressure
- Fasting Blood Sugar
- Post-Meal Blood Sugar

Each reading is stored with timestamps and available for historical analysis.

### Dashboard

Provides:

- Latest health readings
- Historical charts
- Health summaries
- Trend analysis
- Average calculations

### Family Management

Users can:

- Create families
- Invite members
- Accept or decline invitations
- Transfer ownership
- Leave families
- Manage members

### Care Relationships

Users can create care relationships with members of their family, allowing them to monitor the health activity of selected family members more closely.

### Reminder System

Configurable reminder settings support:

- Weekly reminders
- Monthly reminders
- Custom intervals

Spring Scheduler automatically generates reminder notifications.

### Notifications

Supports notifications for:

- Health reminders
- Family invitations
- Invitation responses
- Care relationship updates

### Health Reports

Generate downloadable PDF reports containing:

- Patient information
- Latest health readings
- Trend summaries
- Blood pressure charts
- Blood sugar charts
- Historical readings

<br>

## 🏗 Architecture

HouseHealth follows a layered architecture that separates presentation, business logic, and persistence.

```text
                Client (React)
                      │
                      ▼
            REST Controllers
                      │
                      ▼
              Business Services
                      │
                      ▼
           Spring Data Repositories
                      │
                      ▼
                 MySQL Database
```

Each layer has a clearly defined responsibility, improving maintainability, readability, and scalability.

<br>

## 📁 Project Structure

```text
src/main/java/com/project/househealth
├── bootstrap
├── config
├── controllers
├── dashboard
├── dto
├── entity
├── enums
├── exception
├── reports
├── repositories
├── scheduler
├── security
├── service
└── trendanalysis
```

| Package        | Responsibility                          |
|:---------------|:-----------------------------------------|
| controllers    | REST API endpoints                       |
| service        | Business logic                           |
| repositories   | Database access                          |
| entity         | Domain models                            |
| dto            | API request and response models          |
| security       | JWT authentication and Spring Security   |
| dashboard      | Dashboard aggregation services           |
| reports        | PDF report generation                    |
| scheduler      | Reminder scheduling                      |
| trendanalysis  | Health trend calculations                |
| exception      | Global exception handling                |

<br>

## 🔒 Security

Security is implemented using Spring Security and JWT authentication.

Implemented security measures include:

- JWT authentication
- BCrypt password hashing
- Token-based email verification before account activation
- Time-limited password reset tokens
- Protected REST endpoints
- Centralized exception handling
- Input validation
- Object-Level Authorization (BOLA protection)

Authorization checks are performed within the service layer to ensure authenticated users can only access resources they own or are explicitly permitted to access.

<br>

## 🛠 Technology Stack

**Backend**
- Java 21
- Spring Boot
- Spring Security
- Spring Data JPA
- Hibernate
- Maven

**Database**
- MySQL
- Aiven Cloud Database

**Email**
- Resend Email API

**Reporting**
- OpenPDF
- JFreeChart

**Deployment**
- Render
- Docker

<br>

## ✅ Testing

The project currently includes unit tests covering core business logic.

Current test coverage includes:

- FamilyService
- FamilyMembershipService
- TrendAnalysisService

Testing framework:

- JUnit 5
- Mockito

Future releases will expand coverage with integration tests and automated CI validation.

<br>

## ⚙️ Running Locally

**Clone the repository**

```bash
git clone https://github.com/<your-username>/househealth.git
```

**Configure environment variables**

Configure the required environment variables before running the application.

Examples include:

- Database URL
- Database username
- Database password
- JWT secret
- Resend API key
- Verified sender email
- Frontend URL

**Run the application**

```bash
./mvnw spring-boot:run
```

The backend starts on:

```
http://localhost:8080
```

<br>

## 📚 Documentation

Additional project documentation can be found in the `docs` directory.

- **ARCHITECTURE.md** — System architecture and design decisions
- **REST_API.md** — REST endpoint reference
- **DATA_MODEL.md** — Domain model and entity relationships
- **DEPLOYMENT.md** — Production deployment guide

<br>

## ☁️ Deployment

The backend is containerized with Docker and deployed on Render, using Aiven MySQL for persistent storage and Resend for transactional email delivery.

| Component      | Platform     |
|:----------------|:-------------|
| Frontend        | Vercel       |
| Backend         | Render       |
| Database        | Aiven MySQL  |
| Email Service   | Resend       |

<br>

## 🔮 Future Improvements

Planned enhancements include:

- Redis caching
- Rate limiting
- GitHub Actions CI pipeline
- Integration testing
- OpenAPI / Swagger documentation
- Audit logging
- Metrics and monitoring

<br>

## 👩‍💻 Author

**Sania Bhandari**

HouseHealth was developed as a portfolio project to demonstrate modern backend software engineering practices, including secure authentication, REST API development, layered architecture, business logic implementation, and production deployment.