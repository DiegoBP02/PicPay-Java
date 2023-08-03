This repository represents a back-end challenge from PicPay. The challenge is titled "[Desafio Back-end PicPay](https://github.com/PicPay/picpay-desafio-backend)".

## Table of Contents
* [Prerequisites](#prerequisites)
* [Libraries](#libraries)
* [Running the application](#running-the-application)
    * [On Windows](#on-windows)

## Prerequisites
- [Java JDK](https://www.oracle.com/pl/java/technologies/javase-downloads.html) version 17+
- [Docker Desktop](https://www.docker.com/products/docker-desktop) 

## Libraries
| Library name                                                                                                     | Description                                                                                                                          |
|-|-|
| [Spring Boot](https://spring.io/projects/spring-boot)                                                            | Spring Boot makes it easy to create stand-alone, production-grade Spring based Applications that you can "just run".                 |
| [Spring Data JPA](https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#jpa.repositories)         | The JPA module of Spring Data contains a custom namespace that allows defining repository beans.                                     |
| [Spring Web](https://docs.spring.io/spring-framework/docs/current/reference/html/web.html)                      | Spring Web is the web framework for building web applications, including RESTful web services.                                      |
| [PostgreSQL JDBC Driver](https://jdbc.postgresql.org/documentation/)                                             | The PostgreSQL JDBC Driver allows Java programs to connect to a PostgreSQL database using standard, database-independent Java code.  |
| [Spring Boot Test](https://spring.io/projects/spring-boot)                                                       | Spring Boot Test provides a set of testing utilities for testing Spring Boot applications.                                          |
| [Lombok](https://projectlombok.org/)                                                                             | Project Lombok is a java library that automatically plugs into your editor and build tools, spicing up your java.                    |
| [Hibernate Validator](https://hibernate.org/validator/)                                                          | Hibernate Validator is a reference implementation of the Bean Validation standard for Java.                                       |
| [JUnit Jupiter API](https://junit.org/junit5/docs/current/user-guide/)                                           | JUnit 5 is the next generation of JUnit. It is an open-source testing framework for Java.                                          |
| [JUnit Jupiter Engine](https://junit.org/junit5/docs/current/user-guide/)                                        | JUnit 5 Jupiter Engine is the TestEngine for running tests written using JUnit Jupiter API.                                        |
| [JUnit Platform Suite](https://junit.org/junit5/docs/current/user-guide/)                                        | The JUnit Platform Suite is a TestEngine that runs tests on the JUnit Platform.                                                     |
| [JUnit Jupiter Params](https://junit.org/junit5/docs/current/user-guide/)                                        | JUnit Jupiter Params provides support for parameterized tests in JUnit 5.                                                           |
| [Spring Security](https://spring.io/projects/spring-security)                                                    | Spring Security provides authentication, authorization, and other security features for Spring-based applications.                 |
| [Java JWT](https://github.com/auth0/java-jwt)                                                                    | Java JWT is a library to work with JSON Web Tokens (JWTs). It allows you to encode and decode JWTs.                                |
| [H2 Database](https://www.h2database.com/html/main.html)                                                        | H2 Database is a lightweight, fast, and embeddable in-memory database written in Java.                                              |
| [Spring Security Test](https://docs.spring.io/spring-security/site/docs/current/reference/html5/#test)           | Spring Security Test provides support for testing Spring Security components in Spring Boot applications.                          |
## Running the application
#### On Windows
1. Clone the repository
1. Create a copy of the application.properties.example file located in src/main/resources and rename it to application.properties.
1. Open the `application.properties` file and provide the following information:
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/picpay
spring.datasource.username=postgres
spring.datasource.password=secret123
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect
spring.jpa.properties.hibernate.format_sql=true
jwt.secret=your-jwt-secret
token.expiration=your-token-expiration
timezone.offset=your-timezone-offset
```
1. Replace the placeholders with your own data. Here's a description of each placeholder:
    1. jwt.secret: Your JWT secret.
    1. token.expiration: Your token expiration in seconds.
    1. timezone.offset: The timezone offset for your location. For example, use `-03:00` for UTC-3:00..
1. Start Docker engine (Linux) or Docker desktop (macOS or Windows). 
1. In the root of the project, run `mvn package -DskipTests` to build the project and packages it into an executable JAR, skipping tests.
1. Run `docker-compose up --build` to start the containers.
1. If the application is running successfully, you should see log messages indicating the startup of the application.
1. In the root of the project, run `mvn clean test` to run all tests.
1. When finished, run `docker-compose down` to stop and remove the containers
