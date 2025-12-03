package com.amazonaws.samples.qdevmovies.movies;

/**
 * Arrr! This exception be thrown when movie data cannot be loaded from the treasure chest, matey!
 * Used for specific data loading failures instead of general exceptions.
 */
public class MovieDataLoadException extends RuntimeException {
    
    public MovieDataLoadException(String message) {
        super(message);
    }
    
    public MovieDataLoadException(String message, Throwable cause) {
        super(message, cause);
    }
    
    public MovieDataLoadException(Throwable cause) {
        super("Blimey! Failed to load movie treasures from the data chest", cause);
    }
}