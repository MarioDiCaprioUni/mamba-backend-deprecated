# Mamba (backend)

This application is the backend for the *Mamba* website.
it is a *SpringBoot* project that exposes a REST API
to communicate with the [frontend of this application](https://github.com/MarioDiCaprio/mamba-frontend).
This application is deployed on the cloud alongside a PostgreSQL
database that is managed via *Hibernate's* ORM.

## Tech Stack

This application utilizes the following technologies:
- [Spring Boot](https://spring.io/projects/spring-boot)
- [Project Lombok](https://projectlombok.org/)
- [Springdoc](https://springdoc.org/)

## REST Documentation
The REST API was fully documented with *Springdoc* and is accessible under the
`/swagger-ui.html` endpoint.

## Project Structure

The main project can be found under `src/main/java/com/mariodicaprio/mamba`
and takes on the following structure:
- `/config`: Configurations for the Spring Container
- `/controllers`: The controllers (i.e. *endpoints*) of the REST API
- `/entities`: The database entities for the ORM
- `/repositories`: The Spring Data repositories that act as DAOs
- `/requests`: Request bodies
- `/responses`: Response bodies
- `/services`: The services that manage actions with the database

For a more detailed documentation, see the Javadoc.

## Testing

Tests for this project can be found under `src/test/java/com/mariodicaprio/mamba`.
Again, the project structure looks as follows:
- `/controllers`: Tests the application's controllers (*Integration Tests*)
- `/repositories`: Tests the JPA repositories
- `/services`: Tests the application's service layer
- 
To run all tests execute `./gradlew test` in the project's root directory.

## Deployment

This application was deployed on *Heroku* and is publicly accessible
[here](https://mamba-backend.herokuapp.com).
