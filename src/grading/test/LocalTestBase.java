package grading.test;

import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

import org.junit.BeforeClass;

import ca.ece.ubc.cpen221.mp5.Restaurant;
import ca.ece.ubc.cpen221.mp5.RestaurantDB;
import ca.ece.ubc.cpen221.mp5.Review;
import ca.ece.ubc.cpen221.mp5.User;
import grading.student.RestaurantDBExtractor;
import grading.student.RestaurantExtractor;
import grading.student.ReviewExtractor;
import grading.student.UserExtractor;

public class LocalTestBase {

    protected static RestaurantDB db;

    protected static Set<Restaurant> allRestaurants;
    protected static Map<String,Restaurant> restaurantIdMap;

    protected static Set<User> allUsers;
    protected static Map<String,User> userIdMap;

    protected static Set<Review> allReviews;
    protected static Map<String,Review> reviewIdMap;

    protected static Map<String,Set<Review>> userReviewMap;
    protected static Map<String,Set<Review>> restaurantReviewMap;

    /**
     * Setup one static database... should be fine as long as there are no writes
     */
    @BeforeClass
    public static void setup() {
        // create the database
        db = loadDatabase("restaurants.json", "reviews.json", "users.json");

        // find all Restaurant instances in the database that are fields of a class within the mp5 package
        allRestaurants = RestaurantDBExtractor.getAllRestaurants(db);
        // make a map of IDs to restaurants
        restaurantIdMap = new HashMap<String,Restaurant>(allRestaurants.size());
        for (Restaurant r : allRestaurants) {
            String id = RestaurantExtractor.getBusinessId(r);
            restaurantIdMap.put(id, r);
        }

        // find all User instances in the database that are fields of a class within the mp5 package
        allUsers = RestaurantDBExtractor.getAllUsers(db);

        // map of ids to users
        userIdMap = new HashMap<String,User>(allUsers.size());
        for (User u : allUsers) {
            String id = UserExtractor.getUserId(u);
            userIdMap.put(id, u);
        }

        // find all Review instances in the database that are fields of a class within the mp5 package
        allReviews = RestaurantDBExtractor.getAllReviews(db);

        // map of ids to users
        reviewIdMap = new HashMap<String,Review>(allReviews.size());
        for (Review r : allReviews) {
            String id = ReviewExtractor.getReviewId(r);
            reviewIdMap.put(id, r);
        }

        // group reviews by user and restaurant
        userReviewMap = new HashMap<>();
        restaurantReviewMap = new HashMap<>();
        for (Review r : allReviews) {
            String userId = ReviewExtractor.getUserId(r);
            String restaurantId = ReviewExtractor.getBusinessId(r);
            if (userIdMap.containsKey(userId) && restaurantIdMap.containsKey(restaurantId)) {
                Set<Review> revs = userReviewMap.get(userId);
                if (revs == null) {
                    revs = new HashSet<Review>();
                    userReviewMap.put(userId, revs);
                }
                revs.add(r);

                revs = restaurantReviewMap.get(restaurantId);
                if (revs == null) {
                    revs = new HashSet<Review>();
                    restaurantReviewMap.put(restaurantId, revs);
                }
                revs.add(r);
            }
        }

    }

    /**
     * Find a file somewhere in the directory tree starting at the parent of the working directory
     * @param name name of file
     * @return the file if found, null otherwise
     */
    public static File findFile(String name) {
        File f = new File(name);
        if (!f.exists()) {
            File parent = new File(".");

            // recursively find file?
            Queue<File> files = new LinkedList<>();
            files.add(parent);

            while (!files.isEmpty()) {
                parent = files.poll();
                f = new File(parent, name);
                if (f.exists()) {
                    return f;
                }
                if (parent.isDirectory()) {
                    for (File child : parent.listFiles()) {
                        files.add(child);
                    }
                }
            }

            return null;
        }

        return f;
    }

    public static RestaurantDB loadDatabase(String restaurantFileName, String reviewFileName, String userFileName) {
        // find files somewhere in the directory tree
        File restaurantFile = findFile(restaurantFileName);
        File reviewFile = findFile(reviewFileName);
        File userFile = findFile(userFileName);

        if (restaurantFile == null || reviewFile == null || userFile == null) {
            throw new RuntimeException("Cannot find one of the data files");
        }
        // create the database
        return new RestaurantDB(restaurantFile.getAbsolutePath(), reviewFile.getAbsolutePath(), userFile.getAbsolutePath());
    }


}
