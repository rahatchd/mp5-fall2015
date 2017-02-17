package grading.student;

import org.json.simple.JSONObject;

public class RestaurantDBServerUtil {

    /**
     * Given a JSON-formatted server response, detect whether an 
     * add operation was successful
     * @param response
     * @return
     */
    public static boolean isAddSuccessful(JSONObject response) {
        // TODO: fill in this function to determine whether
        // the provide response indicates success
        // example
        boolean success = ((String)response.get("status")).toUpperCase().contains("OK");
        throw new RuntimeException("Not implemented");
    }

    /**
     * Creates a string to send to the server for adding a restaurant
     * @param r restaurant in JSON format
     * @return request line
     */
    public static String createAddRestaurantRequest(JSONObject r) {
        // example
        String out = "addRestaurant(\"" + r.toJSONString() + "\")";
        throw new RuntimeException("Not implemented");
    }
    
    /**
     * Crates a string to send to the server for getting a restaurant 
     * @param businessId
     * @return
     */
    public static String createGetRestaurantRequest(String businessId) {
        // example
        String out = "getRestaurant(\"" + businessId + "\")";
        throw new RuntimeException("Not implemented");
    }
    
    /**
     * Creates a string to send to the server for adding a review
     * @param r review in JSON format
     * @return request line
     */
    public static String createAddReviewRequest(JSONObject r) {
        // example
        String out = "addReview(\"" + r.toJSONString() + "\")";
        throw new RuntimeException("Not implemented");
    }
    
    /**
     * Crates a string to send to the server for getting a random reveiw
     * @param name restaurant name
     * @return
     */
    public static String createRandomReviewRequest(String name) {
        // example
        String out = "randomReview(\"" + name + "\")";
        throw new RuntimeException("Not implemented");
    }
    
    /**
     * Creates a string to send to the server for adding a user
     * @param user restaurant in JSON format
     * @return request line
     */
    public static String createAddUserRequest(JSONObject user) {
        // example
        String out = "addUser(\"" + user.toJSONString() + "\")";
        throw new RuntimeException("Not implemented");
    }
    
}
