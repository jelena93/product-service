# Product Service

This is a Spring Boot application for product management. It includes product related functionalities, such as creating products via REST API and sending notification when new products are created.

## Technology Stack

- **Spring Boot:** Backend framework
- **PostgreSQL:** Database used for storing products
- **Kafka:** Used for implementing sending notifications
- **Flyway:** Database migration tool
- **Docker & Docker Compose:** For containerization and managing services

## Getting Started

### Build and Run the Application
Prerequisites

Make sure you have the following installed on your machine:

    Java 17
    Maven
    Docker

Docker Compose is used to manage the services, which includes Spring Boot application, PostgreSQL, Kafka, and Zookeeper.

To build & run the application, navigate to the root directory of the project and run:

```bash
mvn clean install
docker-compose up --build
```

Spring Boot application will be running on localhost:8080.

### API Endpoints

Service currently supports the following API operation:

    POST /api/product: Create a new product

* When this API is called service will create product in db with name and price and send this notification to topic on Kafka.

### API Documentation
The API documentation is generated using the OpenAPI specification. You can access the API documentation by visiting [http://localhost:8080/v3/api-docs](http://localhost:8080/v3/api-docs) in your browser.

Swagger UI

For exploring and testing the API, you can access the Swagger UI by visiting [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html) in your browser.

### Running Tests
To run the tests, navigate to the project's root directory and run:

```bash
mvn test 
```
To run integration test execute
```bash
docker-compose up --build
mvn verify -PIT
```
To see published kafka messages go to:

[http://localhost:9000/topic/product](http://localhost:9000/topic/product)