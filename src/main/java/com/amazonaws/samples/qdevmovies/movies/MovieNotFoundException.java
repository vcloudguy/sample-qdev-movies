package com.amazonaws.samples.qdevmovies.movies;

/**
 * Arrr! This exception be thrown when a movie treasure cannot be found in our chest, matey!
 * Used for specific movie lookup failures instead of general exceptions.
 */
public class MovieNotFoundException extends RuntimeException {
    
    public MovieNotFoundException(String message) {
        super(message);
    }
    
    public MovieNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
    
    public MovieNotFoundException(Long movieId) {
        super("Shiver me timbers! No movie treasure found with ID: " + movieId);
    }
}