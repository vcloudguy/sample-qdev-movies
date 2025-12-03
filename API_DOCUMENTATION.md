# 🏴‍☠️ Movie Search API Documentation

Ahoy matey! Welcome to the comprehensive API documentation for our pirate-themed movie search and filtering system. This treasure map will guide ye through all the endpoints and functionality available in our movie service.

## Table of Contents

- [Overview](#overview)
- [Base URL](#base-url)
- [Authentication](#authentication)
- [Endpoints](#endpoints)
- [Data Models](#data-models)
- [Error Handling](#error-handling)
- [Examples](#examples)
- [Pirate Language Guide](#pirate-language-guide)

## Overview

The Movie Search API provides a comprehensive system for searching and filtering movie treasures. Built with Spring Boot and enhanced with pirate-themed language, this API supports:

- **Full-text search** across movie names
- **Genre-based filtering** with partial matching
- **ID-based lookup** for specific movies
- **Combined search criteria** for advanced filtering
- **Pirate-themed responses** for an engaging user experience

## Base URL

```
http://localhost:8080
```

For production deployments, replace with your actual domain.

## Authentication

Currently, no authentication is required for accessing the movie search API. All endpoints are publicly accessible, ye scurvy dogs!

## Endpoints

### 1. Get All Movies

**Endpoint:** `GET /movies`

**Description:** Returns an HTML page displaying all available movie treasures with the search form.

**Parameters:** None

**Response:** HTML page with movie grid and search form

**Specific Exceptions:**
- `MovieDataLoadException`: When movie data cannot be loaded from storage
- `IOException`: When there are file system access issues

**Example:**
```bash
curl -X GET "http://localhost:8080/movies"
```

---

### 2. Search Movie Treasures

**Endpoint:** `GET /movies/search`

**Description:** Searches for movie treasures based on provided criteria. This be the main treasure hunting endpoint, arrr!

**Parameters:**

| Parameter | Type | Required | Description | Example |
|-----------|------|----------|-------------|---------|
| `name` | String | No | Movie name to search for (case-insensitive, partial matching) | `prison`, `the`, `HERO` |
| `id` | Long | No | Specific movie ID (1-12, overrides other parameters) | `1`, `5`, `12` |
| `genre` | String | No | Genre to filter by (case-insensitive, partial matching) | `Drama`, `crime`, `ACTION` |

**Search Priority:**
1. If `id` is provided, it takes highest priority and ignores other parameters
2. If `name` and/or `genre` are provided, both must match (AND operation)
3. All searches are case-insensitive and support partial matching

**Response:** HTML page with filtered results and pirate-themed messages

**Specific Exceptions:**
- `InvalidSearchCriteriaException`: When no valid search parameters are provided
- `MovieNotFoundException`: When searching by ID with non-existent movie
- `MovieDataLoadException`: When movie data cannot be accessed during search
- `IllegalArgumentException`: When search parameters contain invalid values

**Examples:**

```bash
# Search by movie name
curl -X GET "http://localhost:8080/movies/search?name=prison"

# Search by genre
curl -X GET "http://localhost:8080/movies/search?genre=Drama"

# Search by specific ID
curl -X GET "http://localhost:8080/movies/search?id=1"

# Combined search (name AND genre)
curl -X GET "http://localhost:8080/movies/search?name=the&genre=Crime"

# URL encoded for spaces and special characters
curl -X GET "http://localhost:8080/movies/search?name=Space%20Wars"
```

---

### 3. Get Movie Details

**Endpoint:** `GET /movies/{id}/details`

**Description:** Returns detailed information about a specific movie treasure, including reviews.

**Parameters:**

| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| `id` | Long | Yes | Movie ID (path parameter, 1-12) |

**Response:** HTML page with detailed movie information

**Specific Exceptions:**
- `MovieNotFoundException`: When the specified movie ID doesn't exist
- `MovieDataLoadException`: When movie data cannot be loaded
- `IllegalArgumentException`: When the ID parameter is invalid

**Example:**
```bash
curl -X GET "http://localhost:8080/movies/1/details"
```

## Data Models

### Movie

```json
{
  "id": 1,
  "movieName": "The Prison Escape",
  "director": "John Director",
  "year": 1994,
  "genre": "Drama",
  "description": "Two imprisoned men bond over a number of years...",
  "duration": 142,
  "imdbRating": 5.0,
  "icon": "🎬"
}
```

### Available Genres

The following genres are available in the movie treasure chest:

- `Action/Crime`
- `Action/Sci-Fi`
- `Adventure/Fantasy`
- `Adventure/Sci-Fi`
- `Crime/Drama`
- `Drama`
- `Drama/History`
- `Drama/Romance`
- `Drama/Thriller`

## Error Handling

### Invalid Search Criteria

When no valid search parameters are provided:

**Exception:** `InvalidSearchCriteriaException`
**Response:** HTML page with error message
**Message:** "Arrr! Ye need to provide at least one search criterion, matey!"

### Movie Not Found

When searching by ID with non-existent movie:

**Exception:** `MovieNotFoundException`
**Response:** HTML page with error message
**Message:** "Shiver me timbers! No movie treasures found matching yer search criteria."

### Empty Search Results

When search criteria don't match any movies:

**Response:** HTML page with informational message
**Message:** "Shiver me timbers! No movie treasures found matching yer search criteria. Try adjusting yer search terms, ye landlubber!"

### Data Loading Errors

When movie data cannot be loaded:

**Exception:** `MovieDataLoadException`
**Response:** HTML page with error message
**Message:** "Blimey! A scurvy bug with the movie data prevented the treasure hunt. Please try again, me hearty!"

### Invalid Parameters

When search parameters contain invalid values:

**Exception:** `IllegalArgumentException`
**Response:** HTML page with error message
**Message:** "Arrr! Invalid search parameters provided, matey! Check yer input and try again."

## Examples

### Basic Search Operations

#### 1. Find movies with "the" in the name
```bash
curl "http://localhost:8080/movies/search?name=the"
```
**Expected Results:** Multiple movies including "The Prison Escape", "The Family Boss", etc.

#### 2. Find all Drama movies
```bash
curl "http://localhost:8080/movies/search?genre=Drama"
```
**Expected Results:** Movies with "Drama" in their genre field

#### 3. Find specific movie by ID
```bash
curl "http://localhost:8080/movies/search?id=1"
```
**Expected Results:** Single movie with ID 1 ("The Prison Escape")

### Advanced Search Operations

#### 1. Case-insensitive search
```bash
curl "http://localhost:8080/movies/search?name=PRISON"
curl "http://localhost:8080/movies/search?genre=drama"
```

#### 2. Partial matching
```bash
curl "http://localhost:8080/movies/search?genre=Crime"  # Matches "Crime/Drama"
curl "http://localhost:8080/movies/search?name=Hero"   # Matches "The Masked Hero"
```

#### 3. Combined criteria
```bash
curl "http://localhost:8080/movies/search?name=the&genre=Drama"
```
**Expected Results:** Movies that have "the" in name AND "Drama" in genre

#### 4. ID priority demonstration
```bash
curl "http://localhost:8080/movies/search?name=something&id=1&genre=Action"
```
**Expected Results:** Only movie with ID 1, ignoring name and genre parameters

### Edge Cases

#### 1. No matching results
```bash
curl "http://localhost:8080/movies/search?name=nonexistent"
```

#### 2. Empty parameters
```bash
curl "http://localhost:8080/movies/search?name=&genre="
```

#### 3. Invalid ID
```bash
curl "http://localhost:8080/movies/search?id=999"
```

## Pirate Language Guide

The API incorporates authentic pirate language throughout the user experience:

### Common Pirate Terms Used

| Pirate Term | Meaning | Usage Context |
|-------------|---------|---------------|
| Ahoy! | Hello/Greeting | Log messages, success responses |
| Arrr! | Expression of excitement | Success messages |
| Matey/Me hearty | Friend | User addressing |
| Ye scurvy dog | Playful insult | Form labels, casual addressing |
| Shiver me timbers! | Expression of surprise | Empty results |
| Blimey! | Expression of surprise/dismay | Error messages |
| Batten down the hatches! | Prepare for action | Multiple results found |
| Landlubber | Someone unfamiliar with the sea | Error guidance |
| Treasure chest | Collection/database | Movie collection references |
| Treasure hunt | Search operation | Search functionality |

### Message Examples

**Success Messages:**
- "Arrr! Found 1 movie treasure matching yer search!"
- "Batten down the hatches! Found 5 movie treasures matching yer search!"

**Error Messages:**
- "Arrr! Ye need to provide at least one search criterion, matey!"
- "Blimey! A scurvy bug with the movie data prevented the treasure hunt. Please try again, me hearty!"

**Information Messages:**
- "Shiver me timbers! No movie treasures found matching yer search criteria. Try adjusting yer search terms, ye landlubber!"

## Integration Examples

### JavaScript/AJAX Integration

```javascript
// Search for movies using fetch API
async function searchMovies(name, genre, id) {
    const params = new URLSearchParams();
    if (name) params.append('name', name);
    if (genre) params.append('genre', genre);
    if (id) params.append('id', id);
    
    try {
        const response = await fetch(`/movies/search?${params}`);
        return response.text(); // Returns HTML
    } catch (error) {
        console.error('Search failed:', error);
        throw new Error('Arrr! Search treasure hunt failed, matey!');
    }
}

// Example usage
searchMovies('prison', null, null)
    .then(html => {
        document.getElementById('results').innerHTML = html;
    })
    .catch(error => {
        console.error('Treasure hunt error:', error);
    });
```

### Form Integration

```html
<form action="/movies/search" method="get">
    <input type="text" name="name" placeholder="Movie name, ye scurvy dog...">
    <input type="number" name="id" placeholder="Movie ID, matey..." min="1">
    <select name="genre">
        <option value="">All Genres, me hearty!</option>
        <option value="Drama">Drama</option>
        <option value="Action">Action</option>
        <!-- More options... -->
    </select>
    <button type="submit">🔍 Hunt for Treasures!</button>
</form>
```

## Performance Considerations

- **Caching:** Movie data is loaded once at startup and cached in memory
- **Search Performance:** All searches are performed in-memory for fast response times
- **Concurrent Requests:** The service can handle multiple concurrent search requests
- **Memory Usage:** Minimal memory footprint with 12 movies in the dataset

## Rate Limiting

Currently, no rate limiting is implemented. For production use, consider implementing:
- Request rate limiting per IP
- Search query complexity limits
- Concurrent request limits per user

## Future Enhancements

Potential improvements for the treasure hunt system:
- **Director Search:** Filter by movie director
- **Year Range:** Search movies by release year range
- **Rating Filter:** Filter by minimum IMDB rating
- **Advanced Sorting:** Sort results by rating, year, or name
- **Fuzzy Search:** Handle typos and similar spellings
- **Search History:** Track and suggest previous searches

---

**Arrr! May this documentation guide ye to successful treasure hunts, matey! 🏴‍☠️**

For technical support or questions about the API, check the application logs for detailed pirate-themed debugging information!