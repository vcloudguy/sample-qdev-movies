package com.amazonaws.samples.qdevmovies.movies;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Ahoy matey! These be the tests for our MovieService treasure hunt functionality.
 * Testing all the search methods to make sure they work ship-shape!
 */
@DisplayName("MovieService Treasure Hunt Tests")
public class MovieServiceTest {

    private MovieService movieService;

    @BeforeEach
    public void setUp() {
        movieService = new MovieService();
    }

    @Test
    @DisplayName("Should load all movie treasures from JSON")
    public void testGetAllMovies() {
        List<Movie> movies = movieService.getAllMovies();
        assertNotNull(movies, "Movie treasure chest should not be null, arrr!");
        assertFalse(movies.isEmpty(), "Movie treasure chest should not be empty, ye scurvy dog!");
        assertEquals(12, movies.size(), "Should have 12 movie treasures in the chest");
    }

    @Test
    @DisplayName("Should find movie treasure by valid ID")
    public void testGetMovieByValidId() {
        Optional<Movie> movie = movieService.getMovieById(1L);
        assertTrue(movie.isPresent(), "Should find movie treasure with ID 1");
        assertEquals("The Prison Escape", movie.get().getMovieName());
        assertEquals("Drama", movie.get().getGenre());
    }

    @Test
    @DisplayName("Should return empty for invalid movie ID")
    public void testGetMovieByInvalidId() {
        Optional<Movie> movie = movieService.getMovieById(999L);
        assertFalse(movie.isPresent(), "Should not find movie treasure with invalid ID");
    }

    @Test
    @DisplayName("Should return empty for null or negative ID")
    public void testGetMovieByNullOrNegativeId() {
        Optional<Movie> nullMovie = movieService.getMovieById(null);
        assertFalse(nullMovie.isPresent(), "Should not find movie treasure with null ID");
        
        Optional<Movie> negativeMovie = movieService.getMovieById(-1L);
        assertFalse(negativeMovie.isPresent(), "Should not find movie treasure with negative ID");
    }

    @Test
    @DisplayName("Should search movie treasures by name (case insensitive)")
    public void testSearchMovieTreasuresByName() {
        List<Movie> results = movieService.searchMovieTreasures("prison", null, null);
        assertEquals(1, results.size(), "Should find 1 movie treasure with 'prison' in name");
        assertEquals("The Prison Escape", results.get(0).getMovieName());
        
        // Test case insensitive search
        List<Movie> caseResults = movieService.searchMovieTreasures("PRISON", null, null);
        assertEquals(1, caseResults.size(), "Should find movie treasure regardless of case");
        
        // Test partial match
        List<Movie> partialResults = movieService.searchMovieTreasures("the", null, null);
        assertTrue(partialResults.size() > 1, "Should find multiple movies with 'the' in name");
    }

    @Test
    @DisplayName("Should search movie treasures by genre")
    public void testSearchMovieTreasuresByGenre() {
        List<Movie> dramaResults = movieService.searchMovieTreasures(null, null, "Drama");
        assertTrue(dramaResults.size() >= 2, "Should find multiple drama movie treasures");
        
        // Test partial genre match
        List<Movie> crimeResults = movieService.searchMovieTreasures(null, null, "Crime");
        assertTrue(crimeResults.size() >= 1, "Should find crime movie treasures");
        
        // Test case insensitive genre search
        List<Movie> caseResults = movieService.searchMovieTreasures(null, null, "drama");
        assertEquals(dramaResults.size(), caseResults.size(), "Genre search should be case insensitive");
    }

    @Test
    @DisplayName("Should search movie treasures by specific ID")
    public void testSearchMovieTreasuresById() {
        List<Movie> results = movieService.searchMovieTreasures(null, 1L, null);
        assertEquals(1, results.size(), "Should find exactly 1 movie treasure with ID 1");
        assertEquals("The Prison Escape", results.get(0).getMovieName());
        
        // Test with invalid ID
        List<Movie> invalidResults = movieService.searchMovieTreasures(null, 999L, null);
        assertTrue(invalidResults.isEmpty(), "Should find no treasures with invalid ID");
    }

    @Test
    @DisplayName("Should combine name and genre search criteria")
    public void testSearchMovieTreasuresCombined() {
        // Search for movies with "the" in name and "Drama" genre
        List<Movie> results = movieService.searchMovieTreasures("the", null, "Drama");
        assertFalse(results.isEmpty(), "Should find movie treasures matching both criteria");
        
        // Verify all results match both criteria
        for (Movie movie : results) {
            assertTrue(movie.getMovieName().toLowerCase().contains("the"), 
                      "Movie name should contain 'the'");
            assertTrue(movie.getGenre().toLowerCase().contains("drama"), 
                      "Movie genre should contain 'drama'");
        }
    }

