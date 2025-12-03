package com.amazonaws.samples.qdevmovies.movies;

/**
 * Arrr! This exception be thrown when invalid search criteria be provided, ye scurvy dog!
 * Used for specific search parameter validation failures instead of general exceptions.
 */
public class InvalidSearchCriteriaException extends RuntimeException {
    
    public InvalidSearchCriteriaException(String message) {
        super(message);
    }
    
    public InvalidSearchCriteriaException(String message, Throwable cause) {
        super(message, cause);
    }
    
    public InvalidSearchCriteriaException() {
        super("Arrr! Ye need to provide at least one search criterion, matey!");
    }
}