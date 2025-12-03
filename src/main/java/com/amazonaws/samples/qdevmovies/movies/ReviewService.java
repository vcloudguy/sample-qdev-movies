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
import java.util.List;
import java.util.ArrayList;
import java.util.Scanner;

@Service
public class ReviewService {
    private static final Logger logger = LogManager.getLogger(ReviewService.class);

    public List<Review> getReviewsForMovie(long movieId) {
        List<Review> reviews = new ArrayList<>();
        try {
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream("mock-reviews.json");
            if (inputStream == null) {
                logger.warn("Arrr! Review treasure chest file 'mock-reviews.json' not found for movie {}", movieId);
                return reviews; // Return empty list instead of throwing exception
            }
            
            Scanner scanner = new Scanner(inputStream, StandardCharsets.UTF_8.name());
            String jsonContent = scanner.useDelimiter("\\A").next();
            scanner.close();
            
            JSONObject reviewsData = new JSONObject(jsonContent);
            if (reviewsData.has(String.valueOf(movieId))) {
                JSONArray movieReviews = reviewsData.getJSONArray(String.valueOf(movieId));
                for (int i = 0; i < movieReviews.length(); i++) {
                    JSONObject reviewObj = movieReviews.getJSONObject(i);
                    reviews.add(new Review(
                        reviewObj.getString("userName"),
                        reviewObj.getString("avatarEmoji"),
                        reviewObj.getDouble("rating"),
                        reviewObj.getString("comment")
                    ));
                }
            }
        } catch (JSONException e) {
            logger.error("Scurvy bug in JSON parsing for movie {} reviews: {}", movieId, e.getMessage(), e);
            // Return empty list for non-critical operation
        } catch (IOException e) {
            logger.error("IO error loading reviews for movie {}: {}", movieId, e.getMessage(), e);
            // Return empty list for non-critical operation
        } catch (IllegalStateException e) {
            logger.error("Scanner state error loading reviews for movie {}: {}", movieId, e.getMessage(), e);
            // Return empty list for non-critical operation
        }
        return reviews;
    }
}
