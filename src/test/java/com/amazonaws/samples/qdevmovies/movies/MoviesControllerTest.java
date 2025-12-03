package com.amazonaws.samples.qdevmovies.movies;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.springframework.ui.Model;
import org.springframework.ui.ExtendedModelMap;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Ahoy matey! These be the tests for our MoviesController treasure hunt endpoints.
 * Testing all the controller methods to make sure they handle requests ship-shape!
 */
@DisplayName("MoviesController Treasure Hunt Tests")
public class MoviesControllerTest {

    private MoviesController moviesController;
    private Model model;
    private MovieService mockMovieService;
    private ReviewService mockReviewService;

    @BeforeEach
    public void setUp() {
        moviesController = new MoviesController();
        model = new ExtendedModelMap();
        
        // Create mock services with pirate-themed test data
        mockMovieService = new MovieService() {
            @Override
            public List<Movie> getAllMovies() {
                return Arrays.asList(
                    new Movie(1L, "The Pirate's Treasure", "Captain Director", 2023, "Adventure", "A pirate's quest for treasure", 120, 4.5),
                    new Movie(2L, "Sea Battle", "Admiral Filmmaker", 2022, "Action", "Epic naval battles", 140, 4.0)
                );
            }
            
            @Override
            public Optional<Movie> getMovieById(Long id) {
                if (id == 1L) {
                    return Optional.of(new Movie(1L, "The Pirate's Treasure", "Captain Director", 2023, "Adventure", "A pirate's quest for treasure", 120, 4.5));
                } else if (id == 2L) {
                    return Optional.of(new Movie(2L, "Sea Battle", "Admiral Filmmaker", 2022, "Action", "Epic naval battles", 140, 4.0));
                }
                return Optional.empty();
            }
            
            @Override
            public List<Movie> searchMovieTreasures(String name, Long id, String genre) {
                List<Movie> allMovies = getAllMovies();
                List<Movie> results = new ArrayList<>();
                
                if (id != null && id > 0) {
                    Optional<Movie> movie = getMovieById(id);
                    if (movie.isPresent()) {
                        results.add(movie.get());
                    }
                    return results;
                }
                
                for (Movie movie : allMovies) {
                    boolean matches = true;
                    
                    if (name != null && !name.trim().isEmpty()) {
                        matches = matches && movie.getMovieName().toLowerCase().contains(name.toLowerCase());
                    }
                    
                    if (genre != null && !genre.trim().isEmpty()) {
                        matches = matches && movie.getGenre().toLowerCase().contains(genre.toLowerCase());
                    }
                    
                    if (matches) {
                        results.add(movie);
                    }
                }
                
                return results;
            }
            
            @Override
            public List<String> getAllGenreTreasures() {
                return Arrays.asList("Action", "Adventure", "Drama");
            }
            
            @Override
            public boolean isValidSearchCriteria(String name, Long id, String genre) {
                boolean hasValidName = name != null && !name.trim().isEmpty();
                boolean hasValidId = id != null && id > 0;
                boolean hasValidGenre = genre != null && !genre.trim().isEmpty();
                return hasValidName || hasValidId || hasValidGenre;
            }
        };
        
        mockReviewService = new ReviewService() {
            @Override
            public List<Review> getReviewsForMovie(long movieId) {
                return new ArrayList<>();
            }
        };
        
        // Inject mocks using reflection
        try {
            java.lang.reflect.Field movieServiceField = MoviesController.class.getDeclaredField("movieService");
            movieServiceField.setAccessible(true);
            movieServiceField.set(moviesController, mockMovieService);
            
            java.lang.reflect.Field reviewServiceField = MoviesController.class.getDeclaredField("reviewService");
            reviewServiceField.setAccessible(true);
            reviewServiceField.set(moviesController, mockReviewService);
        } catch (Exception e) {
            throw new RuntimeException("Failed to inject mock services", e);
        }
    }

