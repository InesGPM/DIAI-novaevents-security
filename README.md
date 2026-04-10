# Nova Events

A Spring Boot SSR web application for managing campus clubs and events.

## Tech Stack
- **Backend:** Spring Boot 3.5.10 (Java 17 / Kotlin 2.1.0)
- **Database:** MySQL 8.0 (Running on Docker)
- **ORM:** Spring Data JPA with Hibernate
- **Frontend:** Thymeleaf & Bootstrap 5

## Prerequisites
- Docker Desktop installed and running.

## Run with Database

1. **Start the database container:**
```bash
docker run --name novaevents-db -e MYSQL_ROOT_PASSWORD=root -e MYSQL_DATABASE=novaevents -e MYSQL_USER=novaevents_user -e MYSQL_PASSWORD=novaevents123 -p 3306:3306 -d mysql:8.0
```

## Run
```bash
.\mvnw spring-boot:run
```

## Access
http://localhost:8080/clubs