    @Test
    @DisplayName("Should return empty list for no matching criteria")
    public void testSearchMovieTreasuresNoMatch() {
        List<Movie> results = movieService.searchMovieTreasures("nonexistent", null, null);
        assertTrue(results.isEmpty(), "Should return empty treasure chest for non-matching name");
        
        List<Movie> genreResults = movieService.searchMovieTreasures(null, null, "NonexistentGenre");
        assertTrue(genreResults.isEmpty(), "Should return empty treasure chest for non-matching genre");
    }

    @Test
    @DisplayName("Should return all movies when no search criteria provided")
    public void testSearchMovieTreasuresNoCriteria() {
        List<Movie> results = movieService.searchMovieTreasures(null, null, null);
        assertEquals(movieService.getAllMovies().size(), results.size(), 
                    "Should return all movie treasures when no criteria provided");
    }

    @Test
    @DisplayName("Should handle empty string search criteria")
    public void testSearchMovieTreasuresEmptyStrings() {
        List<Movie> results = movieService.searchMovieTreasures("", null, "");
        assertEquals(movieService.getAllMovies().size(), results.size(), 
                    "Should return all movie treasures for empty string criteria");
        
        List<Movie> whitespaceResults = movieService.searchMovieTreasures("   ", null, "   ");
        assertEquals(movieService.getAllMovies().size(), whitespaceResults.size(), 
                    "Should return all movie treasures for whitespace-only criteria");
    }

    @Test
    @DisplayName("Should get all unique genre treasures")
    public void testGetAllGenreTreasures() {
        List<String> genres = movieService.getAllGenreTreasures();
        assertNotNull(genres, "Genre treasure list should not be null");
        assertFalse(genres.isEmpty(), "Should have genre treasures available");
        
        // Check for expected genres
        assertTrue(genres.contains("Drama"), "Should contain Drama genre");
        assertTrue(genres.contains("Crime/Drama"), "Should contain Crime/Drama genre");
        assertTrue(genres.contains("Action/Sci-Fi"), "Should contain Action/Sci-Fi genre");
        
        // Verify uniqueness and sorting
        long uniqueCount = genres.stream().distinct().count();
        assertEquals(genres.size(), uniqueCount, "All genres should be unique");
        
        // Check if sorted (assuming natural string ordering)
        for (int i = 1; i < genres.size(); i++) {
            assertTrue(genres.get(i-1).compareTo(genres.get(i)) <= 0, 
                      "Genres should be sorted alphabetically");
        }
    }

    @Test
    @DisplayName("Should validate search criteria properly")
    public void testIsValidSearchCriteria() {
        // Valid criteria
        assertTrue(movieService.isValidSearchCriteria("movie", null, null), 
                  "Should be valid with movie name");
        assertTrue(movieService.isValidSearchCriteria(null, 1L, null), 
                  "Should be valid with movie ID");
        assertTrue(movieService.isValidSearchCriteria(null, null, "Drama"), 
                  "Should be valid with genre");
        assertTrue(movieService.isValidSearchCriteria("movie", 1L, "Drama"), 
                  "Should be valid with all criteria");
        
        // Invalid criteria
        assertFalse(movieService.isValidSearchCriteria(null, null, null), 
                   "Should be invalid with all null criteria");
        assertFalse(movieService.isValidSearchCriteria("", null, ""), 
                   "Should be invalid with empty string criteria");
        assertFalse(movieService.isValidSearchCriteria("   ", null, "   "), 
                   "Should be invalid with whitespace-only criteria");
        assertFalse(movieService.isValidSearchCriteria(null, 0L, null), 
                   "Should be invalid with zero ID");
        assertFalse(movieService.isValidSearchCriteria(null, -1L, null), 
                   "Should be invalid with negative ID");
    }

    @Test
    @DisplayName("Should handle ID priority in search (ID overrides other criteria)")
    public void testSearchMovieTreasuresIdPriority() {
        // When ID is provided, it should override other criteria
        List<Movie> results = movieService.searchMovieTreasures("SomeOtherMovie", 1L, "SomeOtherGenre");
        assertEquals(1, results.size(), "Should find exactly 1 movie treasure when ID is provided");
        assertEquals("The Prison Escape", results.get(0).getMovieName(), 
                    "Should return movie with specified ID, ignoring other criteria");
    }
}