    @Test
    @DisplayName("Should get all movies and genres for main page")
    public void testGetMovies() {
        String result = moviesController.getMovies(model);
        
        assertNotNull(result, "Result should not be null, arrr!");
        assertEquals("movies", result, "Should return movies template");
        
        // Verify model attributes
        assertTrue(model.containsAttribute("movies"), "Model should contain movies attribute");
        assertTrue(model.containsAttribute("genres"), "Model should contain genres attribute");
        
        @SuppressWarnings("unchecked")
        List<Movie> movies = (List<Movie>) model.asMap().get("movies");
        assertEquals(2, movies.size(), "Should have 2 movie treasures");
        
        @SuppressWarnings("unchecked")
        List<String> genres = (List<String>) model.asMap().get("genres");
        assertEquals(3, genres.size(), "Should have 3 genre treasures");
    }

    @Test
    @DisplayName("Should search movie treasures by name successfully")
    public void testSearchMovieTreasuresByName() {
        String result = moviesController.searchMovieTreasures("pirate", null, null, model);
        
        assertNotNull(result, "Result should not be null, matey!");
        assertEquals("movies", result, "Should return movies template");
        
        // Verify search results
        @SuppressWarnings("unchecked")
        List<Movie> movies = (List<Movie>) model.asMap().get("movies");
        assertEquals(1, movies.size(), "Should find 1 movie treasure with 'pirate' in name");
        assertEquals("The Pirate's Treasure", movies.get(0).getMovieName());
        
        // Verify search parameters are preserved
        assertEquals("pirate", model.asMap().get("searchName"));
        assertTrue(model.containsAttribute("searchMessage"), "Should have search message");
    }

    @Test
    @DisplayName("Should search movie treasures by ID successfully")
    public void testSearchMovieTreasuresById() {
        String result = moviesController.searchMovieTreasures(null, 1L, null, model);
        
        assertNotNull(result, "Result should not be null, ye scurvy dog!");
        assertEquals("movies", result, "Should return movies template");
        
        // Verify search results
        @SuppressWarnings("unchecked")
        List<Movie> movies = (List<Movie>) model.asMap().get("movies");
        assertEquals(1, movies.size(), "Should find exactly 1 movie treasure with ID 1");
        assertEquals("The Pirate's Treasure", movies.get(0).getMovieName());
        
        // Verify search parameters are preserved
        assertEquals(1L, model.asMap().get("searchId"));
        assertTrue(model.containsAttribute("searchMessage"), "Should have search message");
    }

    @Test
    @DisplayName("Should search movie treasures by genre successfully")
    public void testSearchMovieTreasuresByGenre() {
        String result = moviesController.searchMovieTreasures(null, null, "Adventure", model);
        
        assertNotNull(result, "Result should not be null, arrr!");
        assertEquals("movies", result, "Should return movies template");
        
        // Verify search results
        @SuppressWarnings("unchecked")
        List<Movie> movies = (List<Movie>) model.asMap().get("movies");
        assertEquals(1, movies.size(), "Should find 1 adventure movie treasure");
        assertEquals("Adventure", movies.get(0).getGenre());
        
        // Verify search parameters are preserved
        assertEquals("Adventure", model.asMap().get("searchGenre"));
        assertTrue(model.containsAttribute("searchMessage"), "Should have search message");
    }

    @Test
    @DisplayName("Should handle empty search results with pirate message")
    public void testSearchMovieTreasuresNoResults() {
        String result = moviesController.searchMovieTreasures("nonexistent", null, null, model);
        
        assertNotNull(result, "Result should not be null, matey!");
        assertEquals("movies", result, "Should return movies template");
        
        // Verify empty results
        @SuppressWarnings("unchecked")
        List<Movie> movies = (List<Movie>) model.asMap().get("movies");
        assertTrue(movies.isEmpty(), "Should have empty treasure chest for non-matching search");
        
        // Verify pirate-themed message
        String searchMessage = (String) model.asMap().get("searchMessage");
        assertNotNull(searchMessage, "Should have search message for empty results");
        assertTrue(searchMessage.contains("Shiver me timbers"), "Should contain pirate language");
    }

