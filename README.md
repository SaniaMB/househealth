# HouseHealth

HouseHealth is a Spring Boot project for managing health-related data in a family setting.

The idea is simple: users can belong to families, log basic health metrics, and manage reminders, while keeping everything private and structured.

This repository currently contains the core backend setup and data model. APIs and application logic will be added on top of this foundation.

---

## What’s in the project right now

- User and family domain models
- Family membership with roles
- Health logs (blood pressure, blood sugar)
- Reminder configuration
- Database integration using JPA and Hibernate

---

## Tech used

- Java
- Spring Boot
- Spring Data JPA
- Hibernate
- MySQL
- Maven

---

## Running locally

You’ll need Java, Maven, and MySQL.

Configure your database credentials in `application.properties`, then run:

```bash
./mvnw spring-boot:run
