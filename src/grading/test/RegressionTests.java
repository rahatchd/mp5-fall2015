package grading.test;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.junit.Assert;
import org.junit.Test;

import ca.ece.ubc.cpen221.mp5.Restaurant;
import ca.ece.ubc.cpen221.mp5.RestaurantDB;
import ca.ece.ubc.cpen221.mp5.Review;
import ca.ece.ubc.cpen221.mp5.User;
import ca.ece.ubc.cpen221.mp5.statlearning.Algorithms;
import ca.ece.ubc.cpen221.mp5.statlearning.MP5Function;
import grading.student.RestaurantExtractor;
import grading.student.ReviewExtractor;
import grading.student.UserExtractor;

public class RegressionTests extends LocalTestBase {
    
    // 10 second timeout
    private static final int TIMOUT_MS = 10000;
    
    /**
     * Test a predicted rating with given feature matches expected
     * @param u user
     * @param db database
     * @param r restaurant to predict rating
     * @param featureFunction feature function
     * @param expected expected solution
     */
    public static void testPredictor(User u, RestaurantDB db, Restaurant r, MP5Function featureFunction, double expected) {

        MP5Function predictor = Algorithms.getPredictor(u, db, featureFunction);
        double rating = predictor.f(r, db);
        Assert.assertEquals("Predictions do not equal", expected, rating, 1e-2); // prediction to 2 decimal places

    }

    @Test(timeout=TIMOUT_MS)
    public void testByPrice() {
        MP5Function priceFeature = (Restaurant r, RestaurantDB db) -> { return RestaurantExtractor.getPrice(r); };
        User sean = userIdMap.get("P3usQcIegPCl-cdH4IDirg");               // Sean B. 21 reviews
        Restaurant wings = restaurantIdMap.get("ysED_563t-tMmi85Y2GIsw");  // Wing Fiesta
        testPredictor(sean, db, wings, priceFeature, 2.9);
    }
    
    @Test(timeout=TIMOUT_MS)
    public void testByStars() {
        MP5Function starsFeature = (Restaurant r, RestaurantDB db) -> { return RestaurantExtractor.getStars(r); };
        User michael = userIdMap.get("FupbUDKwsiLnJ2epwYs9pw");             // Michael L. 55 reviews
        Restaurant foleys = restaurantIdMap.get("rcu96dLkUGU4Naj7GqOHDQ");  // Foley's
        testPredictor(michael, db, foleys, starsFeature, 4.10);
    }
    
    @Test(timeout=TIMOUT_MS)
    public void testBestPredictor() {
        
        List<MP5Function> featureFunctions = new ArrayList<>();
        
        // Lambda feature functions used for testing accuracy
        MP5Function price = (Restaurant r, RestaurantDB db) -> { return RestaurantExtractor.getPrice(r); };
        MP5Function stars = (Restaurant r, RestaurantDB db) -> { return RestaurantExtractor.getStars(r); };
        MP5Function latitude = (Restaurant r, RestaurantDB db) -> { return RestaurantExtractor.getLatitude(r); };
        MP5Function longitude = (Restaurant r, RestaurantDB db) -> { return RestaurantExtractor.getLongitude(r); };
        
        featureFunctions.add(price);
        featureFunctions.add(stars);
        featureFunctions.add(latitude);
        featureFunctions.add(longitude);
        
        User sean = userIdMap.get("P3usQcIegPCl-cdH4IDirg");  // Sean B. 21 reviews
        MP5Function bestPredictor = Algorithms.getBestPredictor(sean, db, featureFunctions);
        double r2 = computePredictionR2(sean, db, bestPredictor);
        double eps = 1e-4;
        
        // prediction feature should be stars
        Assert.assertEquals("Best prediction R2 does not match computed", 0.213179, r2, eps );
        
    }
   
    public double computePredictionR2(User user, RestaurantDB db, MP5Function predictor) {
        String userId = UserExtractor.getUserId(user);
        Set<Review> userReviews = userReviewMap.get(userId);
        return computePredictionR2(userReviews, db, predictor);
    }
    
    /**
     * Use formula for R2 measure
     * @param reviews
     * @param db
     * @param predictor
     * @return
     */
    public double computePredictionR2(Set<Review> reviews, RestaurantDB db, MP5Function predictor) {
        
        double r2 = 0;
        double meanStars = 0;
        for (Review r : reviews) {
            // extract star rating
            String restaurantId = ReviewExtractor.getBusinessId(r);
            Restaurant restaurant = restaurantIdMap.get(restaurantId);
            double predicted = predictor.f(restaurant, db);
            
            double stars = ReviewExtractor.getStars(r);
            double err = (stars-predicted);
            r2 += err*err;
            meanStars += stars;
        }
        
        meanStars = meanStars/reviews.size();
        double sstotal = 0;
        for (Review r : reviews) {
            double stars = ReviewExtractor.getStars(r);
            double err = stars-meanStars;
            sstotal += err*err;
        }
        
        r2 = 1-(r2/sstotal); 
        
        return r2;
    }
    
    public MP5Function getBestPredictor(User user, RestaurantDB db, List<MP5Function> predictors) {
        
        // get reviews by user
        String userId = UserExtractor.getUserId(user);
        Set<Review> userReviews = userReviewMap.get(userId);
        
        MP5Function bestPredictor = null;
        double bestR2 = Double.NEGATIVE_INFINITY;
        
        for (MP5Function predictor : predictors) {
            double r2 = computePredictionR2(userReviews, db, predictor);
            if (r2 > bestR2) {
                bestPredictor = predictor;
                bestR2 = r2;
            }
        }
        
        return bestPredictor;
    }

}
