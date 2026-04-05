# Sensor Group Application

A Spring Boot microservice for managing IoT sensor groups and their readings using MongoDB as the primary database.

## Prerequisites

- **Java 21** or higher
- **Maven 3.6+**
- **Docker** and **Docker Compose**

## Project Structure

```
src/
├── main/
│   ├── java/com/iot/sensorgroup/
│   │   ├── SensorgroupApplication.java
│   │   ├── controller/          # REST API controllers
│   │   ├── dao/                 # Data access objects
│   │   ├── dto/                 # Data transfer objects
│   │   └── service/             # Business logic services
│   └── resources/
│       ├── application.yaml     # Application configuration
│       ├── docker/              # Docker Compose setup
│       ├── static/              # Static assets
│       └── templates/           # HTML templates
└── test/                        # Unit and integration tests
```

## Quick Start

### 1. Start Docker Services

Start MongoDB and Mongo Express using Docker Compose:

```bash
cd src/main/resources/docker
docker-compose up
```

This will start:
- **MongoDB** on `localhost:27017`
  - Admin credentials: `admin` / `password`
  - Database: `sensorgroup`
- **Mongo Express** (Web UI) on `http://localhost:8081`
  - Admin credentials: `admin` / `pass`

### 2. Build the Application

Build the project using Maven:

```bash
mvn clean install
```

### 3. Run the Application

Run the Spring Boot application:

```bash
mvn spring-boot:run
```

The application will start on `http://localhost:8080`

## API Endpoints

- **Health Check**: `http://localhost:8080/actuator/health`
- **Application Info**: `http://localhost:8080/actuator/info`

## Configuration

The application is configured via `src/main/resources/application.yaml`:

```yaml
spring:
  application:
    name: sensorgroup
  data:
    mongodb:
      uri: mongodb://admin:password@localhost:27017/sensorgroup?authSource=admin
```

### Key Features

- **Spring Boot 4.0.5** - Latest stable framework
- **MongoDB Integration** - NoSQL database with Spring Data MongoDB
- **RESTful API** - Spring Web MVC for REST services
- **Health Monitoring** - Spring Boot Actuator for application health checks
- **Lombok** - Simplified entity and service development
- **Docker Support** - Complete containerized setup

## Technologies

| Technology | Version |
|-----------|---------|
| Spring Boot | 4.0.5 |
| Java | 21 |
| MongoDB | Latest |
| Maven | 3.6+ |

## Testing

Run tests with:

```bash
mvn test
```

## Development

### Stopping Services

To stop the Docker services:

```bash
docker-compose down
```

To remove volumes:

```bash
docker-compose down -v
```

### Rebuild and Restart

```bash
# Clean and rebuild
mvn clean install

# Stop the application and restart
docker-compose restart
mvn spring-boot:run
```

## Documentation

- [HELP.md](HELP.md) - Setup reference and documentation links
- [Spring Boot Documentation](https://docs.spring.io/spring-boot/4.0.5/reference/)
- [Spring Data MongoDB](https://docs.spring.io/spring-boot/4.0.5/reference/data/nosql.html#data.nosql.mongodb)

## Troubleshooting

### MongoDB Connection Issues

Ensure MongoDB is running and accessible:

```bash
# Check if containers are running
docker-compose ps

# View logs
docker-compose logs mongodb
```

### Port Already in Use

If ports 8080, 27017, or 8081 are already in use:

- Change Spring Boot port in `application.yaml`: `server.port: 8080`
- Change MongoDB port in docker-compose:
  ```yaml
  ports:
    - "27018:27017"  # Change external port
  ```
- Change Mongo Express port in docker-compose:
  ```yaml
  ports:
    - "8082:8081"  # Change external port


  ```

  http://localhost:8081/actuator/health

## License

This project is an IoT Sensor Group Management System.
