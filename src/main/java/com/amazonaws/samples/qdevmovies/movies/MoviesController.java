package com.amazonaws.samples.qdevmovies.movies;

import com.amazonaws.samples.qdevmovies.utils.MovieIconUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Optional;

@Controller
public class MoviesController {
    private static final Logger logger = LogManager.getLogger(MoviesController.class);

    @Autowired
    private MovieService movieService;

    @Autowired
    private ReviewService reviewService;

    @GetMapping("/movies")
    public String getMovies(org.springframework.ui.Model model) {
        logger.info("Fetching movies");
        model.addAttribute("movies", movieService.getAllMovies());
        model.addAttribute("genres", movieService.getAllGenreTreasures());
        return "movies";
    }

    /**
     * Ahoy matey! This be the treasure hunt endpoint for searching movies.
     * Accepts query parameters to filter the movie treasure chest.
     * 
     * @param name Optional movie name to search for (partial matches, arrr!)
     * @param id Optional specific movie ID to find
     * @param genre Optional genre to filter by
     * @param model Spring model for passing data to the view
     * @return The movies template with search results
     */
    @GetMapping("/movies/search")
    public String searchMovieTreasures(
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "id", required = false) Long id,
            @RequestParam(value = "genre", required = false) String genre,
            org.springframework.ui.Model model) {
        
        logger.info("Ahoy! Starting movie treasure hunt with name: '{}', id: '{}', genre: '{}'", 
                   name, id, genre);
        
        try {
            // Search for movie treasures using specific exception handling!
            List<Movie> searchResults = movieService.searchMovieTreasures(name, id, genre);
            
            // Prepare the treasure chest for display
            model.addAttribute("movies", searchResults);
            model.addAttribute("genres", movieService.getAllGenreTreasures());
            model.addAttribute("searchName", name);
            model.addAttribute("searchId", id);
            model.addAttribute("searchGenre", genre);
            
            // Add search result message with pirate flair, arrr!
            if (searchResults.isEmpty()) {
                model.addAttribute("searchMessage", 
                    "Shiver me timbers! No movie treasures found matching yer search criteria. " +
                    "Try adjusting yer search terms, ye landlubber!");
                logger.info("No movie treasures found for search criteria");
            } else {
                String treasureMessage = searchResults.size() == 1 ? 
                    "Arrr! Found 1 movie treasure matching yer search!" :
                    String.format("Batten down the hatches! Found %d movie treasures matching yer search!", 
                                searchResults.size());
                model.addAttribute("searchMessage", treasureMessage);
                logger.info("Found {} movie treasures matching search criteria", searchResults.size());
            }
            
        } catch (InvalidSearchCriteriaException e) {
            logger.warn("Invalid search criteria provided: {}", e.getMessage());
            model.addAttribute("movies", movieService.getAllMovies());
            model.addAttribute("genres", movieService.getAllGenreTreasures());
            model.addAttribute("searchError", e.getMessage());
            model.addAttribute("searchName", name);
            model.addAttribute("searchId", id);
            model.addAttribute("searchGenre", genre);
        } catch (MovieDataLoadException e) {
            logger.error("Movie data loading error during search: {}", e.getMessage(), e);
            model.addAttribute("movies", movieService.getAllMovies());
            model.addAttribute("genres", movieService.getAllGenreTreasures());
            model.addAttribute("searchError", 
                "Blimey! A scurvy bug with the movie data prevented the treasure hunt. Please try again, me hearty!");
            model.addAttribute("searchName", name);
            model.addAttribute("searchId", id);
            model.addAttribute("searchGenre", genre);
        } catch (IllegalArgumentException e) {
            logger.error("Invalid argument during search: {}", e.getMessage(), e);
            model.addAttribute("movies", movieService.getAllMovies());
            model.addAttribute("genres", movieService.getAllGenreTreasures());
            model.addAttribute("searchError", 
                "Arrr! Invalid search parameters provided, matey! Check yer input and try again.");
            model.addAttribute("searchName", name);
            model.addAttribute("searchId", id);
            model.addAttribute("searchGenre", genre);
        }
        
        return "movies";
    }

    @GetMapping("/movies/{id}/details")
    public String getMovieDetails(@PathVariable("id") Long movieId, org.springframework.ui.Model model) {
        logger.info("Fetching details for movie ID: {}", movieId);
        
        Optional<Movie> movieOpt = movieService.getMovieById(movieId);
        if (!movieOpt.isPresent()) {
            logger.warn("Movie with ID {} not found", movieId);
            model.addAttribute("title", "Movie Not Found");
            model.addAttribute("message", "Movie with ID " + movieId + " was not found.");
            return "error";
        }
        
        Movie movie = movieOpt.get();
        model.addAttribute("movie", movie);
        model.addAttribute("movieIcon", MovieIconUtils.getMovieIcon(movie.getMovieName()));
        model.addAttribute("allReviews", reviewService.getReviewsForMovie(movie.getId()));
        
        return "movie-details";
    }
}