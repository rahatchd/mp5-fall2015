package grading.test;

import java.io.IOException;
import java.util.HashSet;
import java.util.Map;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.junit.Assert;
import org.junit.Test;

import grading.staff.RequestException;
import grading.staff.RestaurantDBClient;
import grading.student.RestaurantDBServerUtil;

public class UpdateTests extends ServerTestBase {
    public static final int TIMEOUT_MS = 30000;
    
    @Test(timeout=TIMEOUT_MS)
    public void testAddRestaurant() throws IOException, ParseException, RequestException {

        String restaurantStr = "{\"open\": true, \"url\": \"http://cpen221.ece.ubc.ca/antonio\", \"longitude\": -123.2431, \"neighborhoods\": [\"UBC Campus Area\"], \"business_id\": \"cpen221isawesomesauce\", \"name\": \"Antonio's\", \"categories\": [\"Cafes\", \"Restaurants\"], \"state\": \"BC\", \"type\": \"business\", \"stars\": 5.0, \"city\": \"Vancouver\", \"full_address\": \"1 UBC Ave\\n, Vancouver, Canada\", \"review_count\": 0, \"photo_url\": \"http://cpen221.ece.ubc.ca/antonio/pic.jpg\", \"schools\": [\"University of British Columbia\"], \"latitude\": 49.2641, \"price\": 1}";
        JSONParser parser = new JSONParser();
        JSONObject restaurant = (JSONObject) (parser.parse(restaurantStr));

        // we will try to ask server for restaurant back to check for equality
        RestaurantDBClient client = createConnectedClient();

        // add restaurant
        client.addRestaurant(restaurant);

        // Try to get restaurant back
        // Will throw a RequestException if failed to get a restaurant
        Map<String, ?> received = client.getRestaurant((String) (restaurant.get("business_id")));

        // close client
        client.disconnect();

        Assert.assertEquals("Received restaurant not same as added", restaurant, received);
    }

    @Test(timeout=TIMEOUT_MS)
    public void testAddExistingRestaurant() throws ParseException, IOException, RequestException {

        String cafe3Str = "{\"open\": true, \"url\": \"http://www.yelp.com/biz/cafe-3-berkeley\", \"longitude\": -122.260408, \"neighborhoods\": [\"Telegraph Ave\", \"UC Campus Area\"], \"business_id\": \"gclB3ED6uk6viWlolSb_uA\", \"name\": \"Cafe 3\", \"categories\": [\"Cafes\", \"Restaurants\"], \"state\": \"CA\", \"type\": \"business\", \"stars\": 2.0, \"city\": \"Berkeley\", \"full_address\": \"2400 Durant Ave\\nTelegraph Ave\\nBerkeley, CA 94701\", \"review_count\": 9, \"photo_url\": \"http://s3-media1.ak.yelpcdn.com/bphoto/AaHq1UzXiT6zDBUYrJ2NKA/ms.jpg\", \"schools\": [\"University of California at Berkeley\"], \"latitude\": 37.867417, \"price\": 1}";
        JSONParser parser = new JSONParser();
        JSONObject cafe3 = (JSONObject) (parser.parse(cafe3Str));

        RestaurantDBClient client = createConnectedClient();

        JSONObject response = client.addRestaurant(cafe3);
        boolean success = RestaurantDBServerUtil.isAddSuccessful(response);
        
        Assert.assertEquals("Repeated restaurant, should not be added", false, success);

        // close client
        client.disconnect();
    }

    @Test(timeout=TIMEOUT_MS)
    public void testAddExistingRestaurantDifferentId() throws ParseException, IOException, RequestException {

        String cafe3bStr = "{\"open\": true, \"url\": \"http://www.yelp.com/biz/cafe-3-berkeley\", \"longitude\": -122.260408, \"neighborhoods\": [\"Telegraph Ave\", \"UC Campus Area\"], \"business_id\": \"gXXXXXXXXXXXXXXXXXXXXX\", \"name\": \"Cafe 3\", \"categories\": [\"Cafes\", \"Restaurants\"], \"state\": \"CA\", \"type\": \"business\", \"stars\": 2.0, \"city\": \"Berkeley\", \"full_address\": \"2400 Durant Ave\\nTelegraph Ave\\nBerkeley, CA 94701\", \"review_count\": 9, \"photo_url\": \"http://s3-media1.ak.yelpcdn.com/bphoto/AaHq1UzXiT6zDBUYrJ2NKA/ms.jpg\", \"schools\": [\"University of California at Berkeley\"], \"latitude\": 37.867417, \"price\": 1}";
        JSONParser parser = new JSONParser();
        JSONObject cafe3b = (JSONObject) (parser.parse(cafe3bStr));

        // will try to get restaurant back
        Map<String, ?> received = null;

        RestaurantDBClient client = createConnectedClient();
        client.addRestaurant(cafe3b);

        // Will throw a RequestException if failed to get a restaurant
        try {
            received = client.getRestaurant((String) (cafe3b.get("business_id")));
        } catch (RequestException e) {
            // expecting to not receive restaurant back
            received = null;
        }

        // close client
        client.disconnect();

        Assert.assertNotEquals("Restaurant should not have been added", cafe3b, received);
    }

    @Test(timeout=TIMEOUT_MS)
    public void testAddReview() throws ParseException, IOException, RequestException {

        // Restaurant "Thai Noodle II" (D0bltNaJ-Ak6ejkUUpWDEw) has only 2
        // reviews. Add one more.
        String reviewStr = "{\"type\": \"review\", \"business_id\": \"D0bltNaJ-Ak6ejkUUpWDEw\", \"votes\": {\"cool\": 1, \"useful\": 0, \"funny\": 0}, \"review_id\": \"XXXXXCF5Q_ixYza7tr5GhQ\", \"text\": \"There's a rumbling in my tummy.\", \"stars\": 3, \"user_id\": \"y7XzbYsDOZQMILOEvJaGoQ\", \"date\": \"2015-10-13\"}";
        JSONParser parser = new JSONParser();
        JSONObject review = (JSONObject) (parser.parse(reviewStr));
        String restaurantName = "Thai Noodle II";

        // we will try to ask server for restaurant back to check for equality
        HashSet<Map<String, ?>> responses = new HashSet<>();

        RestaurantDBClient client = createConnectedClient();

        // add review
        client.addReview(review);

        // Try to get review back, until we've either seen the desired review
        // or we've retried too many times
        Map<String, ?> received = null;
        int retryCount = 0;
        int maxRetryCount = 50;
        try {
            do {
                received = client.getRandomReview(restaurantName);
                responses.add(received);
                ++retryCount;
            } while (retryCount < maxRetryCount && !(review.equals(received)));
        } catch (RequestException e) {
            JSONObject jsonObject = (JSONObject) (e.getResponse());
            System.out.println(jsonObject.toJSONString());
        }

        // close client
        client.disconnect();

        Assert.assertTrue("Added review not among selection received", responses.contains(review));

    }

}
