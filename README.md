# Movie Service - Spring Boot Demo Application 🏴‍☠️

A simple movie catalog web application built with Spring Boot, demonstrating Java application development best practices with a **pirate-themed movie search and filtering system**!

## Features

- **Movie Catalog**: Browse 12 classic movies with detailed information
- **🔍 Pirate Movie Search**: Hunt for movie treasures using our advanced search functionality
- **🎭 Genre Filtering**: Filter movies by genre with pirate-themed interface
- **🆔 ID-based Search**: Find specific movie treasures by their unique ID
- **Movie Details**: View comprehensive information including director, year, genre, duration, and description
- **Customer Reviews**: Each movie includes authentic customer reviews with ratings and avatars
- **Responsive Design**: Mobile-first design that works on all devices
- **Modern UI**: Dark theme with gradient backgrounds, smooth animations, and pirate-themed styling
- **Robust Error Handling**: Specific exception handling instead of general exceptions for better debugging

## Technology Stack

- **Java 8**
- **Spring Boot 2.0.5**
- **Maven** for dependency management
- **Log4j 2.20.0**
- **JUnit 5.8.2**
- **Thymeleaf** for templating
- **Pirate Language Integration** 🏴‍☠️
- **Custom Exception Classes** for specific error handling

## Quick Start

### Prerequisites

- Java 8 or higher
- Maven 3.6+

### Run the Application

```bash
git clone https://github.com/<youruser>/sample-qdev-movies.git
cd sample-qdev-movies
mvn spring-boot:run
```

The application will start on `http://localhost:8080`

### Access the Application

- **Movie List**: http://localhost:8080/movies
- **Movie Details**: http://localhost:8080/movies/{id}/details (where {id} is 1-12)

## Building for Production

```bash
mvn clean package
java -jar target/sample-qdev-movies-0.1.0.jar
```

## Project Structure

```
src/
├── main/
│   ├── java/
│   │   └── com/amazonaws/samples/qdevmovies/
│   │       ├── MoviesApplication.java    # Main Spring Boot application
│   │       ├── MoviesController.java     # REST controller for movie endpoints
│   │       ├── Movie.java                # Movie data model
│   │       ├── Review.java               # Review data model
│   │       └── utils/
│   │           ├── HTMLBuilder.java      # HTML generation utilities
│   │           └── MovieUtils.java       # Movie validation utilities
│   └── resources/
│       ├── application.yml               # Application configuration
│       ├── mock-reviews.json             # Mock review data
│       └── log4j2.xml                    # Logging configuration
└── test/                                 # Unit tests
```

## API Endpoints

### Get All Movies
```
GET /movies
```
Returns an HTML page displaying all movies with ratings and basic information.

### Get Movie Details
```
GET /movies/{id}/details
```
Returns an HTML page with detailed movie information and customer reviews.

**Parameters:**
- `id` (path parameter): Movie ID (1-12)

**Example:**
```
http://localhost:8080/movies/1/details
```

## Troubleshooting

### Port 8080 already in use

Run on a different port:
```bash
mvn spring-boot:run -Dspring-boot.run.arguments=--server.port=8081
```

### Build failures

Clean and rebuild:
```bash
mvn clean compile
```

## Contributing

This project is designed as a demonstration application. Feel free to:
- Add more movies to the catalog
- Enhance the UI/UX
- Add new features like search or filtering
- Improve the responsive design

## License

This sample code is licensed under the MIT-0 License. See the LICENSE file.
