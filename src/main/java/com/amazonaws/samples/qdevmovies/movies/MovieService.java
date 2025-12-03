package com.amazonaws.samples.qdevmovies.movies;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONException;
import org.springframework.stereotype.Service;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Scanner;
import java.util.stream.Collectors;

@Service
public class MovieService {
    private static final Logger logger = LogManager.getLogger(MovieService.class);
    private final List<Movie> movies;
    private final Map<Long, Movie> movieMap;

    public MovieService() {
        this.movies = loadMoviesFromJson();
        this.movieMap = new HashMap<>();
        for (Movie movie : movies) {
            movieMap.put(movie.getId(), movie);
        }
    }

    private List<Movie> loadMoviesFromJson() {
        List<Movie> movieList = new ArrayList<>();
        try {
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream("movies.json");
            if (inputStream == null) {
                throw new MovieDataLoadException("Arrr! Movie treasure chest file 'movies.json' not found in resources, matey!");
            }
            
            Scanner scanner = new Scanner(inputStream, StandardCharsets.UTF_8.name());
            String jsonContent = scanner.useDelimiter("\\A").next();
            scanner.close();
            
            JSONArray moviesArray = new JSONArray(jsonContent);
            for (int i = 0; i < moviesArray.length(); i++) {
                JSONObject movieObj = moviesArray.getJSONObject(i);
                movieList.add(new Movie(
                    movieObj.getLong("id"),
                    movieObj.getString("movieName"),
                    movieObj.getString("director"),
                    movieObj.getInt("year"),
                    movieObj.getString("genre"),
                    movieObj.getString("description"),
                    movieObj.getInt("duration"),
                    movieObj.getDouble("imdbRating")
                ));
            }
        } catch (JSONException e) {
            logger.error("Scurvy bug in JSON parsing: {}", e.getMessage(), e);
            throw new MovieDataLoadException("Blimey! Invalid JSON format in movie treasure chest", e);
        } catch (IOException e) {
            logger.error("IO error loading movie treasures: {}", e.getMessage(), e);
            throw new MovieDataLoadException("Arrr! Failed to read movie treasure chest file", e);
        } catch (Exception e) {
            logger.error("Unexpected error loading movie treasures: {}", e.getMessage(), e);
            throw new MovieDataLoadException("Shiver me timbers! Unexpected error loading movie data", e);
        }
        return movieList;
    }

    public List<Movie> getAllMovies() {
        return movies;
    }

    public Optional<Movie> getMovieById(Long id) {
        if (id == null || id <= 0) {
            return Optional.empty();
        }
        return Optional.ofNullable(movieMap.get(id));
    }

    /**
     * Searches for movies based on the provided criteria, matey!
     * This method be the treasure map to find yer desired movies.
     * 
     * @param name The movie name to search for (partial matches allowed, arrr!)
     * @param id The specific movie ID to find
     * @param genre The genre to filter by (partial matches allowed)
     * @return List of movies matching the search criteria
     * @throws InvalidSearchCriteriaException if no valid search criteria provided
     */
    public List<Movie> searchMovieTreasures(String name, Long id, String genre) {
        logger.info("Ahoy! Starting treasure hunt for movies with name: '{}', id: '{}', genre: '{}'", 
                   name, id, genre);
        
        // Validate search criteria first, ye scurvy dog!
        if (!isValidSearchCriteria(name, id, genre)) {
            throw new InvalidSearchCriteriaException();
        }
        
        List<Movie> treasureChest = new ArrayList<>(movies);
        
        try {
            // Filter by ID first, as it be the most specific search, arrr!
            if (id != null && id > 0) {
                logger.debug("Searching for movie treasure with ID: {}", id);
                Optional<Movie> movieTreasure = getMovieById(id);
                if (movieTreasure.isPresent()) {
                    treasureChest = List.of(movieTreasure.get());
                    logger.info("Found movie treasure with ID {}: '{}'", id, movieTreasure.get().getMovieName());
                } else {
                    logger.warn("No treasure found with ID: {}", id);
                    treasureChest = new ArrayList<>();
                }
            } else {
                // Filter by name if provided, ye scurvy dog!
                if (name != null && !name.trim().isEmpty()) {
                    String searchName = name.trim().toLowerCase();
                    logger.debug("Filtering treasure chest by name containing: '{}'", searchName);
                    treasureChest = treasureChest.stream()
                        .filter(movie -> movie.getMovieName().toLowerCase().contains(searchName))
                        .collect(Collectors.toList());
                    logger.debug("Found {} movies matching name criteria", treasureChest.size());
                }
                
                // Filter by genre if provided, me hearty!
                if (genre != null && !genre.trim().isEmpty()) {
                    String searchGenre = genre.trim().toLowerCase();
                    logger.debug("Filtering treasure chest by genre containing: '{}'", searchGenre);
                    treasureChest = treasureChest.stream()
                        .filter(movie -> movie.getGenre().toLowerCase().contains(searchGenre))
                        .collect(Collectors.toList());
                    logger.debug("Found {} movies matching genre criteria", treasureChest.size());
                }
            }
        } catch (IllegalArgumentException e) {
            logger.error("Invalid search parameter provided: {}", e.getMessage(), e);
            throw new InvalidSearchCriteriaException("Arrr! Invalid search parameters provided, matey!", e);
        }
        
        logger.info("Treasure hunt complete! Found {} movie treasures matching yer criteria", treasureChest.size());
        return treasureChest;
    }

    /**
     * Gets all available genres from the movie treasure chest, arrr!
     * Useful for populating search forms and helping landlubbers find their preferred genres.
     * 
     * @return List of unique genres available in the movie collection
     */
    public List<String> getAllGenreTreasures() {
        logger.debug("Gathering all genre treasures from the movie chest");
        try {
            List<String> genres = movies.stream()
                .map(Movie::getGenre)
                .distinct()
                .sorted()
                .collect(Collectors.toList());
            logger.debug("Found {} unique genre treasures", genres.size());
            return genres;
        } catch (IllegalStateException e) {
            logger.error("Stream processing error gathering genre treasures: {}", e.getMessage(), e);
            // Return empty list instead of throwing exception for non-critical operation
            return new ArrayList<>();
        } catch (NullPointerException e) {
            logger.error("Null pointer error gathering genre treasures: {}", e.getMessage(), e);
            // Return empty list instead of throwing exception for non-critical operation
            return new ArrayList<>();
        }
    }

    /**
     * Validates search parameters to prevent scurvy bugs, matey!
     * 
     * @param name The movie name parameter
     * @param id The movie ID parameter  
     * @param genre The genre parameter
     * @return true if at least one valid search parameter is provided
     */
    public boolean isValidSearchCriteria(String name, Long id, String genre) {
        boolean hasValidName = name != null && !name.trim().isEmpty();
        boolean hasValidId = id != null && id > 0;
        boolean hasValidGenre = genre != null && !genre.trim().isEmpty();
        
        boolean isValid = hasValidName || hasValidId || hasValidGenre;
        logger.debug("Search criteria validation - Name: {}, ID: {}, Genre: {}, Valid: {}", 
                    hasValidName, hasValidId, hasValidGenre, isValid);
        
        return isValid;
    }
}