    @Test
    @DisplayName("Should handle invalid search criteria with error message")
    public void testSearchMovieTreasuresInvalidCriteria() {
        String result = moviesController.searchMovieTreasures(null, null, null, model);
        
        assertNotNull(result, "Result should not be null, ye landlubber!");
        assertEquals("movies", result, "Should return movies template");
        
        // Should return all movies when criteria is invalid
        @SuppressWarnings("unchecked")
        List<Movie> movies = (List<Movie>) model.asMap().get("movies");
        assertEquals(2, movies.size(), "Should return all movie treasures for invalid criteria");
        
        // Verify error message
        String searchError = (String) model.asMap().get("searchError");
        assertNotNull(searchError, "Should have search error message");
        assertTrue(searchError.contains("Arrr"), "Should contain pirate language in error");
    }

    @Test
    @DisplayName("Should handle empty string search criteria")
    public void testSearchMovieTreasuresEmptyStrings() {
        String result = moviesController.searchMovieTreasures("", null, "", model);
        
        assertNotNull(result, "Result should not be null, arrr!");
        assertEquals("movies", result, "Should return movies template");
        
        // Should return all movies for empty string criteria
        @SuppressWarnings("unchecked")
        List<Movie> movies = (List<Movie>) model.asMap().get("movies");
        assertEquals(2, movies.size(), "Should return all movie treasures for empty criteria");
        
        // Should have error message for invalid criteria
        assertTrue(model.containsAttribute("searchError"), "Should have search error for empty criteria");
    }

    @Test
    @DisplayName("Should preserve all search parameters in model")
    public void testSearchMovieTreasuresPreservesParameters() {
        String result = moviesController.searchMovieTreasures("test", 1L, "Adventure", model);
        
        assertNotNull(result, "Result should not be null, matey!");
        assertEquals("movies", result, "Should return movies template");
        
        // Verify all search parameters are preserved
        assertEquals("test", model.asMap().get("searchName"));
        assertEquals(1L, model.asMap().get("searchId"));
        assertEquals("Adventure", model.asMap().get("searchGenre"));
        
        // Should always include genres for dropdown
        assertTrue(model.containsAttribute("genres"), "Should always include genres");
    }

    @Test
    @DisplayName("Should get movie details successfully")
    public void testGetMovieDetails() {
        String result = moviesController.getMovieDetails(1L, model);
        
        assertNotNull(result, "Result should not be null, arrr!");
        assertEquals("movie-details", result, "Should return movie-details template");
        
        // Verify movie details are in model
        assertTrue(model.containsAttribute("movie"), "Model should contain movie");
        assertTrue(model.containsAttribute("movieIcon"), "Model should contain movie icon");
        assertTrue(model.containsAttribute("allReviews"), "Model should contain reviews");
    }

    @Test
    @DisplayName("Should handle movie not found with error page")
    public void testGetMovieDetailsNotFound() {
        String result = moviesController.getMovieDetails(999L, model);
        
        assertNotNull(result, "Result should not be null, ye scurvy dog!");
        assertEquals("error", result, "Should return error template for non-existent movie");
        
        // Verify error details are in model
        assertTrue(model.containsAttribute("title"), "Model should contain error title");
        assertTrue(model.containsAttribute("message"), "Model should contain error message");
    }

    @Test
    @DisplayName("Should integrate with movie service properly")
    public void testMovieServiceIntegration() {
        List<Movie> movies = mockMovieService.getAllMovies();
        assertEquals(2, movies.size(), "Should have 2 movie treasures from service");
        assertEquals("The Pirate's Treasure", movies.get(0).getMovieName());
        
        List<String> genres = mockMovieService.getAllGenreTreasures();
        assertEquals(3, genres.size(), "Should have 3 genre treasures from service");
        
        assertTrue(mockMovieService.isValidSearchCriteria("test", null, null), 
                  "Should validate search criteria properly");
        assertTrue(mockMovieService.isValidSearchCriteria(null, 1L, null), 
                  "Should validate ID criteria properly");
    }
}