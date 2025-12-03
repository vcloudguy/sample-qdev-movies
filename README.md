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

## Technology Stack

- **Java 8**
- **Spring Boot 2.0.5**
- **Maven** for dependency management
- **Log4j 2.20.0**
- **JUnit 5.8.2**
- **Thymeleaf** for templating
- **Pirate Language Integration** 🏴‍☠️

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
- **Movie Search**: http://localhost:8080/movies/search (with query parameters)
- **Movie Details**: http://localhost:8080/movies/{id}/details (where {id} is 1-12)

## 🏴‍☠️ Movie Search API - Treasure Hunt Guide

### Search Endpoint
```
GET /movies/search
```

Ahoy matey! This endpoint allows ye to search for movie treasures using various criteria. The search supports partial matching and is case-insensitive, arrr!

### Query Parameters

| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| `name` | String | No | Movie name to search for (partial matches allowed, ye scurvy dog!) |
| `id` | Long | No | Specific movie ID to find (overrides other parameters, arrr!) |
| `genre` | String | No | Genre to filter by (partial matches allowed, me hearty!) |

### Search Examples

#### Search by Movie Name
```bash
# Find movies with "pirate" in the name
curl "http://localhost:8080/movies/search?name=pirate"

# Case-insensitive search for "THE"
curl "http://localhost:8080/movies/search?name=the"
```

#### Search by Genre
```bash
# Find all Drama movies
curl "http://localhost:8080/movies/search?genre=Drama"

# Find movies with "Crime" in genre
curl "http://localhost:8080/movies/search?genre=Crime"
```

#### Search by Movie ID
```bash
# Find specific movie by ID (highest priority)
curl "http://localhost:8080/movies/search?id=1"
```

#### Combined Search
```bash
# Search for movies with "the" in name AND "Drama" genre
curl "http://localhost:8080/movies/search?name=the&genre=Drama"

# Note: ID parameter overrides other parameters
curl "http://localhost:8080/movies/search?name=something&id=1&genre=Action"
# This will return only the movie with ID 1, ignoring name and genre
```

### Search Response

The search endpoint returns an HTML page with:
- **Search Form**: Pirate-themed form with all search fields pre-populated
- **Search Results**: Movie cards matching the search criteria
- **Pirate Messages**: Success, error, or informational messages in pirate language
- **Genre Dropdown**: Populated with all available genres for easy filtering

### Search Behavior

1. **ID Priority**: If an ID is provided, it takes precedence over all other parameters
2. **Partial Matching**: Name and genre searches support partial, case-insensitive matching
3. **Combined Filters**: When multiple parameters are provided (except ID), all must match
4. **Empty Results**: Returns pirate-themed message when no movies match the criteria
5. **Invalid Criteria**: Shows error message if no valid search parameters are provided

### Pirate Language Features

The search functionality includes authentic pirate language throughout:
- **Success Messages**: "Arrr! Found X movie treasures matching yer search!"
- **Error Messages**: "Shiver me timbers! No movie treasures found..."
- **Form Labels**: "Movie Name (Arrr!)", "Genre (Treasure Type)", etc.
- **Buttons**: "🔍 Hunt for Treasures!", "🗺️ Show All Treasures"

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
│   │       ├── movies/
│   │       │   ├── MoviesApplication.java    # Main Spring Boot application
│   │       │   ├── MoviesController.java     # REST controller with search endpoint
│   │       │   ├── MovieService.java         # Service with search functionality
│   │       │   ├── Movie.java                # Movie data model
│   │       │   ├── Review.java               # Review data model
│   │       │   └── ReviewService.java        # Review service
│   │       └── utils/
│   │           ├── MovieIconUtils.java       # Movie icon utilities
│   │           └── MovieUtils.java           # Movie validation utilities
│   └── resources/
│       ├── application.yml                   # Application configuration
│       ├── movies.json                       # Movie data with 12 movies
│       ├── mock-reviews.json                 # Mock review data
│       ├── log4j2.xml                        # Logging configuration
│       └── templates/
│           ├── movies.html                   # Enhanced with search form
│           └── movie-details.html            # Movie details template
└── test/                                     # Comprehensive unit tests
    └── java/
        └── com/amazonaws/samples/qdevmovies/movies/
            ├── MovieServiceTest.java         # Service layer tests
            └── MoviesControllerTest.java     # Controller tests with search
```

## API Endpoints

### Get All Movies
```
GET /movies
```
Returns an HTML page displaying all movies with ratings, basic information, and the pirate-themed search form.

### Search Movie Treasures 🔍
```
GET /movies/search?name={name}&id={id}&genre={genre}
```
Returns an HTML page with filtered movie results based on search criteria.

**Parameters:**
- `name` (optional): Movie name to search for (partial matching)
- `id` (optional): Specific movie ID (1-12, overrides other parameters)
- `genre` (optional): Genre to filter by (partial matching)

**Examples:**
```
http://localhost:8080/movies/search?name=prison
http://localhost:8080/movies/search?genre=Drama
http://localhost:8080/movies/search?id=1
http://localhost:8080/movies/search?name=the&genre=Crime
```

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

## Available Movie Data

The application includes 12 movies across various genres:

| ID | Movie Name | Genre | Year | Rating |
|----|------------|-------|------|--------|
| 1 | The Prison Escape | Drama | 1994 | 5.0/5 |
| 2 | The Family Boss | Crime/Drama | 1972 | 5.0/5 |
| 3 | The Masked Hero | Action/Crime | 2008 | 5.0/5 |
| 4 | Urban Stories | Crime/Drama | 1994 | 4.5/5 |
| 5 | Life Journey | Drama/Romance | 1994 | 4.0/5 |
| 6 | Dream Heist | Action/Sci-Fi | 2010 | 4.5/5 |
| 7 | The Virtual World | Action/Sci-Fi | 1999 | 4.5/5 |
| 8 | The Wise Guys | Crime/Drama | 1990 | 4.5/5 |
| 9 | The Quest for the Ring | Adventure/Fantasy | 2001 | 4.5/5 |
| 10 | Space Wars: The Beginning | Adventure/Sci-Fi | 1977 | 4.0/5 |
| 11 | The Factory Owner | Drama/History | 1993 | 4.5/5 |
| 12 | Underground Club | Drama/Thriller | 1999 | 4.5/5 |

## Testing

Run the comprehensive test suite:

```bash
# Run all tests
mvn test

# Run specific test classes
mvn test -Dtest=MovieServiceTest
mvn test -Dtest=MoviesControllerTest
```

The test suite includes:
- **MovieServiceTest**: Tests for search functionality, genre filtering, and validation
- **MoviesControllerTest**: Tests for search endpoint, parameter handling, and pirate messages
- **Edge Case Testing**: Empty results, invalid parameters, and error handling

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

### Search not working

1. Verify the application is running on the correct port
2. Check that at least one search parameter is provided
3. Ensure movie data is loaded (check logs for "Ahoy! Starting treasure hunt...")

## Contributing

This project demonstrates modern Spring Boot development with pirate-themed search functionality. Feel free to:
- Add more movies to the catalog
- Enhance the pirate-themed UI/UX
- Add new search features (director, year range, rating filters)
- Improve the responsive design
- Add more pirate language elements
- Implement advanced filtering options

## License

This sample code is licensed under the MIT-0 License. See the LICENSE file.

---

**Arrr! May yer movie treasure hunts be successful, matey! 🏴‍☠️**
